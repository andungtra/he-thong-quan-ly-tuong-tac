package org.hcmus.tis.repository;

import org.hcmus.tis.model.Priority;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Priority.class)
public interface PriorityRepository {
}
