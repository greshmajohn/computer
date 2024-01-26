package com.device.management.computers.service;

import java.util.List;

import com.device.management.computers.dao.EmployeeRequestDao;
import com.device.management.computers.dao.EmployeeResponseDao;
import com.device.management.computers.dao.ResponseObjectDao;

public interface EmployeeService {

	/*
	 * author: greshma.john
	 * 
	 */
	/*
	 * Request-first name, last name, date of join, designation
	 * Response - Emp no. emp name and abbrevation.
	 */
	public ResponseObjectDao<EmployeeResponseDao> saveEmployee(EmployeeRequestDao requestDao);
	/*
	 * no request
	 * list of employee details such as name, emp no, abbrevation , desingation and date of join
	 */
	public List<EmployeeResponseDao> ListEmployees();
}
