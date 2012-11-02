package org.hcmus.tis.repository;

import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.model.WorkItemVersion;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WorkItemVersion.class)
public interface WorkItemVersionRepository {
}
