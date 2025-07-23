import React from "react";
import { Link, useNavigate } from "react-router-dom";

function Header() {
  const navigate = useNavigate();

  const handleLogout = () => {
    const confirmLogout = window.confirm("Are you sure you want to logout?");
    if (confirmLogout) {
      sessionStorage.removeItem("seller");
      navigate("/");
    }
  };

  return (
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
            <Link className="nav-link" to="/about">About</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/seller-contact">Contact</Link>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="#">Add Products</a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="#">Orders</a>
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
