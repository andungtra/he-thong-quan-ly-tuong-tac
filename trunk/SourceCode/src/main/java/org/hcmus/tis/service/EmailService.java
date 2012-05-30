package org.hcmus.tis.service;

import java.util.Map;

public interface EmailService {
	public class SendMailException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Exception rootCause;

		public Exception getRootCause() {
			return rootCause;
		}

		public void setRootCause(Exception rootCause) {
			this.rootCause = rootCause;
		}
		public SendMailException(){
			
		}
		public SendMailException(Exception rootCause){
			this.rootCause = rootCause;
		}
	}
	public void sendEmail(String destEmail, String templateFile, Map<?, ?> model, String subject) throws SendMailException;
}
