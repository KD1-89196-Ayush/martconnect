import api from './axiosConfig';

export const addProduct = async (productData) => {
  try {
    const response = await api.post("/products", productData);

    // Handle direct response (new format)
    if (response.data && response.data.productId) {
      return { success: true, data: response.data };
    } else if (response.data.success) {
      return { success: true, data: response.data.data };
    } else {
      throw new Error(response.data.message || "Failed to add product");
    }
  } catch (err) {
    return { success: false, error: err.response?.data?.message || err.message };
  }
};
