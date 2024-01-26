package com.device.management.computers.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDao {
	
	private String level;
	private String employeeAbbreviation;
	private String message;

}
