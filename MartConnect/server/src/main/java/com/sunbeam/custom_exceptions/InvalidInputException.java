package com.sunbeam.custom_exceptions;

public class InvalidInputException extends RuntimeException {
	public InvalidInputException(String message) {
		super(message);
	}
} 