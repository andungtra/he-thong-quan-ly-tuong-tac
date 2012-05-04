package org.hcmus.tis.model;

import java.util.List;

public class FieldDefine {
	private String refName;
	private boolean required;
	private BuildInType type;
	private String name;
	private String defaultValue;
	private List<String> choices;
	public String getRefName() {
		return refName;
	}
	public void setRefName(String refName) {
		this.refName = refName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public List<String> getChoices() {
		return choices;
	}
	public void setChoices(List<String> choices) {
		this.choices = choices;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public BuildInType getType() {
		return type;
	}
	public void setType(BuildInType type) {
		this.type = type;
	}
}
