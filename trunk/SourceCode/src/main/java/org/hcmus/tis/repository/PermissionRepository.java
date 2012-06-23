package org.hcmus.tis.repository;

import java.util.List;

import org.hcmus.tis.model.Permission;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Permission.class)
public interface PermissionRepository {
	public List<Permission> findByRefName(String refName);
}
