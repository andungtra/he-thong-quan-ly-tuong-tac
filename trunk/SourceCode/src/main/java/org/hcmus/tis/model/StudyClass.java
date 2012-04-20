package org.hcmus.tis.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findStudyClassesByNameLike" })
public class StudyClass {

    @NotNull
    @Size(max = 50, min = 1)
    private String name;

    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Project> projects = new HashSet<Project>();
}
