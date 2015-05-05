package com.mytest.utils.image;

public class OutOfSizeException extends Exception {
	private static final long serialVersionUID = -8210630094532815078L;

	public OutOfSizeException(String message) {
		super(message);
	}
}