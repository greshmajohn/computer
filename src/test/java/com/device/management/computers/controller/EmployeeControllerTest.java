package com.device.management.computers.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import com.device.management.computers.dao.EmployeeRequestDao;
import com.device.management.computers.dao.EmployeeResponseDao;
import com.device.management.computers.dao.ResponseObjectDao;
import com.device.management.computers.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	EmployeeService empService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void getEmployeeListFailed() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/employee").accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isBadRequest());
	}
	
	@Test
	void getEmployeeListSuccess() throws Exception {
		
		Mockito.when(empService.ListEmployees())
		.thenReturn(getEmployeeList());
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/employee").accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk());
	}
	@Test
	void employeeSave() throws JsonProcessingException, Exception {
		EmployeeRequestDao empRequest=new EmployeeRequestDao("jacob","mathew",LocalDate.now(),"Engineer");
		
		ResponseObjectDao<EmployeeResponseDao> resp=new ResponseObjectDao<>();
		resp.setResponseStatus("SUCCESS");
		Mockito.when(empService.saveEmployee(empRequest))
		.thenReturn(resp);
		
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/employee/employee")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(empRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	private List<EmployeeResponseDao> getEmployeeList() {
		List<EmployeeResponseDao>  empDao=new ArrayList<EmployeeResponseDao>();
		empDao.add(new EmployeeResponseDao("ji12","J R Manav","jrm") );
		return empDao;
	}

}
