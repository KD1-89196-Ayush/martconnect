// services/sellerProductService.js
import axios from "axios";
// import productData from "../data.json"; // Remove static import

const BASE_URL = "http://localhost:8080/api";
const USE_JSON = true; // Toggle to true for local JSON, false for backend

// Fetch products for a specific seller
export const getProductsBySeller = async (sellerId) => {
  if (USE_JSON) {
    let productData = JSON.parse(localStorage.getItem('products'));
    if (!productData) {
      const module = await import("../data.json");
      productData = module.default;
    }
    return productData.filter((product) => product.seller_id === sellerId);
  }
  try {
    const response = await axios.get(`${BASE_URL}/products/seller/${sellerId}`);
    return response.data;
  } catch (error) {
    // fallback to dynamic import
    let productData = JSON.parse(localStorage.getItem('products'));
    if (!productData) {
      const module = await import("../data.json");
      productData = module.default;
    }
    return productData.filter((product) => product.seller_id === sellerId);
  }
};

// Delete product
export const deleteProductById = async (productId) => {
  if (USE_JSON) {
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

// Update product
export const updateProductById = async (productId, productData) => {
  if (USE_JSON) {
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
