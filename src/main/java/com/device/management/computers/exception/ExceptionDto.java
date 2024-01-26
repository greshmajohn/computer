package com.device.management.computers.exception;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {
	
	private String exceptionTitle;
	private List<String> exceptionMessage;
	private String status;

}
