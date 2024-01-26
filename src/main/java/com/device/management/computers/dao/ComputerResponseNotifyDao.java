package com.device.management.computers.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerResponseNotifyDao {

	private ComputerResponseDao computerDetails;
	private NotificationDao notificationToSuperVisor;

	
}
