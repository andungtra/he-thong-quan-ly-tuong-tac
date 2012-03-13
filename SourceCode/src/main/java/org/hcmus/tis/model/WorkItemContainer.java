package org.hcmus.tis.model;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "JOINED")
public abstract class WorkItemContainer {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @ManyToOne
    private org.hcmus.tis.model.WorkItemContainer parentContainer;
}
