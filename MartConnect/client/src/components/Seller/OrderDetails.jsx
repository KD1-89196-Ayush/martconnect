import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import SellerHeader from "./Header";
import Footer from "./Footer";
import { getAllOrders } from "../../services/orderDetails";

const OrderDetail = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const orderId = Number(state?.orderId);

  const [orderItems, setOrderItems] = useState([]);
  const [orderCustomerName, setOrderCustomerName] = useState("Customer");
  const [orderCustomerAddress, setOrderCustomerAddress] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const seller = JSON.parse(sessionStorage.getItem('seller')) || JSON.parse(localStorage.getItem('seller'));
    if (!seller) {
      alert("Seller not logged in.");
      navigate("/login", { state: { role: "Seller" } });
      return;
    }

    async function load() {
      const res = await getAllOrders();
      if (!res || !res.success) {
        alert("Failed to load orders: " + (res?.error || "Unknown error"));
        return;
      }
      const allOrders = Array.isArray(res.data) ? res.data : res.data?.default;
      if (!Array.isArray(allOrders)) {
        alert("Orders data is not in expected array format.");
        return;
      }
      const selectedOrder = allOrders.find(
        o => Number(o.order_id) === orderId && Number(o.seller_id) === Number(seller.seller_id)
      );
      if (!selectedOrder) {
        alert("Order not found for this seller.");
        return;
      }

      // For now, we'll use placeholder data since JSON imports are removed
      // In a real implementation, this would fetch from backend APIs
      const cust = { first_name: "Customer", last_name: "Name", address: "Address" };
      setOrderCustomerName(
        cust ? `${cust.first_name} ${cust.last_name}` : "Customer"
      );
      setOrderCustomerAddress(cust?.address || "");

      // Placeholder order items - in real implementation, fetch from backend
      const items = [
        {
          product_id: 1,
          name: "Product",
          price: 0,
          quantity: 1,
          packed: false
        }
      ];

      setOrderItems(items);
      setLoading(false);
    }

    load();
  }, [orderId, navigate]);

  const handlePriceChange = (index, value) => {
    const newItems = [...orderItems];
    newItems[index].price = Number(value);
    setOrderItems(newItems);
  };

  const handleQuantityChange = (index, value) => {
    const newItems = [...orderItems];
    newItems[index].quantity = Number(value);
    setOrderItems(newItems);
  };

  const handlePackChange = (index, checked) => {
    const newItems = [...orderItems];
    newItems[index].packed = checked;
    setOrderItems(newItems);
  };

  if (loading) {
    return <><SellerHeader /><div className="container mt-5 text-center">Loading Order...</div><Footer /></>;
  }

  return (
    <div className="d-flex flex-column min-vh-100">
      <SellerHeader />
      <div className="container flex-grow-1 mt-5 mb-5" style={{ maxWidth: "1000px" }}>
        <h3 className="text-center mb-4">Order Details for {orderCustomerName}</h3>
        
        <div className="alert alert-info mb-4">
          <div className="row">
            <div className="col-md-6">
              <strong>Customer:</strong> {orderCustomerName}<br />
              <strong>Delivery Address:</strong><br />
              {orderCustomerAddress ? (
                <div>
                  {orderCustomerAddress}
                  <button 
                    className="btn btn-sm btn-outline-secondary ms-2"
                    onClick={() => navigator.clipboard.writeText(orderCustomerAddress)}
                    title="Copy address to clipboard"
                  >
                    📋 Copy
                  </button>
                </div>
              ) : (
                <span className="text-muted">No address provided</span>
              )}
            </div>
            <div className="col-md-6">
              <strong>Order ID:</strong> {orderId}<br />
              <strong>Order Date:</strong> {new Date().toLocaleDateString()}
            </div>
          </div>
        </div>

        <table className="table table-bordered table-hover">
          <thead className="table-primary text-center">
            <tr>
              <th>Sr.</th>
              <th>Product ID</th>
              <th>Name</th>
              <th>Price (₹)</th>
              <th>Qty</th>
              <th>Total (₹)</th>
              <th>Packed?</th>
            </tr>
          </thead>
          <tbody className="text-center">
            {orderItems.map((item, i) => (
              <tr key={i}>
                <td>{i + 1}</td>
                <td>{item.product_id}</td>
                <td>{item.name}</td>
                <td>
                  <input
                    type="number"
                    value={item.price}
                    onChange={e => handlePriceChange(i, e.target.value)}
                    className="form-control form-control-sm"
                    style={{ width: "80px" }}
                  />
                </td>
                <td>
                  <input
                    type="number"
                    value={item.quantity}
                    onChange={e => handleQuantityChange(i, e.target.value)}
                    className="form-control form-control-sm"
                    style={{ width: "80px" }}
                  />
                </td>
                <td>{(item.price * item.quantity).toFixed(2)}</td>
                <td>
                  <input
                    type="checkbox"
                    checked={item.packed}
                    onChange={e => handlePackChange(i, e.target.checked)}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <h5 className="text-end mt-4">Total Bill: ₹{orderItems.reduce((s, it) => s + it.price * it.quantity, 0).toFixed(2)}</h5>

        <div className="text-center mt-3">
          <button onClick={() => navigate(-1)} className="btn btn-secondary">Back</button>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default OrderDetail;
