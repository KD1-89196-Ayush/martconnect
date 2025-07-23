import React from "react";
import Header from "./Header";
import Footer from "./Footer";

function About() {
  return (
    <div className="d-flex flex-column" style={{ minHeight: "100vh" }}>
      <Header />

      <main className="flex-grow-1 container py-5">
        <h2 className="text-center mb-4">About MartConnect</h2>
        <p>
          <strong>MartConnect</strong> is an innovative online e-commerce platform that connects sellers directly to
          customers across the country. Whether you're an established business or just starting out,
          MartConnect empowers you with the tools to showcase your products and manage your store effortlessly.
        </p>
        <p>
          Our vision is to simplify online selling for local sellers while offering customers a
          seamless, trusted shopping experience through our portal <strong>eMart</strong>.
        </p>
        <ul>
          <li>Easy product uploads for sellers</li>
          <li>Secure order management system</li>
          <li>Responsive design and user-friendly navigation</li>
          <li>Multi-category product listings</li>
        </ul>
        <p>
          Join us in the digital marketplace revolution. MartConnect – <em>Empowering Sellers, Connecting Communities.</em>
        </p>
      </main>

      <Footer />
    </div>
  );
}

export default About;
