package org.hcmus.tis.repository;

import org.hcmus.tis.model.WorkItemType;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WorkItemType.class)
public interface WorkItemTypeRepository {
}
