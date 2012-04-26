package org.hcmus.tis.model;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooJpaActiveRecord(finders = { "findMemberInformationsByAccountAndProject" })
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

    @Value("false")
    private Boolean deleted;

    public String toString() {
        return account.getEmail();
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }
        if (this.getProject() != null) {
            this.getProject().getMemberInformations().remove(this);
        }
        if (this.entityManager.contains(this)) {
            this.setDeleted(true);
        } else {
            MemberInformation attached = MemberInformation.findMemberInformation(this.getId());
            attached.setDeleted(true);
        }
    }
}
