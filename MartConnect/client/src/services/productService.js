import api from "./axiosConfig"; // Use configured axios instance

const BASE_URL = "http://localhost:8087/api";

// Fetch products
export const fetchProducts = async () => {
  try {
    // First, let's check if the backend is accessible
    try {
      await api.get(`/products`, { timeout: 5000 });
    } catch (healthError) {
      if (healthError.code === 'ECONNREFUSED') {
        throw new Error("Backend server is not running. Please start the server on port 8087.");
      } else if (healthError.code === 'ENOTFOUND') {
        throw new Error("Cannot connect to backend server. Please check if the server is running.");
      } else {
        throw new Error(`Backend connection failed: ${healthError.message}`);
      }
    }
    
    const response = await api.get(`/products`);
    
    // Handle direct array response (new format)
    if (Array.isArray(response.data)) {
      return response.data;
    } else if (response.data.success) {
      // Handle both paginated and non-paginated responses
      const data = response.data.data;
      if (data && data.content) {
        // Paginated response
        return data.content;
      } else if (Array.isArray(data)) {
        // Direct array response
        return data;
      } else {
        return [];
      }
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

// Add to Cart (requires backend)
export const addToCart = async (productId, userId) => {
  try {
    const response = await api.post(`/cart`, {
      productId,
      customerId: userId,
      quantity: 1
    });
    if (response.data.success) {
      return { success: true, data: response.data.data };
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

// Buy Now (requires backend)
export const buyNow = async (productId, userId) => {
  try {
    const response = await api.post(`/orders`, {
      productId,
      customerId: userId,
      quantity: 1
    });
    if (response.data.success) {
      return { success: true, data: response.data.data };
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

export const getAllProducts = fetchProducts;
