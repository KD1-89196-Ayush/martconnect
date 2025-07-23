import React from "react";

function Footer() {
  return (
    <footer className="text-center py-3" style={{ backgroundColor: "#add8e6" }}>
      <div className="container">
        <p className="mb-1">&copy; {new Date().getFullYear()} MartConnect</p>
        <p className="mb-0">Empowering Sellers to Connect with Customers</p>
      </div>
    </footer>
  );
}

export default Footer;
