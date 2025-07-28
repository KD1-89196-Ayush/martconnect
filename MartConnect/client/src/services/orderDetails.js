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

export const getCustomerOrders = async (customerId) => {
  if (USE_JSON) {
    try {
      // Load orders and order items
      const ordersModule = await import("../orders.json");
      const orderItemsModule = await import("../order_items.json");
      const productsModule = await import("../data.json");
      const orders = ordersModule.default;
      const orderItems = orderItemsModule.default;
      const products = productsModule.default;

      // Filter orders for this customer
      const customerOrders = orders.filter((order) => order.customer_id === customerId);

      // Attach items and product info to each order
      const ordersWithItems = customerOrders.map((order) => {
        const items = orderItems
          .filter((item) => item.order_id === order.order_id)
          .map((item) => {
            const product = products.find((p) => p.product_id === item.product_id);
            return {
              ...item,
              name: product ? product.name : "Unknown Product",
              price: item.price_per_unit,
            };
          });
        return {
          ...order,
          items,
          total: order.total_amount,
          status: order.payment_status,
          date: order.order_date,
        };
      });
      return ordersWithItems;
    } catch (err) {
      console.error("Error loading customer orders:", err);
      return [];
    }
  } else {
    try {
      const response = await fetch(`http://localhost:4000/api/orders/customer/${customerId}`);
      if (!response.ok) throw new Error("Failed to fetch customer orders from backend");
      const data = await response.json();
      return data; // Should be in the same format as above
    } catch (err) {
      console.error("API error:", err);
      return [];
    }
  }
};
