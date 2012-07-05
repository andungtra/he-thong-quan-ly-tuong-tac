package org.hcmus.tis.repository;

import java.util.Collection;

import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;

public interface EventRepositoryCustom {
	public Collection<Event> findEventsOfMembers (Project project);
}
