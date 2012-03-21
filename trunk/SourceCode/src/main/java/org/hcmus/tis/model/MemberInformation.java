package org.hcmus.tis.model;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord
public class MemberInformation {

    @NotNull
    @ManyToOne
    private Account account;

    @NotNull
    @ManyToOne
    private MemberRole memberRole;

    @NotNull
    @ManyToOne
    private Project project;
    public String toString()
    {
    	return account.getEmail();
    }
}
