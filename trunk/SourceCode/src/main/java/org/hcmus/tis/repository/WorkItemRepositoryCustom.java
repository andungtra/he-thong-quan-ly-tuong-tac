package org.hcmus.tis.repository;

import java.util.Collection;
import java.util.List;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.springframework.data.domain.Page;

public interface WorkItemRepositoryCustom {

	public abstract List<WorkItem> findByAncestor(WorkItemContainer container, Boolean closed,
			int startDisplay, int displayLength);

	public abstract List<WorkItem> findBy(String filter, SearchConditionsDTO searchCondition, int startDisplay,
			int displayLength);

	public abstract long countByAncestorContainter(WorkItemContainer container, Boolean closed);

	public abstract long countBy(String filter, SearchConditionsDTO searchCondition);
	public  List<WorkItem> findByAncestor(WorkItemContainer container, Boolean closed) ;

	public abstract long countByAncestorContainerAndStatus(WorkItemContainer container,
			WorkItemStatus status);

}
