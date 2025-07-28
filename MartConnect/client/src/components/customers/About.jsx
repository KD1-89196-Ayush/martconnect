import React from "react";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";

function CustomerAbout() {
  return (
    <div className="d-flex flex-column" style={{ minHeight: "100vh" }}>
      <CustomerHeader />

      <main className="flex-grow-1 container py-5">
        <h2 className="text-center mb-4">About MartConnect</h2>
        <p>
          <strong>MartConnect</strong> is your trusted online marketplace, connecting you directly with local sellers and a wide variety of products. Enjoy a seamless shopping experience with secure orders and responsive support.
        </p>
        <ul>
          <li>Browse and shop from multiple categories</li>
          <li>Track your orders easily</li>
          <li>Responsive design for all devices</li>
          <li>Trusted sellers and secure payments</li>
        </ul>
        <p>
          Join the MartConnect community and discover a better way to shop online!
        </p>
      </main>

      <CustomerFooter />
    </div>
  );
}

export default CustomerAbout; 