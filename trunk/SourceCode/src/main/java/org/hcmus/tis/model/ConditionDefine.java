package org.hcmus.tis.model;

public class ConditionDefine {
	private String refName;
	private String searchCondition;
	public String getRefName() {
		return refName;
	}
	public ConditionDefine(String refName, String searchCondition) {
		super();
		this.refName = refName;
		this.searchCondition = searchCondition;
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
	

}
