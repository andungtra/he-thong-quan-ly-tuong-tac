package org.hcmus.tis.dto;

import java.util.ArrayList;
import java.util.List;

public class DtReply {
	private int iTotalRecords;
	private int iTotalDisplayRecords;
	private String sEcho;
	private String sColumns;
	private List<Object> aaData = new ArrayList<Object>();
	
	public DtReply(){
		iTotalDisplayRecords = 10;
	}
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
		if(this.iTotalDisplayRecords> iTotalRecords)
			this.iTotalDisplayRecords = iTotalRecords;
	}
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	public String getsColumns() {
		return sColumns;
	}
	public void setsColumns(String sColumns) {
		this.sColumns = sColumns;
	}
	public List<Object> getAaData() {
		return aaData;
	}
	public void setAaData(List<Object> aaData) {
		this.aaData = aaData;
	}
}
