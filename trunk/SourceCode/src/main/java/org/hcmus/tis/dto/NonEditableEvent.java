package org.hcmus.tis.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hcmus.tis.util.JsonDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

public class NonEditableEvent {
	private Long id;
	private Integer version = 0;
	private boolean canEdit = false;
	private String eventWindowStyle = "testStyle";

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getEventWindowStyle() {
		return eventWindowStyle;
	}

	public void setEventWindowStyle(String eventWindowStyle) {
		this.eventWindowStyle = eventWindowStyle;
	}

	private String name;

    private String description;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using=JsonDateSerializer.class)
    private Date startDate;

    @JsonSerialize(using=JsonDateSerializer.class)
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
}
