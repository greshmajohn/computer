package com.device.management.computers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.device.management.computers.dao.EmployeeRequestDao;
import com.device.management.computers.dao.EmployeeResponseDao;
import com.device.management.computers.dao.ResponseObjectDao;
import com.device.management.computers.entity.EmployeeEntity;
import com.device.management.computers.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

/*
 * author : greshma.john
 * 
 * service implementation to save and fetch employee details.
 */

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository empRepo;

	public EmployeeServiceImpl(EmployeeRepository empRepo) {
		this.empRepo = empRepo;
	}
	/*
	 * save employee details such as emp no, name, abbrevation , desingation and other details
	 *  
	 */
	@Override
	public ResponseObjectDao<EmployeeResponseDao>  saveEmployee(EmployeeRequestDao requestDao) {
		
		ResponseObjectDao<EmployeeResponseDao> response=new ResponseObjectDao<>();

		String firstName = requestDao.getFirstName();
		String lastname = requestDao.getLastname();

		StringBuilder abbrevation = new StringBuilder(String.valueOf(firstName.charAt(0)))
				.append(lastname.substring(0, 2));
		String abbrv = abbrevation.toString().toLowerCase();

		EmployeeEntity employee = new EmployeeEntity(getEmployeeNo(abbrv), firstName + " " + lastname,
				requestDao.getDateOfJoining(), requestDao.getDesignation(), abbrv);
		EmployeeEntity empResult = empRepo.save(employee);
		if(empResult!=null&&empResult.getId()!=null) {
			response.setResponseMessage("Employee details saved to database successfully !");
			response.setResponseStatus("SUCCESS");
			response.setResponseResult(new EmployeeResponseDao(empResult.getEmployeeNo(),empResult.getEmployeeName(),empResult.getAbbrevation()));
		}else {
			response.setResponseMessage("Save Operation failed");
			response.setResponseStatus("FAILURE");
		}
			
		return response;
	}

	/*
	 * generate random emp number by taking few characters from the name 
	 * combined with random generated 2 or 3 digits.
	 */
	private String getEmployeeNo(String abbrv) {
		int random = (int) (Math.random() * 1000);
		String empNo = abbrv + random;
		Optional<EmployeeEntity> empData = empRepo.getEmployeeByEmpNo(empNo);
		log.info("present" + empData.isPresent());
		if (empData.isPresent()) {
			random = (int) (Math.random() * 100);

		}

		return abbrv + random;

	}

	/*
	 * fetch all list of employee details from DB.
	 */
	@Override
	public List<EmployeeResponseDao> ListEmployees() {
		List<EmployeeResponseDao> empList=new ArrayList<EmployeeResponseDao>();
		List<EmployeeEntity> list=empRepo.findAll();
		for(EmployeeEntity entity:list) {
			empList.add(new EmployeeResponseDao(entity.getEmployeeNo(),entity.getEmployeeName(),entity.getAbbrevation()));
			
		}
		return empList;
	}

}
