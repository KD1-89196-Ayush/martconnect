import api from './axiosConfig';

export const updateProduct = async (productId, updatedProduct) => {
  try {
    const response = await api.put(`/products/${productId}`, updatedProduct);
    
    // Handle direct object response (new format)
    if (response.data && response.data.productId) {
      return { success: true, data: response.data };
    } else if (response.data.success) {
      return { success: true, data: response.data.data };
    } else {
      throw new Error(response.data.message || "Failed to update product");
    }
  } catch (err) {
    return { success: false, error: err.response?.data?.message || err.message };
  }
};
