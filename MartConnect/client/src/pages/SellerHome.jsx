import React from "react";
import { useLocation } from "react-router-dom";
import productData from "../data.json";
import Header from "../components/Seller/Header";
import Footer from "../components/Seller/Footer";

function SellerHome() {
  const location = useLocation();

  // Load seller from state or session
  const seller = location.state?.seller || JSON.parse(sessionStorage.getItem('seller')) || {
    seller_id: -1,
    first_name: "Seller",
    shop_name: "Unknown Shop"
  };

  // Save seller to sessionStorage if loaded from state
  if (location.state?.seller) {
    sessionStorage.setItem("seller", JSON.stringify(location.state.seller));
  }

  // Filter products belonging to this seller
  const sellerProducts = productData.filter(
    (product) => product.seller_id === seller.seller_id
  );

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <Header />

      <div className="text-center py-3 bg-light">
        <h5 className="m-0">Welcome @{seller.shop_name}</h5>
      </div>

      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">Your Products</h2>
        <div className="row row-cols-1 row-cols-md-3 g-4">
          {sellerProducts.length > 0 ? (
            sellerProducts.map((product) => (
              <div className="col" key={product.product_id}>
                <div className="card h-100 shadow-sm d-flex flex-column">
                  <img
                    src={product.image_url}
                    alt={product.name}
                    className="card-img-top"
                    style={{ height: "200px", objectFit: "cover" }}
                  />
                  <div className="card-body d-flex flex-column">
                    <h5 className="card-title">{product.name}</h5>
                    <p className="card-text">{product.description}</p>
                    <p className="fw-bold text-success">₹{product.price}</p>
                    <span className="badge bg-secondary mt-auto">{product.category}</span>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <p className="text-center">No products added yet.</p>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default SellerHome;
