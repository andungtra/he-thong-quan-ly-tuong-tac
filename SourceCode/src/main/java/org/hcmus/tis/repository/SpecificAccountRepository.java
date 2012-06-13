package org.hcmus.tis.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hcmus.tis.model.Account;
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

}
