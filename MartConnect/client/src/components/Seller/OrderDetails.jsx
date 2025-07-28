import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import SellerHeader from "./Header";
import Footer from "./Footer";
import { getAllOrders } from "../../services/orderDetails";
import orderItemsData from "../../order_items.json";
import products from "../../data.json";
import customers from "../../customers.json";

const OrderDetail = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const orderId = Number(state?.orderId);

  const [orderItems, setOrderItems] = useState([]);
  const [orderCustomerName, setOrderCustomerName] = useState("Customer");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const seller = JSON.parse(localStorage.getItem("seller"));
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

      console.log("Order ID from state:", orderId);
      console.log("Seller from localStorage:", seller);
      console.log("Orders received:", allOrders);

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

      const cust = customers.find(c => Number(c.customer_id) === Number(selectedOrder.customer_id));
      setOrderCustomerName(
        cust ? `${cust.first_name} ${cust.last_name}` : "Customer"
      );

      const items = orderItemsData
        .filter(item => Number(item.order_id) === orderId)
        .map(item => {
          const prod = products.find(p => Number(p.product_id) === Number(item.product_id));
          return {
            product_id: item.product_id,
            name: prod?.name || "Product",
            price: item.price_per_unit,
            quantity: item.quantity,
            packed: false
          };
        });

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
