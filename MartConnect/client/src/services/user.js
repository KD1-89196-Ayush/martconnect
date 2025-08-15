import axios from 'axios';
import api from './axiosConfig'; // Import configured axios instance
const BASE_URL = 'http://localhost:8087/api';

// Login user (unchanged)
export const loginUser = async (email, password, role = 'User') => {
  try {
    const response = await axios.post(`${BASE_URL}/users/login`, { email, password, role });
    
    // Check if login was successful based on the message field
    if (response.data.message === "Login successful") {
      // Store the JWT token in localStorage
      localStorage.setItem('token', response.data.jwt);
      localStorage.setItem('userData', JSON.stringify(response.data.userData));
      
      return { 
        status: 'success', 
        data: {
          firstName: response.data.userData.firstName,
          lastName: response.data.userData.lastName,
          token: response.data.jwt,
          email: response.data.userData.email,
          role: role,
          // Include role-specific fields
          ...(role === 'Seller' ? {
            seller_id: response.data.userData.sellerId,
            shop_name: response.data.userData.shopName,
            shop_address: response.data.userData.shopAddress
          } : {
            customer_id: response.data.userData.customerId,
            phone: response.data.userData.phone,
            address: response.data.userData.address
          })
        }
      };
    } else {
      return { status: 'error', error: response.data.message };
    }
  } catch (err) {
    return { status: 'error', error: err.response?.data?.message || 'Login failed' };
  }
};

// Register user (flexible for JSON/backend)
export const registerUser = async (firstName, lastName, email, phone, password, role = 'User', address = '', shopName = '', shopAddress = '') => {
  try {
    const response = await axios.post(`${BASE_URL}/users/register`, {
      firstName,
      lastName,
      email,
      phone,
      password,
      role,
      address,
      shopName,
      shopAddress
    });
    if (response.data.success) {
      return { status: 'success' };
    } else {
      return { status: 'error', error: response.data.message };
    }
  } catch (error) {
    return { status: 'error', error: error.response?.data?.message || 'Registration failed' };
  }
};
