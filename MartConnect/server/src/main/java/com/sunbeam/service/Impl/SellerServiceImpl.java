package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.OrderDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.dao.SellerDao;
import com.sunbeam.dto.SellerDto;
import com.sunbeam.entities.Seller;
import com.sunbeam.service.SellerService;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    
    @Autowired
    private SellerDao sellerDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Essential CRUD methods
    @Override
    public SellerDto save(SellerDto sellerDto) { 
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        Seller savedSeller = sellerDao.save(seller);
        return modelMapper.map(savedSeller, SellerDto.class);
    }
    
    @Override
    public Optional<SellerDto> findById(Integer id) { 
        return sellerDao.findById(id).map(seller -> modelMapper.map(seller, SellerDto.class)); 
    }
    
    @Override
    public List<SellerDto> findAll() { 
        return sellerDao.findAll().stream().map(seller -> modelMapper.map(seller, SellerDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public void deleteById(Integer id) { sellerDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return sellerDao.existsById(id); }
    
    @Override
    public long count() { return sellerDao.count(); }
    
    @Override
    public Optional<SellerDto> findByEmail(String email) { 
        return sellerDao.findByEmail(email).map(seller -> modelMapper.map(seller, SellerDto.class)); 
    }
    
    @Override
    public boolean existsByEmail(String email) { return sellerDao.existsByEmail(email); }
    
    @Override
    public List<SellerDto> findByFirstName(String firstName) { 
        return sellerDao.findByFirstNameContainingIgnoreCase(firstName).stream().map(seller -> modelMapper.map(seller, SellerDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<SellerDto> findByLastName(String lastName) { 
        return sellerDao.findByLastNameContainingIgnoreCase(lastName).stream().map(seller -> modelMapper.map(seller, SellerDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<SellerDto> findByPhone(String phone) { 
        // Since findByPhone was removed from SellerDao, return empty list
        return List.of(); 
    }
    
    @Override
    public List<SellerDto> findByShopName(String shopName) { 
        return sellerDao.findByShopNameContainingIgnoreCase(shopName).stream().map(seller -> modelMapper.map(seller, SellerDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<SellerDto> findByShopAddressContaining(String shopAddress) { 
        // Since there's no direct shop address search, return empty list for now
        return List.of(); 
    }
    
    @Override
    public long countByFirstName(String firstName) { 
        return sellerDao.findByFirstNameContainingIgnoreCase(firstName).size(); 
    }
    
    @Override
    public long countByLastName(String lastName) { 
        return sellerDao.findByLastNameContainingIgnoreCase(lastName).size(); 
    }
    
    @Override
    public long countByPhone(String phone) { 
        // Since findByPhone was removed from SellerDao, return 0
        return 0; 
    }
    
    @Override
    public long countByShopName(String shopName) { 
        return sellerDao.findByShopNameContainingIgnoreCase(shopName).size(); 
    }
    
    @Override
    public SellerDto registerSeller(SellerDto sellerDto) {
        // Check if email already exists
        if (sellerDao.existsByEmail(sellerDto.getEmail())) {
            throw new RuntimeException("Seller with email " + sellerDto.getEmail() + " already exists");
        }
        
        // Check if phone already exists
        if (sellerDao.existsByPhone(sellerDto.getPhone())) {
            throw new RuntimeException("Seller with phone " + sellerDto.getPhone() + " already exists");
        }
        
        // Check if shop name already exists
        if (sellerDao.existsByShopName(sellerDto.getShopName())) {
            throw new RuntimeException("Seller with shop name " + sellerDto.getShopName() + " already exists");
        }
        
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        
        // Encode password
        seller.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        
        // Set timestamps
        seller.setCreatedAt(LocalDateTime.now());
        seller.setUpdatedAt(LocalDateTime.now());
        
        Seller savedSeller = sellerDao.save(seller);
        return modelMapper.map(savedSeller, SellerDto.class);
    }
    
    @Override
    public SellerDto authenticate(String email, String password) {
        Optional<Seller> sellerOpt = sellerDao.findByEmail(email);
        if (sellerOpt.isEmpty()) {
            throw new RuntimeException("Seller not found with email: " + email);
        }
        
        Seller seller = sellerOpt.get();
        
        // Verify password
        if (!passwordEncoder.matches(password, seller.getPassword())) {
            throw new RuntimeException("Invalid password for seller: " + email);
        }
        
        return modelMapper.map(seller, SellerDto.class);
    }
} 