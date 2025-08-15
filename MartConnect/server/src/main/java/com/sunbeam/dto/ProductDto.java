package com.sunbeam.dto;

import java.math.BigDecimal;

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
public class ProductDto {
	private Integer productId;
	private String name;
	private String description;
	private BigDecimal price;
	private String unit;
	private Integer stock;
	private String imageUrl;
	private Integer categoryId;
	private String category; // Add category name
	private Integer sellerId;
} 