package org.hcmus.tis.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ProjectProcess {

    @NotNull
    @Size(max = 50, min = 1)
    private String name;

    private String description;
    
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @NotNull
    private byte[] processTemplateFile;

    @NotNull
    @Column(unique = true)
    private String uniqueName;

    @OneToMany(mappedBy = "projectProcess", cascade = CascadeType.ALL)
    private List<WorkItemType> workItemTypes;

    @Value("false")
    private boolean isDeleted;
}
