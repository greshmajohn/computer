package com.device.management.computers.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="computer",uniqueConstraints={@UniqueConstraint(columnNames = {"macAddress" , "computer_name","ip_address"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="mac_address",nullable=false)
	private String macAddress;
	
	@Column(name="computer_name",nullable=false)
	private String computerName;
	
	@Column(name="ip_address",nullable=false)
	private String ipAddress;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="emp_id")
	private EmployeeEntity empId;
	
	public ComputerEntity(String macAddress,String computerName,String ipAddress,String description,EmployeeEntity empId) {
		this.macAddress=macAddress;
		this.computerName=computerName;
		this.ipAddress=ipAddress;
		this.description=description;
		this.empId=empId;
	}
	
}
