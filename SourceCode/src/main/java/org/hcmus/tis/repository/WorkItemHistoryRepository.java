package org.hcmus.tis.repository;

import java.util.List;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WorkItemHistory.class)
public interface WorkItemHistoryRepository extends WorkItemHistoryRepositoryCustom{
	public Page<WorkItemHistory> findByWorkItem(WorkItem workItem, Pageable pageable);
}