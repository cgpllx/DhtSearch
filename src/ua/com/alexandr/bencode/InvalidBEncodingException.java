package ua.com.alexandr.bencode;

import java.io.IOException;

public class InvalidBEncodingException extends IOException {
	private static final long serialVersionUID = 1L;
	
	public InvalidBEncodingException(String message) {
		super(message);
	}

}
