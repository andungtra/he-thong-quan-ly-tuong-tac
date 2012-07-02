package org.hcmus.tis.repository;

import java.util.List;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.WorkItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WorkItem.class)
public interface WorkItemRepository extends WorkItemRepositoryCustom {
	public List<WorkItem> findByAsignee_AccountAndAsignee_DeletedAndStatus_Closed(Account account,boolean deleted, boolean closed, Pageable pageable);
	
}