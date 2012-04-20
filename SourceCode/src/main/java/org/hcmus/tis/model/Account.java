package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Enumerated;
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

    @NotNull
    @Value("false")
    private Boolean isAdmin;

    @Value("false")
    private Boolean isEnable;

    @Enumerated
    @NotNull
    private AccountStatus status;

    public String toString() {
        return email;
    }

    public static Collection<org.hcmus.tis.model.Account> findAccount(String email, int firstIndex, int maxResult) {
        email = "%" + email + "%";
        String queryString = "SELECT account FROM Account AS account WHERE account.email LIKE :email";
        TypedQuery<Account> query = entityManager().createQuery(queryString, Account.class);
        query.setParameter("email", email);
        query.setFirstResult(firstIndex);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    public static org.hcmus.tis.model.Account getAccountbyEmail(String email) {
        String queryString = "SELECT account FROM Account AS account WHERE account.email = :email";
        TypedQuery<Account> query = entityManager().createQuery(queryString, Account.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}