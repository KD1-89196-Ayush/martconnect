import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import SellerProfile from "./Profile";

function Header() {
  const [showProfile, setShowProfile] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    const confirmLogout = window.confirm("Are you sure you want to logout?");
    if (confirmLogout) {
      // Clear only session data, keep the sellerdata array intact
      localStorage.removeItem("seller");
      localStorage.removeItem("user");
      // Don't remove sellerdata - that contains all registered sellers
      navigate("/");

    }
  };

  return (
    <>
      <header className="navbar navbar-expand-lg" style={{ backgroundColor: "#add8e6" }}>
        <div className="container justify-content-between">
          {/* Left: Brand */}
          <Link className="navbar-brand fw-bold" to="/seller-home">
            MartConnect
          </Link>

          {/* Right: Navigation + Logout */}
          <ul className="navbar-nav flex-row gap-3 align-items-center">
            <li className="nav-item">
              <Link className="nav-link" to="/seller-home">Home</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/seller-dashboard">Dashboard</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/about">About</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/seller-contact">Contact</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/add-product">Add Products</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/order-details">Orders</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/category-management">Manage Categories</Link>
            </li>
            <li className="nav-item">
              <button className="btn btn-outline-primary btn-sm" onClick={() => setShowProfile(true)}>
                Profile
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
      <SellerProfile show={showProfile} onClose={() => setShowProfile(false)} />
    </>
  );
}

export default Header;
