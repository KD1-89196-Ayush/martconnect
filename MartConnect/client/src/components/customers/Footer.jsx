import React from "react";

function CustomerFooter() {
  return (
    <footer style={{ backgroundColor: '#f1f8e9' }} className="border-top py-3 mt-auto">
      <div className="container">
        <div className="text-center">
          <p className="mb-1">&copy; {new Date().getFullYear()} MartConnect</p>
          <p className="mb-0 text-muted small">Empowering Customers to Connect with Sellers</p>
        </div>
      </div>
    </footer>
  );
}

export default CustomerFooter; 