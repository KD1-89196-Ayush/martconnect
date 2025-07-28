import React, { useState, useEffect } from "react";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";
import { toast } from "react-toastify";
import axios from "axios";

const USE_JSON = true; // Set to false if you want to use backend for address saving

function Cart() {
  const [cart, setCart] = useState([]);
  const [showPayment, setShowPayment] = useState(false);
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [areas, setAreas] = useState([]);
  const [selectedAddress, setSelectedAddress] = useState("");
  const [customerId, setCustomerId] = useState(null);
  const [showAddAddress, setShowAddAddress] = useState(false);
  const [newAddress, setNewAddress] = useState({ area_name: '', city: '', state: '', pincode: '' });

  useEffect(() => {
    const storedCart = JSON.parse(localStorage.getItem("cart")) || [];
    setCart(storedCart);
    // Get customerId from session
    const customer = JSON.parse(sessionStorage.getItem("customer"));
    if (customer) setCustomerId(customer.customer_id);
  }, []);

  useEffect(() => {
    // Load areas for this customer
    async function loadAreas() {
      if (!customerId) return;
      let allAreas = [];
      if (USE_JSON) {
        allAreas = JSON.parse(localStorage.getItem('areas')) || [];
        if (!allAreas.length) {
          const module = await import("../../areas.json");
          allAreas = module.default;
          localStorage.setItem('areas', JSON.stringify(allAreas));
        }
      } else {
        // Backend: fetch from API
        const res = await axios.get("http://localhost:4000/api/areas");
        allAreas = res.data;
      }
      const filtered = allAreas.filter(a => a.customer_id === customerId);
      setAreas(filtered);
      if (filtered.length > 0) {
        setSelectedAddress(filtered[filtered.length - 1].area_name);
      }
    }
    loadAreas();
  }, [customerId, showPayment, paymentSuccess]);

  const updateQuantity = (index, quantity) => {
    const newCart = [...cart];
    newCart[index].quantity = quantity;
    setCart(newCart);
    localStorage.setItem("cart", JSON.stringify(newCart));
  };

  const removeItem = (index) => {
    const newCart = cart.filter((_, i) => i !== index);
    setCart(newCart);
    localStorage.setItem("cart", JSON.stringify(newCart));
    toast.info("Item removed from cart");
  };

  const getTotal = () => {
    return cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
  };

  const handleCheckout = () => {
    if (cart.length === 0) {
      toast.warn("Your cart is empty!");
      return;
    }
    setShowPayment(true);
  };

  const handlePaymentConfirm = () => {
    setShowPayment(false);
    setPaymentSuccess(true);
    setCart([]);
    localStorage.removeItem("cart");
    toast.success("Payment successful! Order placed.");
  };

  const handleViewBill = () => {
    const bill = `\nMartConnect Invoice\n----------------------\nDelivery Address: ${selectedAddress}\n${cart.map(item => `${item.name} x${item.quantity} = ₹${item.price * item.quantity}`).join("\n")}\n----------------------\nTotal: ₹${getTotal()}\nThank you for shopping!`;
    alert(bill);
  };

  const handleAddressChange = (e) => {
    const value = e.target.value;
    if (value === '__add_new__') {
      setShowAddAddress(true);
      setSelectedAddress('');
    } else {
      setShowAddAddress(false);
      setSelectedAddress(value);
    }
  };

  const handleNewAddressChange = (e) => {
    setNewAddress({ ...newAddress, [e.target.name]: e.target.value });
  };

  const getNewAddressString = () => newAddress.area_name;

  const saveNewAddress = async () => {
    if (USE_JSON) {
      let areas = JSON.parse(localStorage.getItem('areas')) || [];
      const customer = JSON.parse(sessionStorage.getItem("customer"));
      const addressObj = { ...newAddress, customer_id: customer.customer_id };
      areas.push(addressObj);
      localStorage.setItem('areas', JSON.stringify(areas));
      setAreas([...areas]);
      setSelectedAddress(`${addressObj.area_name}, ${addressObj.city}, ${addressObj.state} - ${addressObj.pincode}`);
      setShowAddAddress(false);
      toast.success('Address added for this session!');
    } else {
      // Backend: send POST to /api/areas or similar
      try {
        const customer = JSON.parse(sessionStorage.getItem("customer"));
        const addressObj = { ...newAddress, customer_id: customer.customer_id };
        await axios.post("http://localhost:4000/api/areas", addressObj);
        setAreas(prev => [...prev, addressObj]);
        setSelectedAddress(`${addressObj.area_name}, ${addressObj.city}, ${addressObj.state} - ${addressObj.pincode}`);
        setShowAddAddress(false);
        toast.success('Address added!');
      } catch (err) {
        toast.error('Failed to save address to backend');
      }
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <CustomerHeader />
      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">My Cart</h2>
        {cart.length > 0 ? (
          <div className="table-responsive">
            <table className="table table-bordered table-hover">
              <thead className="table-light">
                <tr>
                  <th>Product</th>
                  <th>Price</th>
                  <th>Quantity</th>
                  <th>Subtotal</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {cart.map((item, idx) => (
                  <tr key={item.product_id}>
                    <td>{item.name}</td>
                    <td>₹{item.price}</td>
                    <td>
                      <input
                        type="number"
                        min="1"
                        value={item.quantity}
                        onChange={(e) => updateQuantity(idx, Number(e.target.value))}
                        style={{ width: "60px" }}
                      />
                    </td>
                    <td>₹{item.price * item.quantity}</td>
                    <td>
                      <button className="btn btn-sm btn-danger" onClick={() => removeItem(idx)}>
                        Remove
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="text-end">
              <h5>Total: ₹{getTotal()}</h5>
              <button className="btn btn-success" onClick={handleCheckout}>
                Checkout
              </button>
            </div>
          </div>
        ) : paymentSuccess ? (
          <div className="text-center">
            <h4 className="text-success">Payment Successful! Order placed.</h4>
            <button className="btn btn-outline-primary mt-3" onClick={handleViewBill}>
              View Bill
            </button>
          </div>
        ) : (
          <p className="text-center">Your cart is empty.</p>
        )}

        {/* Payment Confirmation Modal */}
        {showPayment && (
          <div className="modal show d-block" tabIndex="-1" style={{ background: "rgba(0,0,0,0.3)" }}>
            <div className="modal-dialog modal-dialog-centered">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Confirm Payment</h5>
                  <button type="button" className="btn-close" onClick={() => setShowPayment(false)}></button>
                </div>
                <div className="modal-body">
                  <p>Total Amount: <b>₹{getTotal()}</b></p>
                  <p>Select Delivery Address:</p>
                  <select
                    className="form-select mb-2"
                    value={showAddAddress ? '__add_new__' : selectedAddress}
                    onChange={handleAddressChange}
                  >
                    {areas.map(area => (
                      <option key={area.area_name} value={area.area_name}>
                        {area.area_name}
                      </option>
                    ))}
                    <option value="__add_new__">Add Another Address</option>
                  </select>
                  {showAddAddress && (
                    <div className="mb-2 p-2 border rounded bg-light">
                      <textarea
                        name="area_name"
                        className="form-control mb-2"
                        placeholder="Enter new delivery address"
                        value={newAddress.area_name}
                        onChange={handleNewAddressChange}
                        rows={2}
                        required
                      />
                      <button
                        className="btn btn-outline-primary btn-sm mt-2"
                        onClick={saveNewAddress}
                        disabled={!newAddress.area_name}
                      >
                        Use This Address
                      </button>
                    </div>
                  )}
                  <p>Proceed to payment and place your order?</p>
                  <p><b>Delivery Address:</b> {showAddAddress ? getNewAddressString() : selectedAddress}</p>
                </div>
                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={() => setShowPayment(false)}>Cancel</button>
                  <button className="btn btn-success" onClick={handlePaymentConfirm}>Pay & Place Order</button>
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

export default Cart; 