package org.hcmus.tis.model;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooJpaActiveRecord
public class MemberInformation {

    @ManyToOne
    @NotNull
    private Account account;

    @ManyToOne
    @NotNull
    private Project project;

    @ManyToOne
    @NotNull
    private MemberRole memberRole;

    public String toString() {
        return account.getEmail();
    }
    @Transactional
    public void remove() {
        if (this.entityManager == null){
        	this.entityManager = entityManager();
        }
        if(this.getProject() != null){
        	this.getProject().getMemberInformations().remove(this);
        }
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            MemberInformation attached = MemberInformation.findMemberInformation(this.getId());
            this.entityManager.remove(attached);
        }
    }
}
