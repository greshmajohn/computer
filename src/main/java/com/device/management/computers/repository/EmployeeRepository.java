package com.device.management.computers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import com.device.management.computers.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {
	
	@Query("select f from EmployeeEntity f where f.employeeNo=:empNo")
	Optional<EmployeeEntity> getEmployeeByEmpNo( String empNo);

}
