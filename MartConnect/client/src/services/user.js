import axios from 'axios';
import sellerData from '../sellerdata.json'; // local fallback
import customerData from '../customers.json'; // local fallback for customers
const BASE_URL = 'http://localhost:4000/api';
const USE_JSON = true; // Toggle this to true to use only JSON data

// Login user (unchanged)
export const loginUser = async (email, password, role = 'User') => {
  console.log('LOGIN DEBUG: loginUser called with role:', role);
  if (USE_JSON) {
    if (role === 'Seller') {
      let sellers = JSON.parse(localStorage.getItem('sellerdata'));
      console.log('LOGIN DEBUG: Raw sellerdata from localStorage:', sellers);
      sellers = (Array.isArray(sellers) && sellers.length > 0) ? sellers : sellerData;
      console.log('LOGIN DEBUG: Final sellers array:', sellers);
      console.log('LOGIN DEBUG: Looking for email:', email, 'password:', password);
      const matched = sellers.find(user => user.email === email && user.password === password);
      console.log('LOGIN DEBUG: Matched seller:', matched);
      if (matched) {
        return {
          status: 'success',
          data: {
            firstName: matched.first_name,
            lastName: matched.last_name,
            token: 'dummy-token',
            seller_id: matched.seller_id,
            email: matched.email,
            role: 'Seller',
            shop_name: matched.shop_name,
            shop_address: matched.shop_address
          }
        };
      }
    } else {
      let customers = JSON.parse(localStorage.getItem('customers'));
      customers = (Array.isArray(customers) && customers.length > 0) ? customers : customerData;
      console.log('LOGIN DEBUG: Customers array:', customers);
      const matched = customers.find(user => user.email === email && user.password === password);
      console.log('LOGIN DEBUG: Matched customer:', matched);
      if (matched) {
        return {
          status: 'success',
          data: {
            firstName: matched.first_name,
            lastName: matched.last_name,
            token: 'dummy-token',
            customer_id: matched.customer_id,
            email: matched.email,
            role: 'User',
          }
        };
      }
    }
    return { status: 'error' };
  }

  try {
    const response = await axios.post(`${BASE_URL}/users/login`, { email, password, role });
    return response.data;
  } catch (err) {
    return { status: 'error' };
  }
};

// Register user (flexible for JSON/backend)
export const registerUser = async (firstName, lastName, email, phone, password, role = 'User', address = '', shopName = '', shopAddress = '') => {
  if (USE_JSON) {
    if (role === 'Seller') {
      let sellers = JSON.parse(localStorage.getItem('sellerdata'));
      sellers = (Array.isArray(sellers) && sellers.length > 0) ? sellers : sellerData;
      if (sellers.find(u => u.email === email)) {
        return { status: 'error', error: 'Seller already exists' };
      }
      const newSeller = {
        seller_id: sellers.length ? Math.max(...sellers.map(u => u.seller_id)) + 1 : 1,
        first_name: firstName, // use snake_case
        last_name: lastName,   // use snake_case
        email,
        phone,
        password,
        created_at: new Date().toISOString(),
        shop_name: shopName,
        shop_address: shopAddress // use the provided shop address
      };
      sellers.push(newSeller);
      localStorage.setItem('sellerdata', JSON.stringify(sellers));
      return { status: 'success' };
    } else {
      let customers = JSON.parse(localStorage.getItem('customers'));
      customers = (Array.isArray(customers) && customers.length > 0) ? customers : customerData;
      if (customers.find(u => u.email === email)) {
        return { status: 'error', error: 'Customer already exists' };
      }
      const newCustomer = {
        customer_id: customers.length ? Math.max(...customers.map(u => u.customer_id)) + 1 : 1,
        first_name: firstName, // use snake_case
        last_name: lastName,   // use snake_case
        email,
        phone,
        password,
        address: address || '',
        created_at: new Date().toISOString()
      };
      customers.push(newCustomer);
      localStorage.setItem('customers', JSON.stringify(customers));
      return { status: 'success' };
    }
  }

  try {
    const response = await axios.post(`${BASE_URL}/users/register`, {
      firstName,
      lastName,
      email,
      phone,
      password,
      role,
      address,
      shopName
    });
    return response.data;
  } catch (error) {
    return { status: 'error' };
  }
};
