package org.hcmus.tis.dto;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;

public class SearchConditionsDTO {
	private String titleDescription;
	private WorkItemStatus status;
	private MemberInformation owner;
	private MemberInformation asignee;
	private Boolean closed;
	public Boolean getClosed() {
		return closed;
	}
	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
	private WorkItemContainer container;
	public String getTitleDescription() {
		return titleDescription;
	}
	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}
	public WorkItemStatus getStatus() {
		return status;
	}
	public void setStatus(WorkItemStatus status) {
		this.status = status;
	}
	public MemberInformation getOwner() {
		return owner;
	}
	public void setOwner(MemberInformation owner) {
		this.owner = owner;
	}
	public MemberInformation getAsignee() {
		return asignee;
	}
	public void setAsignee(MemberInformation asignee) {
		this.asignee = asignee;
	}
	public WorkItemContainer getContainer() {
		return container;
	}
	public void setContainer(WorkItemContainer container) {
		this.container = container;
	}	

}
