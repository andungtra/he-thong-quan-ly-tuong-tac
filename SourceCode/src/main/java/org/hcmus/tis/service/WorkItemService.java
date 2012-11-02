package org.hcmus.tis.service;

import org.hcmus.tis.model.WorkItem;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { org.hcmus.tis.model.WorkItem.class })
public interface WorkItemService {
	public void save(WorkItem workItem);
}
