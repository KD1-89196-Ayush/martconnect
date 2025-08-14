package com.sunbeam.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sunbeam.dao.CustomerDao;
import com.sunbeam.dao.SellerDao;
import com.sunbeam.entities.Customer;
import com.sunbeam.entities.Seller;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private SellerDao sellerDao;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("Loading user details for email: " + email);
		
		// Try to find customer first
		var customerOpt = customerDao.findByEmail(email);
		if (customerOpt.isPresent()) {
			Customer customer = customerOpt.get();
			System.out.println("Customer found: " + customer.getEmail() + ", password length: " + customer.getPassword().length());
			System.out.println("Customer password starts with: " + customer.getPassword().substring(0, Math.min(10, customer.getPassword().length())));
			return new User(customer.getEmail(), customer.getPassword(), 
					List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
		}
		
		// Try to find seller
		var sellerOpt = sellerDao.findByEmail(email);
		if (sellerOpt.isPresent()) {
			Seller seller = sellerOpt.get();
			System.out.println("Seller found: " + seller.getEmail() + ", password length: " + seller.getPassword().length());
			System.out.println("Seller password starts with: " + seller.getPassword().substring(0, Math.min(10, seller.getPassword().length())));
			return new User(seller.getEmail(), seller.getPassword(), 
					List.of(new SimpleGrantedAuthority("ROLE_SELLER")));
		}
		
		System.out.println("No user found with email: " + email);
		throw new UsernameNotFoundException("User not found with email: " + email);
	}
} 