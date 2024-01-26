package com.device.management.computers.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.device.management.computers.dao.ComputerEmployeeDao;
import com.device.management.computers.dao.ComputerRequestDao;
import com.device.management.computers.dao.ComputerResponseDao;
import com.device.management.computers.dao.ComputerResponseNotifyDao;
import com.device.management.computers.dao.ResponseObjectDao;
import com.device.management.computers.repository.ComputerRepository;
import com.device.management.computers.repository.EmployeeRepository;
import com.device.management.computers.service.ComputerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ComputerController.class)
public class ComputerControllerTest {

	@Autowired
	MockMvc mvc;
	
	@MockBean
	ComputerService computerService;
	

	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void saveComputerDetails() throws JsonProcessingException, Exception {
		Mockito.when(computerService.saveComputerDetails(getComputerObj()))
		.thenReturn(getCompNotifyResp());
		
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/device/computer")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getComputerObj()))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
	

	@Test
	void getComputersSuccess() throws Exception {
	
		Mockito.when(computerService.getAllComputers(null))
		.thenReturn(getComputerList());
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/device/computer").accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk());
		
	}
	@Test
	void getComputersFailure() throws Exception {
		
		
		Mockito.when(computerService.getAllComputers(null))
		.thenReturn(null);
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/device/computer").accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isBadRequest());
		
	}
	@Test
	void getComputersForEmployee() throws Exception {
		

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/device/computer-of-employee?employeeNo=120").accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk());
		
		
	}
	@Test
	void removeDevice() throws Exception {
		

		
		
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/device/deallocate-computer?computerName=lenova")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		
		
	}
	@Test
	void updateDeviceAndEmployeeSuccess() throws JsonProcessingException, Exception {
		
		ResponseObjectDao<ComputerResponseNotifyDao> resp=new ResponseObjectDao<>();
		resp.setResponseStatus("SUCCESS");
		ComputerEmployeeDao empComp=new ComputerEmployeeDao("dell","we12");
		Mockito.when(computerService.updateComputerAllocation(empComp))
		.thenReturn(resp);
		
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/device/update-computer-employee")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(empComp))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		
	}
	@Test
	void updateDeviceAndEmployeeFailed() throws JsonProcessingException, Exception {
	
		
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/device/update-computer-employee")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ComputerEmployeeDao("dell","we12")))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
		
	}
	



	private List<ComputerResponseDao> getComputerList() {
		List<ComputerResponseDao> compList=new ArrayList<ComputerResponseDao>();
		compList.add(new ComputerResponseDao("lenova","10.10.10.19","122.11.1.1","laptop"));
		return compList;
	}

	private ResponseObjectDao<ComputerResponseNotifyDao> getCompNotifyResp() {
		ResponseObjectDao<ComputerResponseNotifyDao> resp =new ResponseObjectDao<>();
		resp.setResponseMessage("Computer data saved sucessfully !");
		resp.setResponseStatus("SUCCESS");
		return resp;
	}

	private ComputerRequestDao getComputerObj() {
		
		return new ComputerRequestDao("Dell","11.1.1.1","1.10.10.10","laptop",null);
	}
}
