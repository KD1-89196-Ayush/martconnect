package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.AreaDao;
import com.sunbeam.dao.CustomerDao;
import com.sunbeam.dto.AreaDto;
import com.sunbeam.entities.Area;
import com.sunbeam.entities.Customer;
import com.sunbeam.service.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    
    @Autowired
    private AreaDao areaDao;
    
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Core: Add area for customer
    @Override
    public AreaDto addAreaForCustomer(Integer customerId, AreaDto areaDto) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerDao.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        Customer customer = customerOpt.get();
        Area area = modelMapper.map(areaDto, Area.class);
        
        // Set the customer
        area.setCustomer(customer);
        
        // Set timestamp
        area.setCreatedAt(LocalDateTime.now());
        
        Area savedArea = areaDao.save(area);
        return modelMapper.map(savedArea, AreaDto.class);
    }
    
    // Essential CRUD methods
    @Override
    public AreaDto save(AreaDto areaDto) { 
        Area area = modelMapper.map(areaDto, Area.class);
        Area savedArea = areaDao.save(area);
        return modelMapper.map(savedArea, AreaDto.class);
    }
    
    @Override
    public Optional<AreaDto> findById(Integer id) { 
        return areaDao.findById(id).map(area -> modelMapper.map(area, AreaDto.class)); 
    }
    
    @Override
    public List<AreaDto> findAll() { 
        return areaDao.findAll().stream().map(area -> modelMapper.map(area, AreaDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public void deleteById(Integer id) { areaDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return areaDao.existsById(id); }
    
    @Override
    public long count() { return areaDao.count(); }
    
    @Override
    public List<AreaDto> findByCustomer(Integer customerId) {
        return areaDao.findByCustomer_CustomerId(customerId).stream()
                .map(area -> modelMapper.map(area, AreaDto.class))
                .collect(Collectors.toList());
    }
   
} 