package org.hcmus.tis.repository;

import java.util.List;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.springframework.data.domain.Pageable;

public interface WorkItemRepositoryCustom {

	public abstract List<WorkItem> findByAncestor(WorkItemContainer container, Boolean closed,Pageable pageable);

	public abstract long countByAncestorContainter(WorkItemContainer container, Boolean closed);
	public abstract long countBy(String filter, SearchConditionsDTO searchCondition);
	public abstract List<WorkItem> findBy(String filter, SearchConditionsDTO searchCondition, Pageable pageable);
	public abstract long countByAncestorContainerAndStatus(WorkItemContainer container,
			WorkItemStatus status);

}
