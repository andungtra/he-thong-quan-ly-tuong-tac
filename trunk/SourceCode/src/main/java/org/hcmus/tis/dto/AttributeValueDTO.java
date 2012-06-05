package org.hcmus.tis.dto;

public class AttributeValueDTO {
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public AttributeValueDTO(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
