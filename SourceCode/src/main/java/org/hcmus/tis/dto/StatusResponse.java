package org.hcmus.tis.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO containing the status of an action and a {@link List} of messages. 
 * This is mainly used as a DTO for the presentation layer
 */
public class StatusResponse {

	private Boolean success;
	private List<String> message;

	public StatusResponse() {
		this.setMessage(new ArrayList<String>());
	}

	public StatusResponse(Boolean success) {
		super();
		this.setSuccess(success);
		this.setMessage(new ArrayList<String>());
	}

	public StatusResponse(Boolean success, String message) {
		super();
		this.setSuccess(success);
		this.setMessage(new ArrayList<String>());
		this.getMessage().add(message);
	}

	public StatusResponse(Boolean success, List<String> message) {
		super();
		this.setSuccess(success);
		this.setMessage(message);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String mess: getMessage()) {
			sb.append(mess +", ");
		}

		return "StatusResponse [success=" + getSuccess() + ", message=" + sb.toString()
				+ "]";
	}

	private Boolean getSuccess() {
		return success;
	}

	private void setSuccess(Boolean success) {
		this.success = success;
	}

	private List<String> getMessage() {
		return message;
	}

	private void setMessage(List<String> message) {
		this.message = message;
	}
}
