import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchProducts, addToCart, buyNow } from "../services/productService";

function Home() {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);

  useEffect(() => {
    // Fetch products from backend
    fetchProducts()
      .then(setProducts)
      .catch((err) => console.error("Failed to load products", err));
  }, []);

  const handleLogin = (role) => {
    navigate("/login", { state: { role } });
  };

  const handleBuyNow = (productId) => {
    navigate("/login", { state: { role: "Customer", action: "buy", productId } });
    // If logged in: buyNow(productId, userId)
  };

  const handleAddToCart = (productId) => {
    navigate("/login", { state: { role: "Customer", action: "cart", productId } });
    // If logged in: addToCart(productId, userId)
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      {/* Header */}
      <header className="navbar navbar-expand-lg" style={{ backgroundColor: "#add8e6" }}>
        <div className="container">
          <a className="navbar-brand fw-bold" href="/">
            MartConnect
          </a>

          <div className="ms-auto d-flex gap-2">
            <button className="btn btn-dark btn-sm" onClick={() => handleLogin("Admin")}>Admin</button>
            <button className="btn btn-dark btn-sm" onClick={() => handleLogin("Customer")}>Customer</button>
            <button className="btn btn-dark btn-sm" onClick={() => handleLogin("Seller")}>Seller</button>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">Our Products</h2>
        <div className="row row-cols-1 row-cols-md-3 g-4">
          {products.map((product, index) => (
            <div className="col" key={index}>
              <div className="card h-100 shadow-sm d-flex flex-column">
                <img
                  src={product.image_url}
                  className="card-img-top"
                  alt={product.name}
                  style={{ height: "200px", objectFit: "cover" }}
                />
                <div className="card-body d-flex flex-column">
                  <h5 className="card-title">{product.name}</h5>
                  <p className="card-text">{product.description}</p>
                  <p className="card-text fw-bold text-success">₹{product.price}</p>

                  <div className="mt-auto d-flex justify-content-between align-items-center">
                    <span className="badge bg-secondary">{product.category}</span>
                    <div className="d-flex gap-2">
                      <button className="btn btn-primary btn-sm" onClick={() => handleAddToCart(product.id)}>
                        Add to Cart
                      </button>
                      <button className="btn btn-success btn-sm" onClick={() => handleBuyNow(product.id)}>
                        Buy Now
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </main>

      {/* Footer */}
      <footer className="text-center py-3" style={{ backgroundColor: "#add8e6" }}>
        <div className="container">
          <p className="mb-1">&copy; {new Date().getFullYear()} MartConnect</p>
          <p className="mb-0">Your one-stop solution for connecting Sellers and Customers</p>
        </div>
      </footer>
    </div>
  );
}

export default Home;
