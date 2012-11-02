package org.hcmus.tis.repository;

import org.hcmus.tis.model.Activity;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Activity.class)
public interface ActivityRepository {
	public Page<Activity> findByProject(Project project, Pageable pageable);
	
}
