package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Calendar {
	@ManyToMany(cascade={CascadeType.PERSIST})
	private Collection<Event> events;
}
