package com.device.management.computers.dao;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerRequestDao {

	@NotBlank(message = "Computer Name is mandatory")
	private String computername;
	
	@NotBlank(message = "Mac Address is mandatory")
	private String macAddress;
	
	@NotBlank(message = "IP Address is mandatory")
	private String ipAddress;
	
	private String description;
	private String empNo;
}
