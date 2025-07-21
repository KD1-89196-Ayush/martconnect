import React from "react";
import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();

  const handleLogin = (role) => {
    // pass the selected role to the login page
    navigate("/login", { state: { role } });
  };

  return (
    <div className="container text-center mt-5">
      <h1 className="mb-4">Welcome to MartConnect</h1>
      <h6 className="mb-4">Let's Connect!!</h6>
      <p className="lead mb-4">Please choose your role to login:</p>

      <div className="d-flex flex-column gap-3 align-items-center">
        <button
          className="btn btn-success btn-lg w-50"
          onClick={() => handleLogin("Admin")}
        >
          Admin Login
        </button>
        <button
          className="btn btn-primary btn-lg w-50"
          onClick={() => handleLogin("Customer")}
        >
          Customer Login
        </button>
        <button
          className="btn btn-danger btn-lg w-50"
          onClick={() => handleLogin("Seller")}
        >
          Seller Login
        </button>
      </div>
    </div>
  );
}

export default Home;
