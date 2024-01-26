package com.device.management.computers.service;

import java.util.List;

import com.device.management.computers.dao.ComputerEmployeeDao;
import com.device.management.computers.dao.ComputerRequestDao;
import com.device.management.computers.dao.ComputerResponseDao;
import com.device.management.computers.dao.ComputerResponseNotifyDao;
import com.device.management.computers.dao.ResponseObjectDao;


public interface ComputerService {
	/*
	 * author : greshma.john
	 * Interface declare APIs to save computer details, fetch computers,
	 * allocate computers to new employees,de-allocate computer, fetch list
	 * of computers assigned to employees
	 * 
	 */
	/*
	 * Request - computer details - name. mac and ip address, description , allocate emp no
	 * Response- saved computer details, notification if any, and save operation status
	 */
	public ResponseObjectDao<ComputerResponseNotifyDao> saveComputerDetails(ComputerRequestDao compDetails) ;
	/*
	 * Request- optional input computer name . 
	 * Response- List of computers with details.
	 */
	public List<ComputerResponseDao> getAllComputers(String computerName);
	/*
	 * Request- emp no of employee
	 * Response - list of computers with details(mac address, ip address, name etc)
	 * allocated to employee
	 * 
	 */
	public List<ComputerResponseDao> getAllComputerOfEmployee(String empNo);
	
	/*
	 *Request-Computer name
	 *Response - details of the computer and status of de-allocation 
	 */
	public ResponseObjectDao<ComputerResponseDao> removeAllocatedCompOfEmployee(String compName);
	/*
	 * Request -ComputerEmployeeDao (emp no, computer name)
	 * Response - ComputerResponseNotifyDao( computer details such as computer name, ip address, mac address
	 * description and allocated employee. Notification object consist of level, emp abbrevation and message)
	 * 
	 */
	public ResponseObjectDao<ComputerResponseNotifyDao> updateComputerAllocation(ComputerEmployeeDao compEmp);

}
