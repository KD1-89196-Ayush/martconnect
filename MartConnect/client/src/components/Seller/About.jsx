import React from "react";
import Header from "./Header";
import Footer from "./Footer";

function About() {
  return (
    <div className="d-flex flex-column" style={{ minHeight: "100vh" }}>
      <Header />

      <main className="flex-grow-1 container py-5">
        <div className="row align-items-center">
          <div className="col-md-6 mb-4">
            <img
              src="https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80"
              alt="E-commerce Platform"
              className="img-fluid rounded shadow"
              style={{ maxHeight: "400px", width: "100%", objectFit: "cover" }}
            />
          </div>
          <div className="col-md-6">
            <h2 className="mb-4">About MartConnect</h2>
            <p className="mb-3">
              <strong>MartConnect</strong> is an innovative online e-commerce platform that connects sellers directly to
              customers across the country. Whether you're an established business or just starting out,
              MartConnect empowers you with the tools to showcase your products and manage your store effortlessly.
            </p>
            <p className="mb-3">
              Our vision is to simplify online selling for local sellers while offering customers a
              seamless, trusted shopping experience through our portal <strong>eMart</strong>.
            </p>
            <ul className="mb-3">
              <li>Easy product uploads for sellers</li>
              <li>Secure order management system</li>
              <li>Responsive design and user-friendly navigation</li>
              <li>Multi-category product listings</li>
            </ul>
            <p>
              Join us in the digital marketplace revolution. MartConnect – <em>Empowering Sellers, Connecting Communities.</em>
            </p>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default About;
