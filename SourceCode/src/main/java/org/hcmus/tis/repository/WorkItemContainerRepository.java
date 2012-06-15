package org.hcmus.tis.repository;

import org.hcmus.tis.model.WorkItemContainer;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WorkItemContainer.class)
public interface WorkItemContainerRepository {
}
