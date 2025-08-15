import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import CustomerProfile from "./Profile";

function CustomerHeader() {
  const [showProfile, setShowProfile] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    const confirmLogout = window.confirm("Are you sure you want to logout?");
    if (confirmLogout) {
      sessionStorage.removeItem("customer");
      navigate("/");
    }
  };

  return (
    <>
      <nav className="navbar navbar-expand-lg navbar-light" style={{ backgroundColor: '#e3f2fd' }}>
        <div className="container">
          <Link className="navbar-brand fw-bold text-primary" to="/customer-home">
            MartConnect
          </Link>

          <button 
            className="navbar-toggler" 
            type="button" 
            data-bs-toggle="collapse" 
            data-bs-target="#navbarNav"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav me-auto">
              <li className="nav-item">
                <Link className="nav-link" to="/customer-home">Home</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/customer-about">About</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/customer-contact">Contact</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/customer-orders">My Orders</Link>
              </li>
            </ul>

            <div className="d-flex gap-2">
              <button 
                className="btn btn-outline-primary btn-sm" 
                onClick={() => setShowProfile(true)}
              >
                Profile
              </button>
              <button
                className="btn btn-warning btn-sm"
                onClick={() => navigate("/cart")}
              >
                Cart
              </button>
              <button
                onClick={handleLogout}
                className="btn btn-outline-danger btn-sm"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>
      <CustomerProfile show={showProfile} onClose={() => setShowProfile(false)} />
    </>
  );
}

export default CustomerHeader; 