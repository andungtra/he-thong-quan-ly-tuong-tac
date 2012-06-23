package org.hcmus.tis.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class WorkItemHistoryRepositoryImpl implements WorkItemHistoryRepositoryCustom {
	@PersistenceContext
	EntityManager em;
	@Autowired
	private IterationRepository iterationRepository;
	@Override
	public Page<WorkItemHistory> findByProject(Project project,
			Pageable pageable) {
		Collection<Iteration> iterations = iterationRepository.findByAncestor(project);
		String hql = "SELECT o FROM WorkItemHistory o WHERE o.workItemContainer.id = ?1";
		int count = 2;
		for(Iteration iteration : iterations){
			hql = hql  + " OR o.workItemContainer.id = ?" + count;
			count ++;
		}
		hql = hql + " ORDER BY dateLastEdit DESC";
		TypedQuery<WorkItemHistory> query = em.createQuery(hql, WorkItemHistory.class);
		query.setParameter(1, project.getId());
		count = 2;
		for(Iteration iteration : iterations){
			query.setParameter(count, iteration.getId());
			count ++;
		}
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		Page<WorkItemHistory> result = new PageImpl<WorkItemHistory>(query.getResultList(), pageable, pageable.getPageSize());
		return result;
	}

}
