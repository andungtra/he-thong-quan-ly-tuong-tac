package org.hcmus.tis.repository;

import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectStatus;
import org.hcmus.tis.model.StudyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Project.class)
public interface ProjectRepository {
	Page<Project> findByNameLikeAndStudyClassAndStatusNot(String name, StudyClass studyClass, ProjectStatus status, Pageable pageable);
	Page<Project> findByNameLikeAndStatusNot(String name, ProjectStatus status, Pageable pageable);
	@Query("SELECT COUNT(o) FROM Project AS o WHERE (o.status IS NULL or  o.status <> ?1)")
	long countByStatusNot(ProjectStatus status);
	@Query("SELECT COUNT(o) FROM Project AS o WHERE o.studyClass =?1 AND (o.status IS NULL or  o.status <> ?2)")
	long countByStudyClassAndStatusNot(StudyClass studyClass ,ProjectStatus status);
}
