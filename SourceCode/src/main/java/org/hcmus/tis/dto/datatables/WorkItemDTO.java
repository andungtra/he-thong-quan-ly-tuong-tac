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
	private static String [][] mappingTable = {{"lName","title"},{"sStatus","status.name"}, {"sType","workItemType.name"},{"priority","priority.name"},{"iteration","workItemContainer.name"}};
	public static String mapToEntityProper(String dtoProper){
		for(int index = 0; index < mappingTable.length; ++index){
			if(mappingTable[index][0].compareTo(dtoProper) == 0){
				return mappingTable[index][1];
			}
		}
		return null;
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
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
}
