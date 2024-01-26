package com.device.management.computers.exception;

public class InvalidOperationException  extends RuntimeException{
	
	/**
	 * Custom exception if the operation is not allowed or invalid.
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOperationException(String msg) {
		super(msg);
	}

}
