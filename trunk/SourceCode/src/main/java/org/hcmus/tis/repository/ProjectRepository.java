package org.hcmus.tis.repository;

import org.hcmus.tis.model.Project;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Project.class)
public interface ProjectRepository {
}
