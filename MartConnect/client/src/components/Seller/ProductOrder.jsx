import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import SellerHeader from "./Header";
import Footer from "./Footer";
import { getSellerOrders } from "../../services/orderDetails";

const OrderDetail = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const orderId = Number(state?.orderId);

  const [orderItems, setOrderItems] = useState([]);
  const [orderCustomerName, setOrderCustomerName] = useState("Customer");
  const [loading, setLoading] = useState(true);
  const [allSellerOrders, setAllSellerOrders] = useState([]);
  const [showOrderList, setShowOrderList] = useState(true); // Changed to true to show order list by default
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
      // Backend processing logic
      const sellerOrders = await getSellerOrders(seller.seller_id);
      
      const detailed = sellerOrders.map(order => {
        // Extract customer info from backend order
        const customer = order.customer ? {
          customer_id: order.customer.customerId,
          first_name: order.customer.firstName,
          last_name: order.customer.lastName,
          email: order.customer.email,
          phone: order.customer.phone,
          address: order.customer.address
        } : null;
        
        // Extract order items from backend order
        const items = order.orderItems ? order.orderItems.map(item => ({
          order_item_id: item.orderItemId,
          order_id: order.orderId,
          product_id: item.product?.productId,
          product_name: item.product?.name || 'Unknown Product',
          quantity: item.quantity,
          price_per_unit: item.pricePerUnit,
          price: item.pricePerUnit
        })) : [];
        
        return {
          order_id: order.orderId,
          customer_id: order.customer?.customerId,
          seller_id: order.seller?.sellerId,
          total_amount: order.totalAmount,
          delivery_charge: 0,
          payment_status: order.paymentStatus,
          order_date: order.orderDate,
          transaction_id: order.transactionId,
          customer,
          items,
        };
      });
      setDetailedOrders(detailed);
      setLoading(false);
    }
    load();
  }, [navigate]); // Removed orderId dependency since we're showing all orders

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
                  <th>Delivery Address</th>
                  <th>Date</th>
                  <th>Status</th>
                  <th>Details</th>
                </tr>
              </thead>
              <tbody>
                {detailedOrders.map(order => (
                  <React.Fragment key={order.order_id}>
                  <tr>
                    <td>{order.order_id}</td>
                    <td>{order.customer ? `${order.customer.first_name} ${order.customer.last_name}` : 'Unknown'}</td>
                    <td>
                      {order.customer?.address ? (
                        <div style={{ maxWidth: '200px', wordWrap: 'break-word' }}>
                          {order.customer.address}
                        </div>
                      ) : (
                        <span className="text-muted">No address provided</span>
                      )}
                    </td>
                    <td>{order.order_date}</td>
                    <td>{order.payment_status}</td>
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
                        {order.customer && (
                          <div className="mb-3">
                            <h6>Customer Information:</h6>
                            <div className="row">
                              <div className="col-md-6">
                                <strong>Name:</strong> {order.customer.first_name} {order.customer.last_name}<br />
                                <strong>Email:</strong> {order.customer.email}<br />
                                <strong>Phone:</strong> {order.customer.phone}
                              </div>
                              <div className="col-md-6">
                                <strong>Delivery Address:</strong><br />
                                {order.customer.address ? (
                                  <div>
                                    {order.customer.address}
                                  </div>
                                ) : (
                                  <span className="text-muted">No address provided</span>
                                )}
                              </div>
                            </div>
                          </div>
                        )}
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
                  </React.Fragment>
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
