package org.hcmus.tis.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = WorkItem.class)
public class WorkItemDataOnDemand {
	@Autowired
	private ProjectDataOnDemand projectDataOnDemand;
    public void setWorkItemContainer(WorkItem obj, int index) {
        WorkItemContainer workItemContainer = projectDataOnDemand.getRandomProject();
        obj.setWorkItemContainer(workItemContainer);
    }
}
