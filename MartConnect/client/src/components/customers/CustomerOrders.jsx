import React, { useEffect, useState } from "react";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";
import { getCustomerOrders } from "../../services/orderDetails";
import { toast } from "react-toastify";

function CustomerOrders() {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    const loadOrders = async () => {
      try {
        const customer = JSON.parse(sessionStorage.getItem("customer"));
        if (!customer) {
          toast.error("Please login to view your orders.");
          return;
        }
        const result = await getCustomerOrders(customer.customer_id);
        setOrders(result);
      } catch (err) {
        toast.error("Failed to load orders");
      }
    };
    loadOrders();
  }, []);

  const handleViewDetails = (order) => {
    setSelectedOrder(order);
    setShowModal(true);
  };

  const handleDownloadBill = () => {
    if (!selectedOrder) return;
    // Generate HTML bill
    const billHtml = `
      <html>
      <head>
        <title>MartConnect Invoice</title>
        <style>
          body { font-family: Arial, sans-serif; margin: 40px; }
          h2 { color: #007bff; }
          table { width: 100%; border-collapse: collapse; margin-top: 20px; }
          th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
          th { background: #f5f5f5; }
          .total { font-weight: bold; }
        </style>
      </head>
      <body>
        <h2>MartConnect Invoice</h2>
        <p><b>Order ID:</b> ${selectedOrder.order_id}</p>
        <p><b>Date:</b> ${selectedOrder.date}</p>
        <p><b>Status:</b> ${selectedOrder.status}</p>
        <table>
          <thead>
            <tr><th>Product</th><th>Quantity</th><th>Price</th><th>Subtotal</th></tr>
          </thead>
          <tbody>
            ${selectedOrder.items.map(item => `
              <tr>
                <td>${item.name}</td>
                <td>${item.quantity}</td>
                <td>₹${item.price}</td>
                <td>₹${item.price * item.quantity}</td>
              </tr>
            `).join('')}
          </tbody>
        </table>
        <p class="total">Total: ₹${selectedOrder.total}</p>
        <p>Thank you for shopping with MartConnect!</p>
      </body>
      </html>
    `;
    // Open in new window and print
    const printWindow = window.open('', '', 'width=800,height=600');
    printWindow.document.write(billHtml);
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <CustomerHeader />
      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">My Orders</h2>
        {orders.length > 0 ? (
          <div className="table-responsive">
            <table className="table table-bordered table-hover">
              <thead className="table-light">
                <tr>
                  <th>Order ID</th>
                  <th>Date</th>
                  <th>Status</th>
                  <th>Total</th>
                  <th>Details</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((order) => (
                  <tr key={order.order_id}>
                    <td>{order.order_id}</td>
                    <td>{order.date}</td>
                    <td>{order.status}</td>
                    <td>₹{order.total}</td>
                    <td>
                      <ul>
                        {order.items.map((item) => (
                          <li key={item.product_id}>
                            {item.name} x {item.quantity} (₹{item.price})
                          </li>
                        ))}
                      </ul>
                    </td>
                    <td>
                      <button className="btn btn-sm btn-outline-primary" onClick={() => handleViewDetails(order)}>
                        View Details
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-center">No orders found.</p>
        )}

        {/* Order Details Modal */}
        {showModal && selectedOrder && (
          <div className="modal show d-block" tabIndex="-1" style={{ background: "rgba(0,0,0,0.3)" }}>
            <div className="modal-dialog modal-dialog-centered">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Order Details</h5>
                  <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
                </div>
                <div className="modal-body">
                  <p><b>Order ID:</b> {selectedOrder.order_id}</p>
                  <p><b>Date:</b> {selectedOrder.date}</p>
                  <p><b>Status:</b> {selectedOrder.status}</p>
                  <p><b>Total:</b> ₹{selectedOrder.total}</p>
                  <p><b>Products:</b></p>
                  <ul>
                    {selectedOrder.items.map((item) => (
                      <li key={item.product_id}>
                        {item.name} x {item.quantity} (₹{item.price})
                      </li>
                    ))}
                  </ul>
                  {/* Add more details as needed, e.g., delivery address, payment info */}
                </div>
                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={() => setShowModal(false)}>Close</button>
                  <button className="btn btn-primary" onClick={handleDownloadBill}>Download Bill</button>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>
      <CustomerFooter />
    </div>
  );
}

export default CustomerOrders; 