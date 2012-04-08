package org.hcmus.tis.service;

public class DuplicateException extends Exception {
	private Exception innerException;
	public DuplicateException(){
		
	}
	public DuplicateException(Exception innerException){
		this.setInnerException(innerException);
	}
	public Exception getInnerException() {
		return innerException;
	}
	public void setInnerException(Exception innerException) {
		this.innerException = innerException;
	}
}
