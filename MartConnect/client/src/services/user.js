import axios from 'axios';
import sellerData from '../sellerdata.json'; // local fallback
const BASE_URL = 'http://localhost:4000/api';
const USE_DUMMY_DATA = false; // Toggle this to true to use only JSON data

// Login user
export const loginUser = async (email, password) => {
  if (USE_DUMMY_DATA) {
    const matched = sellerData.find(user => user.email === email && user.password === password);
    if (matched) {
      return {
        status: 'success',
        data: {
          firstName: matched.first_name,
          lastName: matched.last_name,
          token: 'dummy-token'
        }
      };
    } else {
      return { status: 'error' };
    }
  }

  try {
    const response = await axios.post(`${BASE_URL}/users/login`, { email, password });
    return response.data;
  } catch (err) {
    console.error('Login failed, falling back to local JSON');
    if (sellerData) {
      const matched = sellerData.find(user => user.email === email && user.password === password);
      if (matched) {
        return {
          status: 'success',
          data: {
            firstName: matched.first_name,
            lastName: matched.last_name,
            token: 'dummy-token'
          }
        };
      }
    }
    return { status: 'error' };
  }
};

// Register user
export const registerUser = async (firstName, lastName, email, phone, password) => {
  if (USE_DUMMY_DATA) {
    // In real usage, you should update the file manually or show a warning
    console.warn('Register is disabled in dummy mode');
    return { status: 'success' };
  }

  try {
    const response = await axios.post(`${BASE_URL}/users/register`, {
      firstName,
      lastName,
      email,
      phone,
      password
    });
    return response.data;
  } catch (error) {
    console.error('Register error:', error);
    return null;
  }
};
