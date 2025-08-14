import React, { useState, useEffect } from "react";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import api from "../../services/axiosConfig";

function Cart() {
  const [cart, setCart] = useState([]);
  const [showPayment, setShowPayment] = useState(false);
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [areas, setAreas] = useState([]);
  const [selectedAddress, setSelectedAddress] = useState("");
  const [customerId, setCustomerId] = useState(null);
  const [showAddAddress, setShowAddAddress] = useState(false);
  const [newAddress, setNewAddress] = useState({ area_name: '', city: '', state: '', pincode: '' });
  const [selectedAddressIndex, setSelectedAddressIndex] = useState(-1);
  const navigate = useNavigate();

  useEffect(() => {
    const storedCart = JSON.parse(localStorage.getItem("cart")) || [];
    
    // Clean up cart items to ensure they have required fields
    const cleanedCart = storedCart.filter(item => {
      if (!item.seller_id || !item.product_id) {
        return false;
      }
      return true;
    });
    
    // Update localStorage with cleaned cart
    if (cleanedCart.length !== storedCart.length) {
      localStorage.setItem("cart", JSON.stringify(cleanedCart));
    }
    
    setCart(cleanedCart);
    
    // Get customerId from localStorage (not sessionStorage)
    const customer = JSON.parse(localStorage.getItem("customer"));
    if (customer) setCustomerId(customer.customer_id);
  }, []);

  useEffect(() => {
    // Load areas for this customer
    async function loadAreas() {
      if (!customerId) return;
      let allAreas = [];
      
      // Backend: fetch from API
      try {
        const response = await api.get(`/areas/customer/${customerId}`);
        
        if (response.status !== 200) {
          const errorText = response.data;
          throw new Error("Failed to fetch areas");
        }
        
        const data = response.data;
        
        // Backend returns array directly, not wrapped in success/data
        allAreas = Array.isArray(data) ? data : [];
      } catch (error) {
        allAreas = [];
      }
      
      // The backend API should already return only areas for this customer
      // No need for additional filtering
      setAreas(allAreas);
      if (allAreas.length > 0) {
        setSelectedAddressIndex(0); // Set to first address
        const firstAddress = allAreas[0];
        setSelectedAddress(`${firstAddress.area_name}, ${firstAddress.city}, ${firstAddress.state} - ${firstAddress.pincode}`);
      } else {
        setSelectedAddress("");
        setSelectedAddressIndex(-1);
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

  const handlePaymentConfirm = async () => {
    if (!selectedAddress) {
      toast.error("Please select a delivery address");
      return;
    }

    try {
      const customer = JSON.parse(localStorage.getItem("customer"));
      if (!customer) {
        toast.error("Please login to place order");
        return;
      }
      
      // Validate customer data
      if (!customer.customer_id) {
        toast.error("Invalid customer data. Please login again.");
        return;
      }

      // Create orders for each seller
      const itemsBySeller = {};
      cart.forEach(item => {
        // Validate cart item data
        if (!item.seller_id) {
          throw new Error("Cart item missing seller_id: " + JSON.stringify(item));
        }
        if (!item.product_id) {
          throw new Error("Cart item missing product_id: " + JSON.stringify(item));
        }
        if (!item.quantity || item.quantity <= 0) {
          throw new Error("Cart item has invalid quantity: " + JSON.stringify(item));
        }
        if (!item.price || item.price <= 0) {
          throw new Error("Cart item has invalid price: " + JSON.stringify(item));
        }
        
        if (!itemsBySeller[item.seller_id]) {
          itemsBySeller[item.seller_id] = [];
        }
        itemsBySeller[item.seller_id].push(item);
      });

      // Create orders using backend API
      for (const [sellerId, items] of Object.entries(itemsBySeller)) {
        const totalAmount = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
        
        // Create order data for backend
        const orderData = {
          customer: {
            customerId: Number(customer.customer_id)
          },
          seller: {
            sellerId: Number(sellerId)
          },
          totalAmount: totalAmount,
          deliveryCharge: 0,
          paymentStatus: "PAID",
          transactionId: String(Math.floor(Math.random() * 1000000) + 100000),
          orderItems: items.map(item => ({
            product: {
              productId: Number(item.product_id)
            },
            quantity: Number(item.quantity),
            pricePerUnit: Number(item.price)
          }))
        };
        
        // Validate order data
        if (!orderData.customer.customerId || orderData.customer.customerId <= 0) {
          throw new Error("Invalid customer ID");
        }
        if (!orderData.seller.sellerId || orderData.seller.sellerId <= 0) {
          throw new Error("Invalid seller ID");
        }
        if (!orderData.totalAmount || orderData.totalAmount <= 0) {
          throw new Error("Invalid total amount");
        }
        if (!orderData.orderItems || orderData.orderItems.length === 0) {
          throw new Error("No order items");
        }
        orderData.orderItems.forEach((item, index) => {
          if (!item.product.productId || item.product.productId <= 0) {
            throw new Error(`Invalid product ID at index ${index}`);
          }
          if (!item.quantity || item.quantity <= 0) {
            throw new Error(`Invalid quantity at index ${index}`);
          }
          if (!item.pricePerUnit || item.pricePerUnit <= 0) {
            throw new Error(`Invalid price at index ${index}`);
          }
        });
        
        // First check if backend is accessible
        // try {
        //   await api.get("/orders");
        // } catch (error) {
        //   throw new Error("Backend server is not running. Please start the server.");
        // }
        
        const response = await api.post("/orders", orderData);

        if (response.status !== 201) {
          let errorMessage = "Failed to create order";
          try {
            const errorData = response.data;
            errorMessage = errorData.message || errorMessage;
          } catch (e) {
            // Error parsing error response
          }
          throw new Error(errorMessage);
        }

        const result = response.data;
        
        // Backend returns Order object directly, not wrapped in success field
        if (!result || !result.orderId) {
          throw new Error("Invalid order response from backend");
        }
      }
      
      setShowPayment(false);
      setPaymentSuccess(true);
      setCart([]);
      localStorage.removeItem("cart");
      toast.success("Payment successful! Orders placed.");
    } catch (error) {
      toast.error("Failed to place orders. Please try again.");
    }
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
      setSelectedAddressIndex(-1);
    } else {
      setShowAddAddress(false);
      const index = parseInt(value);
      setSelectedAddressIndex(index);
      if (areas[index]) {
        const selectedArea = areas[index];
        setSelectedAddress(`${selectedArea.area_name}, ${selectedArea.city}, ${selectedArea.state} - ${selectedArea.pincode}`);
      }
    }
  };

  const handleNewAddressChange = (field, value) => {
    setNewAddress(prev => ({ ...prev, [field]: value }));
  };

  const getNewAddressString = () => `${newAddress.area_name}, ${newAddress.city}, ${newAddress.state} - ${newAddress.pincode}`;

  const saveNewAddress = async () => {
    if (!newAddress.area_name || !newAddress.city || !newAddress.state || !newAddress.pincode) {
      toast.error('Please fill all address fields');
      return;
    }
    
    // Backend: send POST to /api/areas
    try {
      const customer = JSON.parse(localStorage.getItem("customer"));
      const addressData = { 
        ...newAddress, 
        customer_id: Number(customer.customer_id) 
      };
      
      const response = await api.post("/areas", addressData);

      if (response.status !== 201) {
        const errorText = response.data;
        throw new Error("Failed to save address");
      }

      const result = response.data;
      
      // Backend returns the Area object directly, not wrapped in success/data
      if (result && result.areaId) {
        // Reload areas from backend
        const areasResponse = await api.get(`/areas/customer/${customer.customer_id}`);
        if (areasResponse.status === 200) {
          const areasData = areasResponse.data;
          
          // Backend returns array directly, not wrapped in success/data
          const updatedAreas = Array.isArray(areasData) ? areasData : [];
          setAreas(updatedAreas);
          
          // Set the newly added address as selected
          const newIndex = updatedAreas.length - 1;
          setSelectedAddressIndex(newIndex);
          setSelectedAddress(`${newAddress.area_name}, ${newAddress.city}, ${newAddress.state} - ${newAddress.pincode}`);
        }
      }
      
      setShowAddAddress(false);
      setNewAddress({ area_name: '', city: '', state: '', pincode: '' });
      toast.success('Address added successfully!');
    } catch (err) {
      // Check if it's an authentication error
      if (err.response?.status === 401) {
        toast.error('Authentication failed. Please login again.');
        // Don't redirect here, let the axios interceptor handle it
      } else if (err.response?.status === 400) {
        toast.error('Invalid address data. Please check your input.');
      } else if (err.response?.status === 500) {
        toast.error('Server error. Please try again later.');
      } else {
        toast.error('Failed to save address. Please try again.');
      }
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <CustomerHeader />
      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">My Cart</h2>
        
        {cart.length > 0 ? (
          <div className="row">
            <div className="col-lg-8 mb-4">
              <div className="card">
                <div className="card-header">
                  <h5 className="mb-0">Cart Items ({cart.length})</h5>
                </div>
                <div className="card-body p-0">
                  <div className="table-responsive">
                    <table className="table table-hover mb-0">
                      <thead className="table-light">
                        <tr>
                          <th>Product</th>
                          <th className="text-center">Price</th>
                          <th className="text-center">Quantity</th>
                          <th className="text-center">Subtotal</th>
                          <th className="text-center">Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        {cart.map((item, idx) => (
                          <tr key={item.product_id}>
                            <td>
                              <div className="d-flex align-items-center">
                                <img 
                                  src={item.image_url || item.imageUrl} 
                                  alt={item.name}
                                  className="me-3"
                                  style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                                />
                                <div>
                                  <h6 className="mb-0">{item.name}</h6>
                                  <small className="text-muted">
                                    {/* Assuming sellerIdToShopName is defined elsewhere or will be added */}
                                    {/* For now, using a placeholder or assuming it's not needed */}
                                    {/* {sellerIdToShopName[item.seller_id] || "Unknown Seller"} */}
                                  </small>
                                </div>
                              </div>
                            </td>
                            <td className="text-center">₹{item.price}</td>
                            <td className="text-center">
                              <input
                                type="number"
                                min="1"
                                value={item.quantity}
                                onChange={(e) => updateQuantity(idx, Number(e.target.value))}
                                className="form-control form-control-sm"
                                style={{ width: '80px', margin: '0 auto' }}
                              />
                            </td>
                            <td className="text-center">₹{item.price * item.quantity}</td>
                            <td className="text-center">
                              <button 
                                className="btn btn-outline-danger btn-sm" 
                                onClick={() => removeItem(idx)}
                              >
                                Remove
                              </button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>

            <div className="col-lg-4">
              <div className="card">
                <div className="card-header">
                  <h5 className="mb-0">Order Summary</h5>
                </div>
                <div className="card-body">
                  <div className="d-flex justify-content-between mb-2">
                    <span>Subtotal:</span>
                    <span>₹{getTotal()}</span>
                  </div>
                  <div className="d-flex justify-content-between mb-2">
                    <span>Delivery:</span>
                    <span className="text-success">Free</span>
                  </div>
                  <hr />
                  <div className="d-flex justify-content-between mb-3">
                    <span className="fw-bold">Total:</span>
                    <span className="fw-bold text-primary">₹{getTotal()}</span>
                  </div>
                  
                  <button 
                    className="btn btn-primary w-100 mb-2"
                    onClick={handleCheckout}
                  >
                    Proceed to Checkout
                  </button>
                  
                  <button 
                    className="btn btn-outline-secondary w-100"
                    onClick={() => window.history.back()}
                  >
                    Continue Shopping
                  </button>
                </div>
              </div>
            </div>
          </div>
        ) : paymentSuccess ? (
          <div className="text-center py-5">
            <h4 className="text-success mb-3">Payment Successful!</h4>
            <p className="text-muted mb-4">Your order has been placed successfully.</p>
            <button 
              className="btn btn-primary"
              onClick={handleViewBill}
            >
              View Bill
            </button>
          </div>
        ) : (
          <div className="text-center py-5">
            <h4 className="text-muted mb-3">Your cart is empty</h4>
            <p className="text-muted mb-4">Add some products to get started!</p>
            <button 
              className="btn btn-primary"
              onClick={() => navigate("/customer-home")}
            >
              Start Shopping
            </button>
          </div>
        )}

        {/* Payment Confirmation Modal */}
        {showPayment && (
          <div className="modal show d-block" tabIndex="-1" style={{ background: "rgba(0,0,0,0.5)" }}>
            <div className="modal-dialog modal-dialog-centered">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Confirm Payment</h5>
                  <button type="button" className="btn-close" onClick={() => setShowPayment(false)}></button>
                </div>
                <div className="modal-body">
                  <p>Total Amount: <b>₹{getTotal()}</b></p>
                  <p>Select Delivery Address:</p>
                  {areas.length > 0 ? (
                    <select
                      className="form-select mb-2"
                      value={showAddAddress ? '__add_new__' : (selectedAddressIndex >= 0 ? selectedAddressIndex.toString() : '')}
                      onChange={handleAddressChange}
                    >
                      {areas.map((area, index) => (
                        <option key={index} value={index}>
                          {area.area_name}, {area.city}, {area.state} - {area.pincode}
                        </option>
                      ))}
                      <option value="__add_new__">Add Another Address</option>
                    </select>
                  ) : (
                    <div className="alert alert-info mb-2">
                      No addresses found. Please add a delivery address.
                      <button 
                        className="btn btn-sm btn-primary ms-2" 
                        onClick={() => setShowAddAddress(true)}
                      >
                        Add Address
                      </button>
                    </div>
                  )}
                  
                  {showAddAddress && (
                    <div className="mb-2 p-2 border rounded bg-light">
                      <input
                        type="text"
                        className="form-control mb-2"
                        placeholder="Area/Locality"
                        value={newAddress.area_name}
                        onChange={(e) => handleNewAddressChange('area_name', e.target.value)}
                      />
                      <input
                        type="text"
                        className="form-control mb-2"
                        placeholder="City"
                        value={newAddress.city}
                        onChange={(e) => handleNewAddressChange('city', e.target.value)}
                      />
                      <input
                        type="text"
                        className="form-control mb-2"
                        placeholder="State"
                        value={newAddress.state}
                        onChange={(e) => handleNewAddressChange('state', e.target.value)}
                      />
                      <input
                        type="text"
                        className="form-control mb-2"
                        placeholder="Pincode"
                        value={newAddress.pincode}
                        onChange={(e) => handleNewAddressChange('pincode', e.target.value)}
                      />
                      <button
                        className="btn btn-outline-primary btn-sm"
                        onClick={saveNewAddress}
                      >
                        Save Address
                      </button>
                    </div>
                  )}
                </div>
                <div className="modal-footer">
                  <button 
                    type="button" 
                    className="btn btn-secondary" 
                    onClick={() => setShowPayment(false)}
                  >
                    Cancel
                  </button>
                  <button 
                    type="button" 
                    className="btn btn-primary"
                    onClick={handlePaymentConfirm}
                    disabled={areas.length === 0 && !showAddAddress}
                  >
                    Confirm Payment
                  </button>
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