import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import SellerHeader from "./Header";
import Footer from "./Footer";
import { getAllOrders } from "../../services/orderDetails";

const USE_JSON = true; // Set to false for backend

const OrderDetail = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const orderId = Number(state?.orderId);

  const [orderItems, setOrderItems] = useState([]);
  const [orderCustomerName, setOrderCustomerName] = useState("Customer");
  const [loading, setLoading] = useState(true);
  const [allSellerOrders, setAllSellerOrders] = useState([]);
  const [showOrderList, setShowOrderList] = useState(false);
  const [detailedOrders, setDetailedOrders] = useState([]);
  const [expandedOrderId, setExpandedOrderId] = useState(null);

  useEffect(() => {
    const seller = JSON.parse(sessionStorage.getItem('seller')) || JSON.parse(localStorage.getItem('seller'));
    if (!seller) {
      alert("Seller not logged in.");
      navigate("/login", { state: { role: "Seller" } });
      return;
    }

    async function load() {
      const res = await getAllOrders();
      if (!res.success) {
        alert("Failed to load orders: " + res.error);
        return;
      }
      const allOrders = res.data;
      // Only show orders for this seller
      const sellerOrders = allOrders.filter(o => Number(o.seller_id) === Number(seller.seller_id));
      let customers = [];
      let orderItemsData = [];
      let products = [];
      if (USE_JSON) {
        customers = (await import("../../customers.json")).default;
        orderItemsData = (await import("../../order_items.json")).default;
        products = (await import("../../data.json")).default;
      } else {
        // Fetch from backend if needed
      }
      const detailed = sellerOrders.map(order => {
        const customer = customers.find(c => Number(c.customer_id) === Number(order.customer_id));
        const items = orderItemsData
          .filter(item => Number(item.order_id) === order.order_id)
          .map(item => {
            const product = products.find(p => Number(p.product_id) === Number(item.product_id));
            return {
              ...item,
              product_name: product ? product.name : 'Unknown Product',
              price: item.price_per_unit,
            };
          });
        return {
          ...order,
          customer,
          items,
        };
      });
      setDetailedOrders(detailed);
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
  if (showOrderList) {
    return (
      <div className="d-flex flex-column min-vh-100">
        <SellerHeader />
        <div className="container flex-grow-1 mt-5 mb-5">
          <h3 className="mb-4">All Orders for You</h3>
          {detailedOrders.length === 0 ? (
            <p>No orders found for this seller.</p>
          ) : (
            <table className="table table-bordered table-hover">
              <thead className="table-light">
                <tr>
                  <th>Order ID</th>
                  <th>Customer</th>
                  <th>Date</th>
                  <th>Status</th>
                  <th>Total</th>
                  <th>Details</th>
                </tr>
              </thead>
              <tbody>
                {detailedOrders.map(order => (
                  <>
                  <tr key={order.order_id}>
                    <td>{order.order_id}</td>
                    <td>{order.customer ? `${order.customer.first_name} ${order.customer.last_name}` : 'Unknown'}</td>
                    <td>{order.order_date}</td>
                    <td>{order.payment_status}</td>
                    <td>₹{order.total_amount}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-outline-primary"
                        onClick={() => setExpandedOrderId(expandedOrderId === order.order_id ? null : order.order_id)}
                      >
                        {expandedOrderId === order.order_id ? 'Hide' : 'View'} Items
                      </button>
                    </td>
                  </tr>
                  {expandedOrderId === order.order_id && (
                    <tr>
                      <td colSpan={6}>
                        <b>Order Items:</b>
                        <table className="table table-sm mt-2">
                          <thead>
                            <tr>
                              <th>Product</th>
                              <th>Qty</th>
                              <th>Price</th>
                              <th>Subtotal</th>
                            </tr>
                          </thead>
                          <tbody>
                            {order.items.map(item => (
                              <tr key={item.order_item_id}>
                                <td>{item.product_name}</td>
                                <td>{item.quantity}</td>
                                <td>₹{item.price}</td>
                                <td>₹{item.price * item.quantity}</td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </td>
                    </tr>
                  )}
                  </>
                ))}
              </tbody>
            </table>
          )}
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="d-flex flex-column min-vh-100">
      <SellerHeader />
      <div className="container flex-grow-1 mt-5 mb-5" style={{ maxWidth: "1000px" }}>
        <h3 className="text-center mb-4">Order Details for {orderCustomerName}</h3>

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
