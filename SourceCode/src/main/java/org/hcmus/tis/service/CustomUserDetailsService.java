package org.hcmus.tis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hcmus.tis.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("deprecation")
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

	//@Autowired
	//AccountDAO accountDAO;

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException, DataAccessException {
		// TODO Auto-generated method stub
		UserDetails user = null;
		try {
			Account acc = Account.getAccountbyEmail(email); 
			user = new User(acc.getEmail(), acc.getPassword(), true, true, true, true, getAuthorities(acc.getIsAdmin()));
		} catch (Exception e) {
			throw new UsernameNotFoundException("Error in retrieving user");
		}
		return user;
	}
	
	public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities(boolean isAdmin) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
        authList.add(new GrantedAuthorityImpl("ROLE_USER"));
        if (isAdmin) {
            authList.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
        }
        return authList;
    }
}
