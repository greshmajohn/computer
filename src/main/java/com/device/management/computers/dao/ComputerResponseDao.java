package com.device.management.computers.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComputerResponseDao {
	
	private String computerName;
	private String macAddress;
	private String ipAddress;
	private String description;
	private String assignedEmployee;
	private String employeeAbbrevation;
	public ComputerResponseDao(String computerName, String macAddress, String ipAddress, String description
			) {
		this.computerName = computerName;
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.description = description;
		
	}
	public ComputerResponseDao(String computerName, String macAddress, String ipAddress, String description
			,String assignedEmploy) {
		this(computerName,macAddress,ipAddress,description);
		this.assignedEmployee=assignedEmploy;
		
	}

}
