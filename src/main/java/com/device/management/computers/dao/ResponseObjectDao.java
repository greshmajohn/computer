package com.device.management.computers.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObjectDao<T> {
	
	private String responseMessage;
	private String responseStatus;
	
	T responseResult;

}
