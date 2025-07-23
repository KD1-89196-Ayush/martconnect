import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const OrderDetail = () => {
  const { state } = useLocation();
  const navigate = useNavigate();
  const order = state?.order;

  const [orderItems, setOrderItems] = useState(order?.orderItems || []);

  const handlePriceChange = (index, value) => {
    const updated = [...orderItems];
    updated[index].price = Number(value);
    setOrderItems(updated);
  };

  const handleQuantityChange = (index, value) => {
    const updated = [...orderItems];
    updated[index].quantity = Number(value);
    setOrderItems(updated);
  };

  const handlePackChange = (index, checked) => {
    const updated = [...orderItems];
    updated[index].packed = checked;
    setOrderItems(updated);
  };

  const getTotal = (price, quantity) => price * quantity;

  const totalBill = orderItems.reduce(
    (sum, item) => sum + getTotal(item.price || 0, item.quantity || 0),
    0
  );

  if (!order) {
    return <div className="container mt-5">No order data found.</div>;
  }

  return (
    <div className="container mt-5"  style={{ maxWidth: "1000px", marginBottom:"300px", marginLeft: "40vh"}}>
      <h3 className="text-center mb-4">Order List for {order.customerName}</h3>

      <table className="table table-bordered table-hover">
        <thead className="table-primary text-center">
          <tr>
            <th>Sr.No</th>
            <th>Product Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Total</th>
            <th>Pack</th>
          </tr>
        </thead>
        <tbody className="text-center">
          {orderItems.map((item, index) => (
            <tr key={index}>
              <td>{index + 1}</td>
              <td>{item.name}</td>

              <td>
                <input
                  type="number"
                  value={item.price || ""}
                  onChange={(e) => handlePriceChange(index, e.target.value)}
                  className="form-control form-control-sm mb-1"
                  style={{ width: "80px", display: "inline-block" }}
                />
                <select className="form-select form-select-sm" style={{ width: "100px", display: "inline-block", marginLeft:"10px"}}>
                  <option>kg</option>
                  <option>liter</option>
                  <option>unit</option>
                </select>
              </td>

              <td>
                <input
                  type="number"
                  value={item.quantity || ""}
                  onChange={(e) => handleQuantityChange(index, e.target.value)}
                  className="form-control form-control-sm mb-2"
                  style={{ width: "100px", display: "inline-block" }}
                />
                <select className="form-select form-select-sm" style={{ width: "100px", display: "inline-block", marginLeft:"10px" }}>
                  <option>kg</option>
                  <option>liter</option>
                  <option>unit</option>
                </select>
              </td>

              <td>{getTotal(item.price || 0, item.quantity || 0)}</td>

              <td>
                <input
                  type="checkbox"
                  checked={item.packed || false}
                  onChange={(e) => handlePackChange(index, e.target.checked)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h5 className="text-end mt-4">
        <strong>Total Bill: ₹{totalBill}</strong>
      </h5>

      <div className="text-center mt-3">
        <button onClick={() => navigate(-1)} className="btn btn-secondary">
          Back
        </button>
      </div>
    </div>
  );
};

export default OrderDetail;
