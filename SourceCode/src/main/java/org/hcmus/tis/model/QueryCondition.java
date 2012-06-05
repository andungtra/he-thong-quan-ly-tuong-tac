package org.hcmus.tis.model;

public class QueryCondition {
	private String refName;
	public String getRefName() {
		return refName;
	}
	public void setRefName(String refName) {
		this.refName = refName;
	}
	public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	public String getValueValue() {
		return valueValue;
	}
	public void setValueValue(String valueValue) {
		this.valueValue = valueValue;
	}
	private String searchCondition;
	private String valueValue;

}
