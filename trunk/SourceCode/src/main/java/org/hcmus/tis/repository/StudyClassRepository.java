package org.hcmus.tis.repository;

import java.util.List;

import org.hcmus.tis.model.StudyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = StudyClass.class)
public interface StudyClassRepository {
	@Query("SELECT COUNT(o) FROM StudyClass o WHERE o.deleted = false")
	public  long countStudyClassesNotDeleted();
	public Page<StudyClass> findByNameLikeAndDeleted(String search, Boolean deleted, Pageable pageable);
	public Page<StudyClass> findByDeleted(Boolean deleted, Pageable pageable);
	public List<StudyClass> findByDeleted(Boolean deleted);
}
