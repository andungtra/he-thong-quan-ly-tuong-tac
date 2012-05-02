package org.hcmus.tis.dto;

import java.util.List;

public class DSResponse {
	private int status;
	private List<Object> data;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
}
