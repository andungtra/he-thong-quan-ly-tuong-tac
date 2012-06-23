package org.hcmus.tis.repository;

import org.hcmus.tis.model.ApplicationRole;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = ApplicationRole.class)
public interface ApplicationRoleRepository {
}
