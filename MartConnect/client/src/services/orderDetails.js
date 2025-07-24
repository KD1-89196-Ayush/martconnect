// services/orderService.js

const USE_JSON = true; // true = use local JSON, false = use backend

export const getAllOrders = async () => {
  if (USE_JSON) {
    try {
      const orders = await import("../orders.json"); 
      return { success: true, data: orders.default };
    } catch (err) {
      console.error("Error loading local orders.json", err);
      return { success: false, error: "Failed to load local orders" };
    }
  } else {
    try {
      const response = await fetch("http://localhost:4000/api/orders");
      if (!response.ok) throw new Error("Failed to fetch orders");
      const data = await response.json();
      return { success: true, data };
    } catch (err) {
      console.error("API error:", err);
      return { success: false, error: err.message };
    }
  }
};
