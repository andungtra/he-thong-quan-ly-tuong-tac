package org.hcmus.tis.model;

import javax.persistence.Enumerated;
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
    private Project project;

    @NotNull
    @ManyToOne
    private MemberRole memberRole;

    public String toString() {
        return account.getEmail();
    }
}
