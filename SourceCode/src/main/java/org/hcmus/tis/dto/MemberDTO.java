package org.hcmus.tis.dto;

public class MemberDTO {
	
    private String name;    
    private String memberRole;
    public long DT_RowId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemberRole() {
		return memberRole;
	}
	public void setMemberRole(String memberRole) {
		this.memberRole = memberRole;
	}

}
