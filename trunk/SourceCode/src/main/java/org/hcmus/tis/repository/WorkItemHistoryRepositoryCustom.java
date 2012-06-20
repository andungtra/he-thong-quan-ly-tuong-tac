package org.hcmus.tis.repository;

import java.util.Collection;
import java.util.List;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemHistory;
import org.hcmus.tis.model.WorkItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkItemHistoryRepositoryCustom {
	public Page<WorkItemHistory> findByProject(Project project, Pageable pageable);
}
