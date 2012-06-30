package org.hcmus.tis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class WorkItemRepositoryImpl implements WorkItemRepositoryCustom {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private IterationRepository iterationRepository;

	private Query createQuery(boolean generateCountQuery, String filter,
			SearchConditionsDTO searchCondition, List<Order> orders) {
		String hql = "";
		if (generateCountQuery) {
			hql = "SELECT COUNT(workItem) FROM WorkItem workItem WHERE 1 = 1";
		} else {
			hql = "SELECT workItem FROM WorkItem workItem WHERE 1 = 1";
		}
		if (searchCondition.getClosed() != null) {
			hql = hql + " AND workItem.status.closed =:closed";
		}
		if (searchCondition.getTitleDescription() != null) {
			hql = hql
					+ " AND (workItem.title LIKE :titleDescription OR workItem.description LIKE :titleDescription)";
		}

		if (searchCondition.getAsignee() != null) {
			hql = hql + " AND workItem.asignee.id =:asignee";
		}
		if (searchCondition.getOwner() != null) {
			hql = hql + " AND workItem.author.id =:author";
		}
		if (searchCondition.getStatus() != null) {
			hql = hql + " AND workItem.status.id =:status";
		}
		if (filter != null && filter.length() > 0) {
			hql = hql
					+ " AND (workItem.title LIKE :filter OR workItem.description LIKE :filter)";
		}
		ArrayList<Iteration> childContainer = null;
		if (searchCondition.getContainer() != null) {
			hql = hql + " AND (workItem.workItemContainer.id =:container";
			childContainer = new ArrayList<Iteration>(
					iterationRepository.findByAncestor(searchCondition
							.getContainer()));
			for (int index = 0; index < childContainer.size(); ++index) {
				hql = hql + " OR workItem.workItemContainer.id =:container_"
						+ String.valueOf(index);
			}
			hql = hql + ")";
		}
		if (generateCountQuery == false && orders != null && orders.size() > 0) {
			hql = hql + " ORDER BY";
			for (Order order : orders) {
				hql = hql + " workItem." + order.getProperty() + " "
						+ order.getDirection();
			}
		}
		Query query = em.createQuery(hql);
		if (searchCondition.getClosed() != null) {
			query = query.setParameter("closed", searchCondition.getClosed());
		}
		if (searchCondition.getTitleDescription() != null) {

			query.setParameter("titleDescription",
					"%" + searchCondition.getTitleDescription() + "%");
		}

		if (searchCondition.getAsignee() != null) {
			query.setParameter("asignee", searchCondition.getAsignee().getId());
		}
		if (searchCondition.getOwner() != null) {
			query.setParameter("author", searchCondition.getOwner().getId());
		}
		if (searchCondition.getStatus() != null) {
			query.setParameter("status", searchCondition.getStatus().getId());
		}
		if (filter != null && filter.length() > 0) {
			query.setParameter("filter", "%" + filter + "%");
		}
		if (searchCondition.getContainer() != null) {
			query.setParameter("container", searchCondition.getContainer()
					.getId());
			for (int index = 0; index < childContainer.size(); ++index) {
				query.setParameter("container_" + String.valueOf(index),
						childContainer.get(index).getId());
			}
		}
		return query;
	}

	@Override
	public long countBy(String filter, SearchConditionsDTO searchCondition) {
		Query query = createQuery(true, filter, searchCondition, null);
		return (Long) query.getSingleResult();
	}

	@Override
	public long countByAncestorContainter(WorkItemContainer container,
			Boolean closed) {
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setContainer(container);
		searchCondition.setClosed(closed);
		Query query = createQuery(true, null, searchCondition, null);
		return (Long) query.getSingleResult();
	}

	@Override
	public List<WorkItem> findBy(String filter,
			SearchConditionsDTO searchCondition, Pageable pageable) {
		List<Order> orders = new ArrayList<Sort.Order>();
		if (pageable.getSort() != null) {
			Iterator<Order> iterator = pageable.getSort().iterator();
			while (iterator.hasNext()) {
				orders.add(iterator.next());
			}
		}
		Query query = createQuery(false, filter, searchCondition, orders);

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}

	@Override
	public List<WorkItem> findByAncestor(WorkItemContainer container,
			Boolean closed, Pageable pageable) {
		SearchConditionsDTO searchCondition = new SearchConditionsDTO();
		searchCondition.setContainer(container);
		searchCondition.setClosed(closed);
		List<Order> orders = new ArrayList<Sort.Order>();
		Iterator<Order> iterator = pageable.getSort().iterator();
		while (iterator.hasNext()) {
			orders.add(iterator.next());
		}
		Query query = createQuery(false, null, searchCondition, orders);
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}

	@Override
	public long countByAncestorContainerAndStatus(WorkItemContainer container,
			WorkItemStatus status) {
		SearchConditionsDTO condition = new SearchConditionsDTO();
		condition.setContainer(container);
		condition.setStatus(status);
		Query query = createQuery(true, null, condition, null);
		return (Long) query.getSingleResult();
	}
}
