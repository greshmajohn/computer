package com.device.management.computers.entity;

import java.time.LocalDate;

import com.device.management.computers.dao.ResponseObjectDao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employee")
@Data
@NoArgsConstructor
public class EmployeeEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="employee_no", nullable=false)
	private String employeeNo;
	
	@Column(name="employee_name", nullable=false)
	private String employeeName;
	
	@Column(name="date_of_join")
	private LocalDate dateOfJoining;
	
	@Column(name="designation")
	private String designation;
	
	@Column(name="abbrevation")
	private String abbrevation;
	
	@Column(name="no_of_devices")
	private Integer noOfDevices;
	
	public EmployeeEntity(String employeeNo,String employeeName,LocalDate dateOfJoining,String designation, String abbrevation) {
		this.employeeNo=employeeNo;
		this.employeeName=employeeName;
		this.dateOfJoining=dateOfJoining;
		this.designation=designation;
		this.abbrevation=abbrevation;
	}

}
