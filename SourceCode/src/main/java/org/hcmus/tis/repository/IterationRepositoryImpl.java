package org.hcmus.tis.repository;

import java.util.Collection;
import java.util.HashSet;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.WorkItemContainer;

public class IterationRepositoryImpl implements IterationRepositoryCustom {

	@Override
	public Collection<Iteration> findByAncestor(
			WorkItemContainer parent) {
		Collection<Iteration> iterations = new HashSet<Iteration>();
		for (WorkItemContainer workItemContainer : parent.getChildren()) {
			if (workItemContainer instanceof Iteration
					&& !iterations.contains(workItemContainer)) {
				iterations.add((Iteration) workItemContainer);
				iterations.addAll(findByAncestor(workItemContainer));
			}
		}
		return iterations;
	}

}
