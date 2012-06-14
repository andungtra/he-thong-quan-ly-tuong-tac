package org.hcmus.tis.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.springframework.stereotype.Repository;
@Repository
public class SpecificAccountRepository {
	@PersistenceContext
	EntityManager entityManger;
	public Account getByEmail(String email){
		String hql = "SELECT account FROM Account account WHERE account.email =:email";
		TypedQuery<Account> query = entityManger.createQuery(hql, Account.class);
		query.setParameter("email", email);
		return query.getSingleResult();
	}
	public MemberInformation  findByAccountAndProjectAndDeleted(Account account, Project project, boolean deleted){
		String hql = "SELECT o FROM MemberInformation o WHERE o.account.id =:accountId AND o.project.id =:projectId AND o.deleted =:deleted";
		TypedQuery<MemberInformation> query = entityManger.createQuery(hql, MemberInformation.class);
		query.setParameter("accountId", account.getId());
		query.setParameter("projectId", project.getId());
		query.setParameter("deleted", deleted);
		return query.getSingleResult();
	}

}
