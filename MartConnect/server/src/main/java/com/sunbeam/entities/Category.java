package com.sunbeam.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "categories")
@Entity
public class Category extends BaseEntity {

	@Column(length = 50, unique = true)
	private String name;

	@Column(length = 255)
	private String description;

	private boolean status;
}
