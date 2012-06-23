package org.hcmus.tis.repository;

import org.hcmus.tis.model.MemberRole;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = MemberRole.class)
public interface MemberRoleRepository {
}
