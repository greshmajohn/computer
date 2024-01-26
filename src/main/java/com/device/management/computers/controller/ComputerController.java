package com.device.management.computers.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.device.management.computers.dao.ComputerEmployeeDao;
import com.device.management.computers.dao.ComputerRequestDao;
import com.device.management.computers.dao.ComputerResponseDao;
import com.device.management.computers.dao.ComputerResponseNotifyDao;
import com.device.management.computers.dao.ResponseObjectDao;
import com.device.management.computers.service.ComputerService;

import jakarta.validation.Valid;

/*
 * author -greshma.john
 * 
 * Controller class defining apis to:
 *       1.save computer details
 *       2.fetch all computers
 *       3.fetch single computer details
 *       4.fetch computers allocated to employee
 *       5.de-allocate computer from employee
 *       6.allocate new computer to an employee
 */

@RestController
@RequestMapping("api/v1/device")
public class ComputerController {

	private ComputerService computerService;

	public ComputerController(ComputerService computerService) {
		this.computerService = computerService;
	}

	/*
	 * API tp save computer details and send the saved computer info along with with notification 
	 * if 3 or more computers are allocated to same employee.
	 */
	@PostMapping("computer")
	public ResponseEntity<ResponseObjectDao<ComputerResponseNotifyDao>> saveComputerDetails(
			@RequestBody @Valid ComputerRequestDao computerDetails)  {
		ResponseObjectDao<ComputerResponseNotifyDao> resp = computerService.saveComputerDetails(computerDetails);
		return resp.getResponseStatus().equals("SUCCESS") ? new ResponseEntity<>(resp, HttpStatus.OK)
				: new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * Fetch all computers stored in database.
	 * If computer name is given a input fetch the details of the computer and if the
	 * given computer name is not avaiable throw DataNotFound custom exception.
	 */
	@GetMapping("computer")
	public ResponseEntity<ResponseObjectDao<List<ComputerResponseDao>>> getComputers(@RequestParam(required=false) String computerName ){
		ResponseObjectDao<List<ComputerResponseDao>> resp=new ResponseObjectDao<>();
		List<ComputerResponseDao> compResponse=computerService.getAllComputers(computerName);
		if(compResponse==null||compResponse.isEmpty()) {
			resp.setResponseMessage("No data found .");
			resp.setResponseStatus("FAILURE");
			return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
		}else {
			resp.setResponseMessage("Data fetched succesfully !");
			resp.setResponseStatus("SUCCESS");
			resp.setResponseResult(compResponse);
			return new ResponseEntity<>(resp,HttpStatus.OK);
		}
		
		
	}
	
	/*
	 * API to fetch the computers allocated to an employee.
	 * Return the list of computer details allocated to employee.
	 */
	@GetMapping("computer-of-employee")
	public ResponseEntity<ResponseObjectDao<List<ComputerResponseDao>>> getComputersForEmployee(@RequestParam String employeeNo){
		ResponseObjectDao<List<ComputerResponseDao>> resp=new ResponseObjectDao<>();
		List<ComputerResponseDao> compResult=computerService.getAllComputerOfEmployee(employeeNo);
		resp.setResponseMessage("Computer details fetched successfully !");
		resp.setResponseStatus("SUCCESS");
		resp.setResponseResult(compResult);
		return new ResponseEntity<>(resp,
				HttpStatus.OK);
		
	}
	/*
	 * API to de-allocate computer allocated to employee.
	 * computer name should be given an input to API in order to de-allocate the 
	 * existing employee
	 * 
	 */
	@PostMapping("deallocate-computer")
	public ResponseEntity<ResponseObjectDao<ComputerResponseDao>> removeDevice(
			@RequestParam String computerName)  {
		
		return new ResponseEntity<>(computerService.removeAllocatedCompOfEmployee(computerName),HttpStatus.OK);
	}
	/*
	 * allocate already existing computer to another employee.
	 * If the computer is already allocated an exception will be thrown to de-allocate
	 * the device first. Take computer name and emp no as inputs.
	 * If the computer name or emp no given is not valid, API will throw DataNotFound exception .
	 */
	@PostMapping("update-computer-employee")
	public ResponseEntity<ResponseObjectDao<ComputerResponseNotifyDao>> updateDeviceAndEmployee(
			@RequestBody @Valid ComputerEmployeeDao computerDetails)  {
		ResponseObjectDao<ComputerResponseNotifyDao> resp=computerService.updateComputerAllocation(computerDetails);
		
		if(resp.getResponseStatus().equals("SUCCESS")) {
			return new ResponseEntity<>(resp,HttpStatus.OK);
		}
		return  new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
	}
	
}
