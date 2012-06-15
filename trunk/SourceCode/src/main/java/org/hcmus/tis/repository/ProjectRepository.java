package org.hcmus.tis.repository;

import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Project.class)
public interface ProjectRepository {
	Page<Project> findByNameLikeAndStatusNot(String name, ProjectStatus status, Pageable pageable);
	@Query("SELECT COUNT(o) FROM Project AS o WHERE (o.status IS NULL or  o.status <> ?1)")
	long countByStatusNot(ProjectStatus status);
}
