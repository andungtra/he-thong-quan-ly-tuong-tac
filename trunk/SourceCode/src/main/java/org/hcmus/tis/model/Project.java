package org.hcmus.tis.model;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Project {

    @NotNull
    @Size(max = 50, min = 1)
    private String name;

    private String description;

    @NotNull
    @ManyToOne
    private StudyClass studyClass;
}
