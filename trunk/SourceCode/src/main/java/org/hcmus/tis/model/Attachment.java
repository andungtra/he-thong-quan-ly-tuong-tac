package org.hcmus.tis.model;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Attachment {
	@NotNull
	private String displayFileName;
	private String realFileName;
	@ManyToOne
	private WorkItem workItem;
}
