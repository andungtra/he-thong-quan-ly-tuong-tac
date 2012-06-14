package org.hcmus.tis.repository;

import java.util.List;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = MemberInformation.class)
public interface MemberInformationRepository {
	public List<MemberInformation> findByProjectAndDeleted(Project project, boolean deleted);
	public List<MemberInformation> findByAccountAndDeleted(Account account, boolean deleted);
	@Query("SELECT COUNT(o) FROM MemberInformation o WHERE o.project =?1 AND o.deleted =?2")
	public long countByProjectAndDeleted(Project project, boolean deleted);
	@Query("SELECT COUNT(o) FROM MemberInformation o WHERE o.account =?1 AND o.deleted =?2")
	public long countByAccountAndDeleted(Account account, boolean deleted);
	public MemberInformation findByAccountAndProjectAndDeleted(Account account, Project project, Boolean deleted);
	@Query(value="SELECT o FROM MemberInformation o WHERE o.project =?1 AND (o.account.firstName LIKE ?2 OR o.account.lastName LIKE ?2) AND o.deleted =?3", countQuery="SELECT COUNT(o) FROM MemberInformation o WHERE o.project =?1 AND (o.account.firstName LIKE ?2 OR o.account.lastName LIKE ?2) AND o.deleted =?3")
	public Page<MemberInformation> findByProjectAndAccountLikeAndDeleted(Project project, String accountSearch,boolean deleted, Pageable pageable);
	@Query(value="SELECT o FROM MemberInformation o WHERE o.account =?1 AND (o.project.name LIKE ?2) AND o.deleted =?3", countQuery="SELECT COUNT(o) FROM MemberInformation o WHERE o.account =?1 AND (o.project.name LIKE ?2) AND o.deleted =?3")
	public Page<MemberInformation> findByAccountAndProjectLikeAndDeleted(Account account, String projectSearch, boolean deleted, Pageable pageable);
}
