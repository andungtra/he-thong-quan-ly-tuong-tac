package org.hcmus.tis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hcmus.tis.util.Parameter;


public class Page<T> {
	private int number;
	private long totalPages;
	private long totalElements;
	private List<T> elements;
	private List<Parameter> anotherParameters;
	public Page()
	{
		elements = new ArrayList<T>();
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public List<T> getElements() {
		return elements;
	}
	public void setElements(List<T> elements) {
		this.elements = elements;
	}
	private List<Parameter> getAnotherParameters() {
		return anotherParameters;
	}
	private void setAnotherParameters(List<Parameter> anotherParameters) {
		this.anotherParameters = anotherParameters;
	}

}
