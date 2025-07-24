import axios from "axios";
import productData from "../data.json"; // fallback dummy data

const BASE_URL = "http://localhost:8080/api";
const USE_BACKEND = false; // set to true when backend is ready

// Fetch products
export const fetchProducts = async () => {
  if (USE_BACKEND) {
    try {
      const response = await axios.get(`${BASE_URL}/products`);
      return response.data;
    } catch (error) {
      console.error("Error fetching products:", error);
      throw error;
    }
  } else {
    // Simulate API delay for JSON
    return new Promise((resolve) => {
      setTimeout(() => resolve(productData), 300);
    });
  }
};

// Add to Cart (requires backend)
export const addToCart = async (productId, userId) => {
  if (!USE_BACKEND) {
    console.log(`[DEV] Simulate addToCart: productId=${productId}, userId=${userId}`);
    return { success: true };
  }
  try {
    const response = await axios.post(`${BASE_URL}/cart`, {
      productId,
      userId,
    });
    return response.data;
  } catch (error) {
    console.error("Error adding to cart:", error);
    throw error;
  }
};

// Buy Now (requires backend)
export const buyNow = async (productId, userId) => {
  if (!USE_BACKEND) {
    console.log(`[DEV] Simulate buyNow: productId=${productId}, userId=${userId}`);
    return { success: true };
  }
  try {
    const response = await axios.post(`${BASE_URL}/purchase`, {
      productId,
      userId,
    });
    return response.data;
  } catch (error) {
    console.error("Error during purchase:", error);
    throw error;
  }
};
