export const updateProduct = async (updatedProduct) => {
  if (USE_JSON) {
    console.log("Simulating product update in JSON mode:", updatedProduct);
    return { success: true, message: "Product updated locally." };
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
