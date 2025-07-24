const USE_JSON = false; // true = local JSON mode, false = backend mode

export const addProduct = async (productData) => {
  if (USE_JSON) {
    console.log("Simulating product add in JSON mode:", productData);
    return { success: true, message: "Product added locally." };
  } else {
    try {
      const formData = new FormData();
      for (let key in productData) {
        formData.append(key, productData[key]);
      }

      const response = await fetch("http://localhost:4000/api/products", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) throw new Error("Failed to add product");

      const data = await response.json();
      return { success: true, data };
    } catch (err) {
      return { success: false, error: err.message };
    }
  }
};
