package org.hcmus.tis.repository;

import org.hcmus.tis.model.WorkItemStatus;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WorkItemStatus.class)
public interface WorkItemStatusRepository {
}
