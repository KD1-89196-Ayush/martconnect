
import { Link, useNavigate } from "react-router-dom";
import React, { useState } from 'react';



function Header() {
  const navigate = useNavigate();

  const handleLogout = () => {
    const confirmLogout = window.confirm("Are you sure you want to logout?");
    if (confirmLogout) {
      sessionStorage.removeItem("seller");
      navigate("/");
    }
  };


  const [searchQuery, setSearchQuery] = useState("");

  const handleSearch = () => {
    if (searchQuery.trim()) {
      navigate(`/seller-home?search=${encodeURIComponent(searchQuery)}`);
    }
  };


  return (
    <header className="navbar navbar-expand-lg" style={{ backgroundColor: "#add8e6" }}>
      <div className="container justify-content-between">
        {/* Left: Brand */}
        <Link className="navbar-brand fw-bold" to="/seller-home">
          MartConnect
        </Link>

        <div className="d-flex align-items-center" style={{ flex: 1, justifyContent: "center" }}>
          <input
            type="text"
            className="form-control me-2"
            placeholder="Search products..."
            style={{ maxWidth: "300px" }}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <button className="btn btn-outline-dark" onClick={handleSearch}>
            Search
          </button>
        </div>


        {/* Right: Navigation + Logout */}
        <ul className="navbar-nav flex-row gap-3 align-items-center">
          <li className="nav-item">
            <Link className="nav-link" to="/seller-home">Home</Link>
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
  );
}

export default Header;
