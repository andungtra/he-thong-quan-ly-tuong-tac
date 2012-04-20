package org.hcmus.tis.model;

import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findWorkItemsByWorkItemContainer" })
public class WorkItem {

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateCreated;

    private String additionalFields;

    @NotNull
    @ManyToOne
    private Priority priority;

    @NotNull
    @ManyToOne
    private WorkItemContainer workItemContainer;

    @NotNull
    @ManyToOne
    private WorkItemType workItemType;

    @NotNull
    @ManyToOne
    private MemberInformation author;

    @ManyToOne
    private MemberInformation asignee;

    @NotNull
    @ManyToOne
    private WorkItemStatus status;

    @PrePersist
    void prePersit() {
        this.dateCreated = new Date();
    }
}
