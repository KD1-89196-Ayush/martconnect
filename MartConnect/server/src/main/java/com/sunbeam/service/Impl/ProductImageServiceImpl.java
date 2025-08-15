package com.sunbeam.service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.ProductImageDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.entities.ProductImage;
import com.sunbeam.entities.Product;
import com.sunbeam.service.ProductImageService;

/**
 * Implementation of ProductImageService
 */
@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

	@Override
	public ProductImage save(ProductImage entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ProductImage> findById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<ProductImage> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existsById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

} 