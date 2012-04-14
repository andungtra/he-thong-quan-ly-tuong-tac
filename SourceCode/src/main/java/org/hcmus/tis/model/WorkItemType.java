package org.hcmus.tis.model;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class WorkItemType {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @ManyToOne
    private ProjectProcess projectProcess;

    private String additionalFieldsDefine;
}
