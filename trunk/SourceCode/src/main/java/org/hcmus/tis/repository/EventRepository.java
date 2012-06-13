package org.hcmus.tis.repository;

import org.hcmus.tis.model.Event;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Event.class)
public interface EventRepository {
}
