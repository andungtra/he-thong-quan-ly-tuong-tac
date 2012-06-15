package org.hcmus.tis.repository;

import java.util.List;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.WorkItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WorkItem.class)
public interface WorkItemRepository extends WorkItemRepositoryCustom {
	@Query("SELECT o FROM WorkItem o  WHERE o.asignee.account =?1 AND o.status.closed =?2")
	public List<WorkItem> findByAsigneeAndFinalStatus(Account account, boolean deleted);
	
}