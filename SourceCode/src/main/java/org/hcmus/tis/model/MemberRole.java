package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class MemberRole {

    @NotNull
    private String name;
    @NotNull
    private String refName;
	@ManyToMany(cascade={CascadeType.PERSIST})
    private Collection<Permission> permissions;
}
