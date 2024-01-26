package com.device.management.computers.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import jakarta.validation.ConstraintViolationException;

/*
 * author : greshma.john
 * custom exception class to handle various exceptions.
 */
@RestControllerAdvice
public class CustomException {
	
	/*
	 * if the given request is not valid, or if some mandatory values are missing MethodArgumentNotValidException
	 * will be thrown
	 */
	
	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ExceptionDto> handleMethodArgumentException(MethodArgumentNotValidException e){
		
		
		List<String> errors = e.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
		ExceptionDto exception=new ExceptionDto("Invalid input",errors,HttpStatus.BAD_REQUEST.name());
		
		return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * If the Database constraint violation occures, for eg: trying to save duplicate data to unique column
	 * 
	 */
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionDto> handleDataIntegrityException(DataIntegrityViolationException e){
		return new ResponseEntity<>(getExceptionDetails("Constraint Violation Exception",e.getRootCause().toString(),HttpStatus.BAD_REQUEST.name()),HttpStatus.BAD_REQUEST);
	}

	/*
	 * handle custome data not found exception.
	 */
	
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ExceptionDto> handleException(DataNotFoundException e){
		
		return new ResponseEntity<>(getExceptionDetails("Data Not Found",e.getLocalizedMessage(),HttpStatus.BAD_REQUEST.name()),HttpStatus.BAD_REQUEST);
	}
	/*
	 * handle custom exception InvalidOperationException
	 */
	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<ExceptionDto> handleException(InvalidOperationException e){
		
		return new ResponseEntity<>(getExceptionDetails("Invalid Operation",e.getLocalizedMessage(),HttpStatus.BAD_REQUEST.name()),HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * Handle all 500 Internal server errors
	 */
	@ExceptionHandler(InternalServerError.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ExceptionDto> handleInternalException(InternalServerError e){
		
		return new ResponseEntity<>(getExceptionDetails("Internal Server Error",e.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR.name()),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	/*
	 * handle all other generic exceptions
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionDto> handleException(Exception e){
		
		return new ResponseEntity<>(getExceptionDetails("General Exceptions",e.getLocalizedMessage(),HttpStatus.BAD_REQUEST.name()),HttpStatus.BAD_REQUEST);
		
	}
	
	private ExceptionDto getExceptionDetails(String title,String exceptionMessage,String exceptionType) {
		List<String> exceptionList=new ArrayList<>();
		exceptionList.add(exceptionMessage);
		return new ExceptionDto(title,exceptionList,exceptionType);
	}
	

}
