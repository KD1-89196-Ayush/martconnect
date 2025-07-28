const USE_JSON = true; // Toggle between local JSON and backend

export const getAllOrders = async () => {
  if (USE_JSON) {
    try {
      // Dynamic import - must be default export
      const ordersModule = await import("../orders.json");
      const orders = ordersModule.default;

      if (!Array.isArray(orders)) {
        throw new Error("orders.json is not an array");
      }

      return { success: true, data: orders };
    } catch (err) {
      console.error("Local JSON import error:", err);
      return { success: false, error: "Failed to load local orders" };
    }
  } else {
    try {
      const response = await fetch("http://localhost:4000/api/orders");

      if (!response.ok) {
        throw new Error(`API Error: ${response.statusText}`);
      }

      const data = await response.json();

      if (!Array.isArray(data)) {
        throw new Error("API response is not an array");
      }

      return { success: true, data };
    } catch (err) {
      console.error("API error:", err);
      return { success: false, error: err.message };
    }
  }
};
