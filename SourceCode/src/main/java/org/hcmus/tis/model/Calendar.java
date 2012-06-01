package org.hcmus.tis.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Calendar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToMany(cascade={CascadeType.PERSIST})
	private Collection<Event> events;
	@OneToOne(mappedBy="calendar")
	private Account account;
	
	@OneToOne(mappedBy="calendar")
	private Project project;
}
