package com.mytest.utils.image;

public class NotPermitImageFormatException extends Exception {
	private static final long serialVersionUID = -8210630094532815078L;

	public NotPermitImageFormatException(String message) {
		super(message);
	}
}