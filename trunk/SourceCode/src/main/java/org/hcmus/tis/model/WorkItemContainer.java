package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @OneToMany(mappedBy="parentContainer")
    private Collection<WorkItemContainer> children;
    @OneToMany(mappedBy="workItemContainer")
    private Collection<WorkItem> workItems;
    public abstract Project getParentProjectOrMyself();
	public Collection<WorkItemContainer> getChildren() {
		return children;
	}
}