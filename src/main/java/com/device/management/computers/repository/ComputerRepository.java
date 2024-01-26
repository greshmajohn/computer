package com.device.management.computers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.device.management.computers.entity.ComputerEntity;

public interface ComputerRepository extends JpaRepository<ComputerEntity, Long> {
	
	@Query("select f from ComputerEntity f where f.computerName=:computerName")
	Optional<ComputerEntity> getComputerDetailsByName(String computerName);
	
	@Query("select f from ComputerEntity f where f.empId.employeeNo=:empNo")
	Optional<List<ComputerEntity>> getComputerDetailsByEmp(String empNo);

}
