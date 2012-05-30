package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Permission {

    @NotNull
    private String refName;
    @ManyToMany(mappedBy="permissions")
    private Collection<MemberRole> memberRoles;
}
