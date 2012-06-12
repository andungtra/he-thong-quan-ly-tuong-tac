package org.hcmus.tis.dto.datatables;

public class ProjectDTO {
	public long DT_RowId;
	private String name;
	private String parentContainer;
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentContainer() {
		return parentContainer;
	}
	public void setParentContainer(String parentContainer) {
		this.parentContainer = parentContainer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
