package com.sunbeam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
	private Integer customerId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private String password;
} 