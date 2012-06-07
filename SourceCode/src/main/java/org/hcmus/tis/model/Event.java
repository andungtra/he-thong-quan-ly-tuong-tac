package org.hcmus.tis.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hcmus.tis.util.JsonDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Event {

	@NotNull
	private String name;

	private String description;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date startDate;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date endDate;

	@JsonIgnore
	@ManyToMany(mappedBy = "events")
	private Collection<Calendar> calendars;

	@PreRemove
	private void preRemove() {
		if (calendars != null) {
			for (Calendar calendar : calendars) {
				calendar.getEvents().remove(this);
			}
		}
	}
}
