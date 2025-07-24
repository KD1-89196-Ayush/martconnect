// services/sellerProductService.js
import axios from "axios";
import productData from "../data.json";

const BASE_URL = "http://localhost:8080/api";
const USE_BACKEND = false;

// Fetch products for a specific seller
export const getProductsBySeller = async (sellerId) => {
  if (!USE_BACKEND) {
    return productData.filter((product) => product.seller_id === sellerId);
  }
  try {
    const response = await axios.get(`${BASE_URL}/products/seller/${sellerId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching seller products:", error);
    return productData.filter((product) => product.seller_id === sellerId); // fallback
  }
};

// Delete product
export const deleteProductById = async (productId) => {
  if (!USE_BACKEND) {
    console.log(`[DEV] Simulate deleteProduct: ${productId}`);
    return { status: "success" };
  }
  try {
    const response = await axios.delete(`${BASE_URL}/products/${productId}`);
    return response.data;
  } catch (error) {
    console.error("Delete failed:", error);
    return null;
  }
};

// Update product - optional, if form is implemented
export const updateProductById = async (productId, productData) => {
  if (!USE_BACKEND) {
    console.log(`[DEV] Simulate updateProduct: ${productId}`, productData);
    return { status: "success" };
  }
  try {
    const response = await axios.put(`${BASE_URL}/products/${productId}`, productData);
    return response.data;
  } catch (error) {
    console.error("Update failed:", error);
    return null;
  }
};
