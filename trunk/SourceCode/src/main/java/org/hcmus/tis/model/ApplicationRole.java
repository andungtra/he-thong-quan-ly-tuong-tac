package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.ManyToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class ApplicationRole {
	private String name;
	@ManyToMany
	private Collection<Permission> permissions;
}
