const USE_JSON = true;

export const updateProduct = async (updatedProduct) => {
  if (USE_JSON) {
    let products = JSON.parse(localStorage.getItem('products')) || [];
    const idx = products.findIndex(p => p.product_id === updatedProduct.product_id);
    if (idx !== -1) {
      products[idx] = { ...products[idx], ...updatedProduct };
      localStorage.setItem('products', JSON.stringify(products));
      return { success: true, message: "Product updated locally." };
    } else {
      return { success: false, error: "Product not found." };
    }
  } else {
    try {
      const formData = new FormData();
      for (let key in updatedProduct) {
        formData.append(key, updatedProduct[key]);
      }
      const response = await fetch(
        `http://localhost:4000/api/products/${updatedProduct.id}`,
        {
          method: "PUT",
          body: formData,
        }
      );
      if (!response.ok) throw new Error("Failed to update product");
      const data = await response.json();
      return { success: true, data };
    } catch (err) {
      return { success: false, error: err.message };
    }
  }
};
