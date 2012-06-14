package org.hcmus.tis.repository;

import org.hcmus.tis.model.Attachment;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Attachment.class)
public interface AttachmentRepository {
}
