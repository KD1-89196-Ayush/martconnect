import React from "react";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";

function CustomerAbout() {
  return (
    <div className="d-flex flex-column" style={{ minHeight: "100vh" }}>
      <CustomerHeader />

      <main className="flex-grow-1 container py-5">
        <div className="row align-items-center">
          <div className="col-md-6 mb-4">
            <img
              src="https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80"
              alt="Online Shopping"
              className="img-fluid rounded shadow"
              style={{ maxHeight: "400px", width: "100%", objectFit: "cover" }}
            />
          </div>
          <div className="col-md-6">
            <h2 className="mb-4">About MartConnect</h2>
            <p className="mb-3">
              <strong>MartConnect</strong> is your trusted online marketplace, connecting you directly with local sellers and a wide variety of products. Enjoy a seamless shopping experience with secure orders and responsive support.
            </p>
            <ul className="mb-3">
              <li>Browse and shop from multiple categories</li>
              <li>Track your orders easily</li>
              <li>Responsive design for all devices</li>
              <li>Trusted sellers and secure payments</li>
            </ul>
            <p>
              Join the MartConnect community and discover a better way to shop online!
            </p>
          </div>
        </div>
      </main>

      <CustomerFooter />
    </div>
  );
}

export default CustomerAbout; 