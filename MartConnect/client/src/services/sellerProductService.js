// services/sellerProductService.js
import api from "./axiosConfig";

// Fetch products for a specific seller
export const getProductsBySeller = async (sellerId) => {
  try {
    const response = await api.get(`/products/seller/${sellerId}`);
    // Handle direct array response (new format)
    if (Array.isArray(response.data)) {
      return response.data;
    } else if (response.data.success) {
      return response.data.data.content || response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

// Delete product
export const deleteProductById = async (productId) => {
  try {
    const response = await api.delete(`/products/${productId}`);
    // Handle direct response (new format)
    if (response.status === 200) {
      return { status: "success" };
    } else if (response.data.success) {
      return { status: "success" };
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

// Update product
export const updateProductById = async (productId, productData) => {
  try {
    const response = await api.put(`/products/${productId}`, productData);
    // Handle direct object response (new format)
    if (response.data && response.data.productId) {
      return { status: "success", data: response.data };
    } else if (response.data.success) {
      return { status: "success", data: response.data.data };
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};
