package com.device.management.computers.dao;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerEmployeeDao {
	
	@NotBlank(message = "Employee No is mandatory.")
	private String empNo;
	
	@NotBlank(message = "Computer name is mandatory.")
	private String computerName;

}
