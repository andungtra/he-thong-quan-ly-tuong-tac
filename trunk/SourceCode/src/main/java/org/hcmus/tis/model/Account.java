package org.hcmus.tis.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findAccountsByEmailEquals" })
public class Account implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1, max = 50)
	private String firstName;

	@NotNull
	@Size(min = 1, max = 50)
	private String lastName;

	@NotNull
	@Column(unique = true)
	@Size(min = 1)
	private String email;

	@NotNull
	@Size(min = 1, max = 50)
	private String password;
	
	public String getFullName(){
		return firstName + " " + lastName;
	}

	@NotNull
	@Value("false")
	private Boolean isAdmin;

	@Enumerated
	@NotNull
	private AccountStatus status;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(updatable = false)
	private Calendar calendar;

	@PrePersist
	public void prePersit() {
		setCalendar(new Calendar());
	}

	public String toString() {
		return email;
	}

	public static Collection<org.hcmus.tis.model.Account> findAccount(
			String email, int firstIndex, int maxResult) {
		email = "%" + email + "%";
		String queryString = "SELECT account FROM Account AS account WHERE account.email LIKE :email AND account.status <> :status";
		TypedQuery<Account> query = entityManager()
				.createQuery(queryString, Account.class)
				.setFirstResult(firstIndex).setMaxResults(maxResult);
		query.setParameter("email", email);
		query.setParameter("status", AccountStatus.DELETED);
		return query.getResultList();
	}

	public static org.hcmus.tis.model.Account getAccountbyEmail(String email) {
		String queryString = "SELECT account FROM Account AS account WHERE account.email = :email AND account.status <> :status";
		TypedQuery<Account> query = entityManager().createQuery(queryString,
				Account.class);
		query.setParameter("email", email);
		query.setParameter("status", AccountStatus.DELETED);
		return query.getSingleResult();
	}

	public static List<Account> findAccountEntries(int iDisplayStart,
			int iDisplayLength, String sSearch) {
		// TODO Auto-generated method stub

		String hql = "SELECT account FROM Account AS account WHERE account.status <> :status";

		if (sSearch.length() > 0) {
			hql += " AND (LOWER(account.firstName) like LOWER(:sSearch) or LOWER(account.lastName) like LOWER(:sSearch) or LOWER(account.email) like LOWER(:sSearch) or LOWER(account.status) like LOWER(:sSearch))";
		}

		TypedQuery<Account> query = entityManager()
				.createQuery(hql, Account.class).setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength);
		if (sSearch.length() > 0)
			query.setParameter("sSearch", "%" + sSearch + "%");
		query.setParameter("status", AccountStatus.DELETED);
		return query.getResultList();
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public static long countAccountsNotDeleted() {
		// TODO Auto-generated method stub
		EntityManager entityManager = Project.entityManager();
		String jpq = "SELECT COUNT(o) FROM Account AS o WHERE o.status <> :status";
		TypedQuery<Long> q = entityManager.createQuery(jpq, Long.class);
		q.setParameter("status", AccountStatus.DELETED);
		return q.getSingleResult();
	}

	public static long countAccountEntries(String sSearch) {
		// TODO Auto-generated method stub
		String hql = "SELECT COUNT(account) FROM Account AS account WHERE account.status <> :status";

		if (sSearch.length() > 0) {
			hql += " AND (LOWER(account.firstName) like LOWER(:sSearch) or LOWER(account.lastName) like LOWER(:sSearch) or LOWER(account.email) like LOWER(:sSearch) or LOWER(account.status) like LOWER(:sSearch))";
		}

		TypedQuery<Long> query = entityManager().createQuery(hql, Long.class);
		if (sSearch.length() > 0)
			query.setParameter("sSearch", "%" + sSearch + "%");
		query.setParameter("status", AccountStatus.DELETED);
		return query.getSingleResult();
	}
}
