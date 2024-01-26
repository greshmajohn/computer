package com.device.management.computers.exception;

public class DataNotFoundException extends RuntimeException {

	/**
	 * Custom exception to handle if the result not found for the given input.
	 * 
	 */
	private static final long serialVersionUID = -3808675015372850064L;

	public DataNotFoundException(String message) {
		super(message);
	}
}
