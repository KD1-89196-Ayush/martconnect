import api from './axiosConfig';

export const getAllOrders = async () => {
  try {
    const response = await api.get("/orders");

    // Handle direct array response (new format)
    if (Array.isArray(response.data)) {
      return { success: true, data: response.data };
    } else if (response.data.success) {
      // Handle both paginated and non-paginated responses
      const responseData = response.data.data;
      if (responseData && responseData.content) {
        // Paginated response
        return { success: true, data: responseData.content };
      } else if (Array.isArray(responseData)) {
        // Direct array response
        return { success: true, data: responseData };
      } else {
        return { success: true, data: [] };
      }
    } else {
      throw new Error(response.data.message);
    }
  } catch (err) {
    return { success: false, error: err.response?.data?.message || err.message };
  }
};

export const getSellerOrders = async (sellerId) => {
  try {
    const response = await api.get(`/orders/seller/${sellerId}`);
    
    // Handle direct array response (new format)
    if (Array.isArray(response.data)) {
      return response.data;
    } else if (response.data.success) {
      // Handle both paginated and non-paginated responses
      const responseData = response.data.data;
      if (responseData && responseData.content) {
        // Paginated response
        return responseData.content;
      } else if (Array.isArray(responseData)) {
        // Direct array response
        return responseData;
      } else {
        return [];
      }
    } else {
      throw new Error(response.data.message);
    }
  } catch (err) {
    return [];
  }
};

export const getCustomerOrders = async (customerId) => {
  try {
    const response = await api.get(`/orders/customer/${customerId}`);
    
    // Backend returns array directly, not wrapped in success/data
    if (Array.isArray(response.data)) {
      return response.data;
    } else {
      return [];
    }
  } catch (err) {
    return [];
  }
};
