package org.hcmus.tis.model;

import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Comment {

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date commentDate;

    @ManyToOne
    @NotNull
    private WorkItem workItem;

    @ManyToOne
    private MemberInformation projectMember;
    @PrePersist
    public void prePersit(){
    	this.commentDate = new Date();
    }
}
