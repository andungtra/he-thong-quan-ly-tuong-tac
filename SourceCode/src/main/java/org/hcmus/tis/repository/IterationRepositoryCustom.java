package org.hcmus.tis.repository;

import java.util.Collection;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.WorkItemContainer;

public interface IterationRepositoryCustom {
	public Collection<Iteration> findByAncestor (WorkItemContainer parent);
}
