import React, { useEffect, useState } from "react";
import SellerHeader from "./Header";
import Footer from "./Footer";
import { getAllOrders } from "../../services/orderDetails";

const Orders = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    async function fetchOrders() {
      const result = await getAllOrders();
      if (result.success) {
        setOrders(result.data);
      } else {
        alert("Failed to load orders: " + result.error);
      }
    }

    fetchOrders();
  }, []);

  return (
    <div className="d-flex flex-column min-vh-100">
      <SellerHeader />
      <div className="container flex-grow-1 mt-5 mb-5">
        <h3 className="text-center mb-4">All Orders</h3>

        <table className="table table-bordered table-hover">
          <thead className="table-dark text-center">
            <tr>
              <th>Order ID</th>
              <th>Customer ID</th>
              <th>Total Amount (₹)</th>
              <th>Delivery Charge (₹)</th>
              <th>Payment Status</th>
              <th>Order Date</th>
              <th>Transaction ID</th>
            </tr>
          </thead>
          <tbody className="text-center">
            {orders.map((order, index) => (
              <tr key={index}>
                <td>{order.order_id}</td>
                <td>{order.customer_id}</td>
                <td>{order.total_amount.toFixed(2)}</td>
                <td>{order.delivery_charge.toFixed(2)}</td>
                <td>
                  <span
                    className={`badge ${
                      order.payment_status === "Paid" ? "bg-success" : "bg-warning"
                    }`}
                  >
                    {order.payment_status}
                  </span>
                </td>
                <td>{new Date(order.order_date).toLocaleString()}</td>
                <td>{order.transaction_id}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Footer />
    </div>
  );
};

export default Orders;
