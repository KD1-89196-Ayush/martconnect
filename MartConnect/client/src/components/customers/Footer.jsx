import React from "react";

function CustomerFooter() {
  return (
    <footer className="text-center py-3" style={{ backgroundColor: "#add8e6" }}>
      <div className="container">
        <p className="mb-1">&copy; {new Date().getFullYear()} MartConnect</p>
        <p className="mb-0">Empowering Customers to Connect with Sellers</p>
      </div>
    </footer>
  );
}

export default CustomerFooter; 