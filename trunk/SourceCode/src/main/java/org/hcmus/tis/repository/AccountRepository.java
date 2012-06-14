package org.hcmus.tis.repository;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Account.class)
public interface AccountRepository {
	@Query("SELECT COUNT(account) FROM Account account WHERE account.status <> org.hcmus.tis.model.AccountStatus.DELETED")
	public long countNotDeleted();
	public Account getByEmail(String email);
	@Query("SELECT COUNT(account) FROM Account account WHERE account.email = ?1")
	public long countByEmail(String email);
	@Query(countQuery = "SELECT COUNT(account) FROM Account account WHERE (account.email LIKE ?1 OR account.firstName LIKE ?1 OR account.lastName LIKE ?1) AND account.status <> org.hcmus.tis.model.AccountStatus.DELETED", value="SELECT account FROM Account account WHERE (account.email LIKE ?1 OR account.firstName LIKE ?1 OR account.lastName LIKE ?1) AND account.status <> org.hcmus.tis.model.AccountStatus.DELETED")
	public Page<Account> find(String search, Pageable pageable);
	public Page<Account> findByEmailLikeAndStatus(String email, AccountStatus status, Pageable pageable);
}
