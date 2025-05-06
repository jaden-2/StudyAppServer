package com.exceptions;

@SuppressWarnings("serial")
public class InvalidOperationException extends RuntimeException {
	public InvalidOperationException(String message) {
		super(message);
	}
}
