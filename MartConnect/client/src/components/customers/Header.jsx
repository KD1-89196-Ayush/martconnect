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
      <header className="navbar navbar-expand-lg" style={{ backgroundColor: "#add8e6" }}>
        <div className="container justify-content-between">
          {/* Left: Brand */}
          <Link className="navbar-brand fw-bold" to="/customer-home">
            MartConnect
          </Link>

          {/* Right: Navigation + View Cart + Logout */}
          <ul className="navbar-nav flex-row gap-3 align-items-center">
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
            <li className="nav-item">
              <button className="btn btn-outline-primary btn-sm" onClick={() => setShowProfile(true)}>
                Profile
              </button>
            </li>
            <li className="nav-item">
              <button
                className="btn btn-outline-primary btn-sm mx-2"
                onClick={() => navigate("/cart")}
              >
                View Cart
              </button>
            </li>
            <li className="nav-item">
              <button
                onClick={handleLogout}
                className="btn btn-link nav-link text-danger fw-bold"
                style={{ textDecoration: "none" }}
              >
                Logout
              </button>
            </li>
          </ul>
        </div>
      </header>
      <CustomerProfile show={showProfile} onClose={() => setShowProfile(false)} />
    </>
  );
}

export default CustomerHeader; 