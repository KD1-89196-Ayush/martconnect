import api from "./axiosConfig";

// Fetch all categories
export const fetchCategories = async () => {
  try {
    const response = await api.get("/categories");
    // Handle direct array response (new format)
    if (Array.isArray(response.data)) {
      return response.data;
    } else if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error; // Throw error instead of returning hardcoded data
  }
};

// Get category by name
export const getCategoryByName = async (categoryName) => {
  try {
    const categories = await fetchCategories();
    return categories.find(cat => cat.name === categoryName);
  } catch (error) {
    return null;
  }
};

// Get category by ID
export const getCategoryById = async (categoryId) => {
  try {
    const response = await api.get(`/categories/${categoryId}`);
    // Handle direct object response (new format)
    if (response.data && response.data.categoryId) {
      return response.data;
    } else if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    return null;
  }
};

// Create new category
export const createCategory = async (categoryData) => {
  try {
    const response = await api.post("/categories", categoryData);
    // Handle direct object response (new format)
    if (response.data && response.data.categoryId) {
      return response.data;
    } else if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

// Update category
export const updateCategory = async (categoryId, categoryData) => {
  try {
    const response = await api.put(`/categories/${categoryId}`, categoryData);
    // Handle direct object response (new format)
    if (response.data && response.data.categoryId) {
      return response.data;
    } else if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

// Delete category
export const deleteCategory = async (categoryId) => {
  try {
    const response = await api.delete(`/categories/${categoryId}`);
    // Handle direct response (new format)
    if (response.status === 200) {
      return true;
    } else if (response.data.success) {
      return true;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    throw error;
  }
};

// Search categories
export const searchCategories = async (searchTerm) => {
  try {
    const response = await api.get(`/categories/search?name=${encodeURIComponent(searchTerm)}`);
    // Handle direct array response (new format)
    if (Array.isArray(response.data)) {
      return response.data;
    } else if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    return [];
  }
}; 