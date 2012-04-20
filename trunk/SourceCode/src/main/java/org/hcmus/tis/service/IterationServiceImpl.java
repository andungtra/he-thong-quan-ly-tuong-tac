package org.hcmus.tis.service;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;


public class IterationServiceImpl implements IterationService {
	public Project getParentProject(Iteration iteration){
		WorkItemContainer topIteration = iteration;
		while(topIteration.getParentContainer().getClass() != Project.class){
			topIteration = topIteration.getParentContainer();
		}
		return (Project)topIteration.getParentContainer();
	}
}
