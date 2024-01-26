package com.device.management.computers.controller;


import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.management.computers.dao.EmployeeRequestDao;
import com.device.management.computers.dao.EmployeeResponseDao;
import com.device.management.computers.dao.ResponseObjectDao;
import com.device.management.computers.service.EmployeeService;

/*
 *  author : greshma.john
 *  controller to save and fetch employee details.
 *  
 */
@RestController()
@RequestMapping("api/v1/employee")
public class EmployeeController {
	

	private EmployeeService empService;
	
	public EmployeeController(EmployeeService empService) {
		this.empService=empService;
	}
	
	/*
	 * save employee details such as name , data of join and designation .
	 * employee abbrevation consist of 3 words (first character of first name and first 2 character 
	 * of last name) and emp no has to be generated from the given inputs.
	 */
	@PostMapping("employee")
	public ResponseEntity<ResponseObjectDao<EmployeeResponseDao>>  employeeSave(@RequestBody EmployeeRequestDao employeeRequest) {
		ResponseObjectDao<EmployeeResponseDao> resp=empService.saveEmployee(employeeRequest);
		if(resp.getResponseStatus().equals("SUCCESS"))
			return new ResponseEntity<>(resp,HttpStatus.OK);
		
		return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * fetch all list of employees.
	 */

	@GetMapping("employee")
	public ResponseEntity<ResponseObjectDao<List<EmployeeResponseDao>>> getEmployeeList(){
		ResponseObjectDao<List<EmployeeResponseDao>> resp=new ResponseObjectDao<>();
		List<EmployeeResponseDao> empList=empService.ListEmployees();
		resp.setResponseResult(empList);
		if(empList.isEmpty()) {
			resp.setResponseMessage("No data found");
			resp.setResponseStatus("FAILURE");
			return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
		}else {
			resp.setResponseMessage("Employee details fetched successfully !");
			resp.setResponseStatus("SUCCESS");
			return new ResponseEntity<>(resp,HttpStatus.OK);
		}
		
		
		
	}
}
