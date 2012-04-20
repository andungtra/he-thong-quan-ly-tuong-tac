package org.hcmus.tis.service;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { org.hcmus.tis.model.Iteration.class })
public interface IterationService {
	public Project getParentProject(Iteration iteration);
}
