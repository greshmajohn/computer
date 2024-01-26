package com.device.management.computers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.device.management.computers.dao.ComputerEmployeeDao;
import com.device.management.computers.dao.ComputerRequestDao;
import com.device.management.computers.dao.ComputerResponseDao;
import com.device.management.computers.dao.ComputerResponseNotifyDao;
import com.device.management.computers.dao.NotificationDao;
import com.device.management.computers.dao.ResponseObjectDao;
import com.device.management.computers.entity.ComputerEntity;
import com.device.management.computers.entity.EmployeeEntity;
import com.device.management.computers.exception.DataNotFoundException;
import com.device.management.computers.exception.InvalidOperationException;
import com.device.management.computers.repository.ComputerRepository;
import com.device.management.computers.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ComputerServiceImpl implements ComputerService {

	private EmployeeRepository empRepo;

	private ComputerRepository compRepo;

	public ComputerServiceImpl(EmployeeRepository empRepo, ComputerRepository compRepo) {
		this.empRepo = empRepo;
		this.compRepo = compRepo;

	}
	/*
	 * Save computer details to DB . If the save opertaion fail, throw data not found exception.
	 * 
	 */
	@Override
	public ResponseObjectDao<ComputerResponseNotifyDao> saveComputerDetails(ComputerRequestDao compDetails) {
		ResponseObjectDao<ComputerResponseNotifyDao> computerResult = new ResponseObjectDao<>();
		EmployeeEntity empObj = null;
		if (compDetails.getEmpNo() != null) {
			Optional<EmployeeEntity> empResult = empRepo.getEmployeeByEmpNo(compDetails.getEmpNo());
		
			if (empResult.isPresent()) {
				
				empObj = empResult.get();
				log.info("empResult: "+empObj);
			} else {
				log.error("Emp No not found orinvalid.");
				throw new DataNotFoundException(
						"Employee details not found for Employee No : " + compDetails.getEmpNo());

			}
		}
		ComputerEntity compObje = new ComputerEntity(compDetails.getMacAddress(), compDetails.getComputername(),
				compDetails.getIpAddress(), compDetails.getDescription(), empObj);
		ComputerEntity result = compRepo.save(compObje);
		if (result != null) {

			computerResult.setResponseMessage("Computer details saved successfully !");
			computerResult.setResponseStatus("SUCCESS");
			computerResult.setResponseResult(updateDeviceCountAndSetResponse(result));
		} else {
			computerResult.setResponseMessage("Save opertaion failed.");
			computerResult.setResponseStatus("FAILURE");

		}
		return computerResult;
	}

	/*
	 * update device count if an employee is allocated with a computer.
	 * if an employee is allocated with 3 or more computers send a notification message
	 */
	private ComputerResponseNotifyDao updateDeviceCountAndSetResponse(ComputerEntity result) {
		EmployeeEntity empResult = result.getEmpId();
		ComputerResponseNotifyDao resp = new ComputerResponseNotifyDao();
		ComputerResponseDao compResp=new ComputerResponseDao(result.getComputerName(), result.getMacAddress(),
				result.getIpAddress(), result.getDescription());
		if (empResult != null) {
			updateEmployeeDevice(empResult, resp);
			compResp.setEmployeeAbbrevation(empResult.getAbbrevation());
			compResp.setAssignedEmployee(empResult.getEmployeeName());
			resp.setComputerDetails(compResp);
		}
		return resp;

	}
	/*
	 * update no_of_device field of employee once a sytem is allocated 
	 */

	private void updateEmployeeDevice(EmployeeEntity empResult, ComputerResponseNotifyDao resp) {
		int deviceNo = empResult.getNoOfDevices() == null ? 1 : empResult.getNoOfDevices() + 1;
		empResult.setNoOfDevices(deviceNo);
		EmployeeEntity empRes = empRepo.save(empResult);
		if (empRes!=null&&deviceNo >= 3)
			log.info("employee is allocated with 3 or more devices");
			resp.setNotificationToSuperVisor(sendNotification(empResult));

	}

	/*
	 *if the device count of an employee is 3 or more, send a notification to 
	 *system administrator using external API -http://localhost:8080/api/notify
	 * The inputs passed to API are level, employee abbrevation and message.
	 * 
	 */
	private NotificationDao sendNotification(EmployeeEntity empRes) {
		RestTemplate restTemplate = new RestTemplate();
		
		String url = "http://localhost:8080/api/notify";
		String msg = "Employee " + empRes.getEmployeeName() + " is assigned with " + empRes.getNoOfDevices()
				+ " devices.";

		NotificationDao not = new NotificationDao("warning", empRes.getAbbrevation(), msg);

		HttpEntity<NotificationDao> request = new HttpEntity<>(not);

		log.info("notification request : "+request);
		return restTemplate.postForObject(url, request, NotificationDao.class);

	}
	/*
	 * get all computer details. If the computer name is given then
	 * fetch computer details of the given computer name otherwice fetch all
	 * computers.  
	 */

	@Override
	public List<ComputerResponseDao> getAllComputers(String computerName) {

		List<ComputerEntity> computerList=new ArrayList<>();
		if(computerName==null) {
			computerList=compRepo.findAll();
		}else {
			Optional<ComputerEntity> optioanlCompResult=compRepo.getComputerDetailsByName(computerName);
			if(optioanlCompResult.isPresent()) {
				computerList.add(optioanlCompResult.get());
			}else
				throw new DataNotFoundException("Given computer name : "+computerName +" not avaiable .");
		}
			
		
			
		return convertToCompRespo(computerList);
	}

	/*
	 * Convert ComputerEntity list to Dao to avoid exposing Id field
	 */
	private List<ComputerResponseDao> convertToCompRespo(List<ComputerEntity> computerList) {
		
		List<ComputerResponseDao> result=new ArrayList<>();
		if(computerList!=null) {
			for(ComputerEntity com:computerList) {
				result.add(new ComputerResponseDao(com.getComputerName(),com.getMacAddress(),com.getIpAddress(),com.getDescription(),com.getEmpId().getEmployeeName()));
			}
		}
		
		return result;
	}
	/*
	 * fetch all computer details of an employee.
	 */
	@Override
	public List<ComputerResponseDao> getAllComputerOfEmployee(String empNo) {
		List<ComputerResponseDao> result=new ArrayList<>();
		Optional<List<ComputerEntity>> compListOpt=compRepo.getComputerDetailsByEmp(empNo);
		if(compListOpt.isPresent()&&!compListOpt.get().isEmpty()) {
			for(ComputerEntity compObj:compListOpt.get()) {
				result.add(new ComputerResponseDao(compObj.getComputerName(),compObj.getMacAddress(),compObj.getIpAddress(),compObj.getDescription()));
			}
		}else
			throw new DataNotFoundException("No computers found for the given Employee No: "+empNo);
		return result;
	}
	/*
	 * Remove allocated computer from an employee.
	 * If the emp no given is invalid , an exception will be thrown - InvalidOperationException
	 * similary for invalid computer name.
	 * 
	 */

	@Override
	public ResponseObjectDao<ComputerResponseDao> removeAllocatedCompOfEmployee(String computerName) {
		ResponseObjectDao<ComputerResponseDao> result=new ResponseObjectDao<>();
		Optional<ComputerEntity> compResult=compRepo.getComputerDetailsByName(computerName);
		if(compResult.isPresent()) {
			ComputerEntity compObj=compResult.get();
			EmployeeEntity allocatedEmp=compObj.getEmpId();
			if(allocatedEmp==null)
				throw new InvalidOperationException("Computer " +computerName+" is either invalid or not eligible for de-allocation.");
			compObj.setEmpId(null);
			ComputerEntity updatedComp=compRepo.save(compObj);
			if(updatedComp!=null) {
				allocatedEmp.setNoOfDevices(allocatedEmp.getNoOfDevices()-1);
				empRepo.save(allocatedEmp);
				result.setResponseResult(new ComputerResponseDao(updatedComp.getComputerName(),updatedComp.getMacAddress(),
						updatedComp.getIpAddress(),updatedComp.getDescription()) );
			}
		}else
			throw new DataNotFoundException("Given computer name not found. ");
		
		result.setResponseMessage("Computer "+computerName+" is de-allocated successfully !");
		result.setResponseStatus("SUCCESS");
		return result;
	}
	
	/*
	 * update an employee with a computer which is not allocated to any one.
	 */

	@Override
	public ResponseObjectDao<ComputerResponseNotifyDao> updateComputerAllocation(ComputerEmployeeDao compEmp) {
	
		ResponseObjectDao<ComputerResponseNotifyDao> computerResult=new ResponseObjectDao<>();
		Optional<ComputerEntity> compObjOpt=compRepo.getComputerDetailsByName(compEmp.getComputerName()); 
		if(compObjOpt.isPresent()) {
			ComputerEntity compObj=compObjOpt.get();
			if(compObj.getEmpId()!=null)
				throw new InvalidOperationException("Computer "+compEmp.getComputerName()+ "is already allocated. Please de-allocate first.");
			
			Optional<EmployeeEntity> empObj=empRepo.getEmployeeByEmpNo(compEmp.getEmpNo());
			if(empObj.isPresent()) {
				compObj.setEmpId(empObj.get());
				ComputerEntity result=compRepo.save(compObj);
				
				if (result != null) {

					computerResult.setResponseMessage("Computer details saved successfully !");
					computerResult.setResponseStatus("SUCCESS");
					computerResult.setResponseResult(updateDeviceCountAndSetResponse(result));
				}else {
					computerResult.setResponseMessage("Computer allocation failed. ");
					computerResult.setResponseStatus("FAILURE");
				}
			}else
				throw new DataNotFoundException("given employee not found in DB");
			
			
		}else
			throw new DataNotFoundException("Computer name : "+compEmp.getComputerName()+" does not exists.");
		return computerResult;
	}

}
