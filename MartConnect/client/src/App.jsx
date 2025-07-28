import React, { useState, useEffect } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';

import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';

import SellerHome from './components/Seller/SellerHome';
import About from './components/Seller/About';
import SellerContact from './components/Seller/contact';
import AddProduct from './components/Seller/AddProduct';
import UpdateProduct from './components/Seller/UpdateProduct';
import ProductOrder from "./components/Seller/ProductOrder";

import { AuthContext } from './contexts/auth.context';
import { ToastContainer } from 'react-toastify';

function App() {
  const [user, setUser] = useState(null);

  // Load user from localStorage
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  // Sync user to localStorage
  useEffect(() => {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      localStorage.removeItem('user');
    }
  }, [user]);

  return (
    <>
      <AuthContext.Provider value={{ user, setUser }}>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Seller Protected Routes */}
          <Route
            path="/seller-home"
            element={user ? <SellerHome /> : <Navigate to="/login" state={{ role: 'Seller' }} />}
          />
          <Route
            path="/about"
            element={user ? <About /> : <Navigate to="/login" state={{ role: 'Seller' }} />}
          />
          <Route
            path="/seller-contact"
            element={user ? <SellerContact /> : <Navigate to="/login" state={{ role: 'Seller' }} />}
          />
          <Route
            path="/add-product"
            element={user ? <AddProduct /> : <Navigate to="/login" state={{ role: 'Seller' }} />}
          />
          <Route
            path="/update-product"
            element={user ? <UpdateProduct /> : <Navigate to="/login" state={{ role: 'Seller' }} />}
          />
          <Route
            path="/order-details"
            element={user ? <ProductOrder /> : <Navigate to="/login" state={{ role: 'Seller' }} />}
          />
        </Routes>
      </AuthContext.Provider>

      <ToastContainer />
    </>
  );
}

export default App;
