package org.hcmus.tis.repository;

import org.hcmus.tis.model.Iteration;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Iteration.class)
public interface IterationRepository extends IterationRepositoryCustom {
}
