package org.hcmus.tis.repository;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;
import org.springframework.test.context.ContextConfiguration;

public class EventRepositoryImpl implements EventRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	public Collection<Event> findEventsOfMembers(Project project) {
		String jpq = "SELECT DISTINCT event FROM Project project JOIN project.memberInformations memberInformation JOIN memberInformation.account.calendar calendar JOIN calendar.events event WHERE project.id = :projectId AND memberInformation.deleted = false  AND event NOT MEMBER OF project.calendar.events";
		TypedQuery<Event> q = entityManager.createQuery(jpq, Event.class);
		q.setParameter("projectId", project.getId());

		return q.getResultList();
	}


}
