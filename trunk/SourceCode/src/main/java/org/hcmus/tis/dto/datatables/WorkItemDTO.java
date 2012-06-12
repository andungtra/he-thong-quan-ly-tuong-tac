package org.hcmus.tis.dto.datatables;

public class WorkItemDTO {
	public long DT_RowId;	
	private String lName;
	private String sStatus;
	private String sType;
	private String priority;
	private String iteration; 
	public String getIteration() {
		return iteration;
	}
	public void setIteration(String iteration) {
		this.iteration = iteration;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getsStatus() {
		return sStatus;
	}
	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}
	public String getsType() {
		return sType;
	}
	public void setsType(String sType) {
		this.sType = sType;
	}
	public String getPriority() {
		return priority;
	}
	private String sIteration;
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getsIteration() {
		return sIteration;
	}
	public void setsIteration(String sIteration) {
		this.sIteration = sIteration;
	}
	
}
