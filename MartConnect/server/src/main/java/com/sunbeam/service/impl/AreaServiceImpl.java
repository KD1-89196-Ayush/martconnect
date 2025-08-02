package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.AreaDao;
import com.sunbeam.dao.CustomerDao;
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
    
    // Core: Update area
    @Override
    public Area updateArea(Integer areaId, Area area) {
        Optional<Area> existingArea = areaDao.findById(areaId);
        if (existingArea.isEmpty()) {
            throw new RuntimeException("Area not found with ID: " + areaId);
        }
        
        Area existing = existingArea.get();
        if (area.getAreaName() != null) existing.setAreaName(area.getAreaName());
        if (area.getCity() != null) existing.setCity(area.getCity());
        if (area.getState() != null) existing.setState(area.getState());
        if (area.getPincode() != null) existing.setPincode(area.getPincode());
        
        return areaDao.save(existing);
    }
    
    // Core: Get area statistics
    @Override
    public Object[] getAreaStatistics(Integer areaId) {
        Optional<Area> areaOpt = areaDao.findById(areaId);
        if (areaOpt.isEmpty()) {
            return null;
        }
        
        Area area = areaOpt.get();
        long customerCount = areaDao.countByCustomer_CustomerId(area.getCustomer().getCustomerId());
        
        return new Object[]{area, customerCount};
    }
    
    // Essential CRUD methods
    @Override
    public Area save(Area area) { return areaDao.save(area); }
    
    @Override
    public Optional<Area> findById(Integer id) { return areaDao.findById(id); }
    
    @Override
    public List<Area> findAll() { return areaDao.findAll(); }
    
    @Override
    public void deleteById(Integer id) { areaDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return areaDao.existsById(id); }
    
    @Override
    public long count() { return areaDao.count(); }
    
    // Essential search methods
    @Override
    public List<Area> findByPincode(String pincode) { return areaDao.findByPincode(pincode); }
    
    // Essential interface methods
    @Override
    public List<Area> findByCustomer(Integer customerId) { return areaDao.findByCustomer_CustomerId(customerId); }
    
    @Override
    public List<Area> findByAreaName(String areaName) { return areaDao.findByAreaNameContainingIgnoreCase(areaName); }
    
    @Override
    public List<Area> findByCity(String city) { return areaDao.findByCityContainingIgnoreCase(city); }
    
    @Override
    public List<Area> findByState(String state) { return areaDao.findByStateContainingIgnoreCase(state); }
    
    @Override
    public List<Area> findByCustomerAndPincode(Integer customerId, String pincode) { return areaDao.findByCustomer_CustomerIdAndPincode(customerId, pincode); }
    
    @Override
    public List<Area> findByCustomerAndCity(Integer customerId, String city) { return areaDao.findByCustomer_CustomerIdAndCityContainingIgnoreCase(customerId, city); }
    
    @Override
    public List<Area> findByCustomerAndState(Integer customerId, String state) { return areaDao.findByCustomer_CustomerIdAndStateContainingIgnoreCase(customerId, state); }
    
    @Override
    public List<Area> findByPincodeAndCity(String pincode, String city) { return areaDao.findByPincodeAndCityContainingIgnoreCase(pincode, city); }
    
    @Override
    public List<Area> findByPincodeAndState(String pincode, String state) { return areaDao.findByPincodeAndStateContainingIgnoreCase(pincode, state); }
    
    @Override
    public List<Area> findByCityAndState(String city, String state) { return areaDao.findByCityContainingIgnoreCaseAndStateContainingIgnoreCase(city, state); }
    
    @Override
    public List<Area> findByAreaNameOrCityContaining(String searchTerm) { return areaDao.findByAreaNameOrCityContaining(searchTerm); }
    
    @Override
    public List<Area> findByAreaNameOrCityOrStateContaining(String searchTerm) { return areaDao.findByAreaNameOrCityOrStateContaining(searchTerm); }
    
    @Override
    public boolean existsByPincode(String pincode) { return areaDao.existsByPincode(pincode); }
    
    @Override
    public boolean existsByCustomerAndPincode(Integer customerId, String pincode) { return areaDao.existsByCustomer_CustomerIdAndPincode(customerId, pincode); }
    
    @Override
    public long countByCustomer(Integer customerId) { return areaDao.countByCustomer_CustomerId(customerId); }
    
    @Override
    public long countByPincode(String pincode) { return areaDao.countByPincode(pincode); }
    
    @Override
    public long countByCity(String city) { return areaDao.countByCityContainingIgnoreCase(city); }
    
    @Override
    public long countByState(String state) { return areaDao.countByStateContainingIgnoreCase(state); }
    
    @Override
    public List<Area> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) { return areaDao.findByCreatedAtBetween(startDate, endDate); }
    
    @Override
    public List<Area> findByCustomerEmail(String email) { return areaDao.findByCustomerEmail(email); }
    
    @Override
    public List<Area> findByCustomerName(String name) { return areaDao.findByCustomerName(name); }
    
    @Override
    public List<String> getDistinctCities() { return areaDao.findDistinctCities(); }
    
    @Override
    public List<String> getDistinctStates() { return areaDao.findDistinctStates(); }
    
    @Override
    public List<String> getDistinctPincodes() { return areaDao.findDistinctPincodes(); }
    
    @Override
    public List<Object[]> getAreasByCityWithCustomerCount() { return areaDao.findAreasByCityWithCustomerCount(); }
    
    @Override
    public List<Object[]> getAreasByStateWithCustomerCount() { return areaDao.findAreasByStateWithCustomerCount(); }
    
    @Override
    public List<Object[]> getAreasByPincodeWithCustomerCount() { return areaDao.findAreasByPincodeWithCustomerCount(); }
    
    @Override
    public List<Object[]> getAreasWithCustomerDetails() { return areaDao.findAreasWithCustomerDetails(); }
    
    @Override
    public List<Object[]> getAreasByCustomerWithFullAddress(Integer customerId) { return areaDao.findAreasByCustomerWithFullAddress(customerId); }
    
    @Override
    public List<Object[]> getAreasByCityWithFullAddress(String city) { return areaDao.findAreasByCityWithFullAddress(city); }
    
    @Override
    public List<Object[]> getAreasByStateWithFullAddress(String state) { return areaDao.findAreasByStateWithFullAddress(state); }
    
    @Override
    public Area addAreaForCustomer(Integer customerId, Area area) {
        Optional<Customer> customerOpt = customerDao.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        area.setCustomer(customerOpt.get());
        area.setCreatedAt(LocalDateTime.now());
        return areaDao.save(area);
    }
    
    @Override
    public void removeArea(Integer areaId) {
        if (!areaDao.existsById(areaId)) {
            throw new RuntimeException("Area not found with ID: " + areaId);
        }
        areaDao.deleteById(areaId);
    }
    
    @Override
    public Optional<Area> getCustomerPrimaryArea(Integer customerId) {
        // Since Area entity doesn't have isPrimary field, return first area
        List<Area> customerAreas = areaDao.findByCustomer_CustomerId(customerId);
        return customerAreas.isEmpty() ? Optional.empty() : Optional.of(customerAreas.get(0));
    }
    
    @Override
    public Area setCustomerPrimaryArea(Integer customerId, Integer areaId) {
        Optional<Area> areaOpt = areaDao.findById(areaId);
        if (areaOpt.isEmpty()) {
            throw new RuntimeException("Area not found with ID: " + areaId);
        }
        
        Area area = areaOpt.get();
        if (!area.getCustomer().getCustomerId().equals(customerId)) {
            throw new RuntimeException("Area does not belong to customer");
        }
        
        // Since Area entity doesn't have isPrimary field, just return the area
        return area;
    }
    
    @Override
    public Object[] getLocationAnalytics() { return List.of().toArray(); }
    
    @Override
    public List<Object[]> getPopularCities(int limit) { return List.of(); }
    
    @Override
    public List<Object[]> getPopularStates(int limit) { return List.of(); }
    
    @Override
    public List<Object[]> getPopularPincodes(int limit) { return List.of(); }
    
    @Override
    public boolean validatePincodeFormat(String pincode) {
        return pincode != null && pincode.matches("^[1-9][0-9]{5}$");
    }
    
    @Override
    public List<Area> getDeliveryAreas(String city, String state) { return areaDao.findByCityContainingIgnoreCaseAndStateContainingIgnoreCase(city, state); }
} 