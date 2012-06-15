package org.hcmus.tis.repository;

import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.WorkItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Comment.class)
public interface CommentRepository {
	public Page<Comment> findByWorkItem(WorkItem workItem, Pageable pageable);
}
