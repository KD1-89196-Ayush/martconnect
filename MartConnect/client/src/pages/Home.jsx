import React, { useEffect, useState } from "react";
import { getAllProducts } from "../services/productService";
import { toast } from "react-toastify";
import { useNavigate, Link } from "react-router-dom";

function PublicHeader() {
  return (
    <nav className="navbar navbar-expand-lg navbar-light" style={{ backgroundColor: '#e3f2fd' }}>
      <div className="container">
        <Link className="navbar-brand fw-bold text-primary" to="/">
          MartConnect
        </Link>
        <div className="ms-auto d-flex gap-2">
          <Link className="btn btn-outline-primary" to="/login">
            Login
          </Link>
          <Link className="btn btn-primary" to="/register">
            Register
          </Link>
        </div>
      </div>
    </nav>
  );
}

function PublicFooter() {
  return (
    <footer style={{ backgroundColor: '#f1f8e9' }} className="border-top py-3 mt-auto">
      <div className="container text-center">
        <p className="mb-1">&copy; {new Date().getFullYear()} MartConnect</p>
        <p className="mb-0 text-muted small">Empowering Customers to Connect with Sellers</p>
      </div>
    </footer>
  );
}

function Home() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("");
  const [categories, setCategories] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const loadProducts = async () => {
      try {
        const result = await getAllProducts();
        setProducts(result);
        const uniqueCategories = Array.from(new Set(result.map(p => 
          p.category || p.category_name || p.categoryName
        ).filter(cat => cat)));
        setCategories(uniqueCategories);
      } catch (err) {
        toast.error("Failed to load products");
      }
    };
    loadProducts();
  }, []);

  const filteredProducts = products.filter(product => {
    const matchesSearch = product.name.toLowerCase().includes(search.toLowerCase()) || 
                         (product.description && product.description.toLowerCase().includes(search.toLowerCase()));
    const productCategory = product.category || product.category_name || product.categoryName;
    const matchesCategory = category ? productCategory === category : true;
    return matchesSearch && matchesCategory;
  });

  const handleLoginRedirect = () => {
    toast.info("Please login to add items to cart");
    setTimeout(() => navigate("/login"), 1000);
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <PublicHeader />
      <main className="flex-grow-1">
        <div className="container py-4">
          <div className="text-center mb-4">
            <h2 className="mb-3">Welcome to MartConnect</h2>
            <p className="text-muted">Discover amazing products from trusted sellers</p>
          </div>

          <div className="row mb-4">
            <div className="col-md-6 mb-3">
              <input
                type="text"
                className="form-control"
                placeholder="Search products..."
                value={search}
                onChange={e => setSearch(e.target.value)}
              />
            </div>
            <div className="col-md-6 mb-3">
              <select
                className="form-select"
                value={category}
                onChange={e => setCategory(e.target.value)}
              >
                <option value="">All Categories</option>
                {categories.map(cat => (
                  <option key={cat} value={cat}>{cat}</option>
                ))}
              </select>
            </div>
          </div>

          <div className="row g-3">
            {filteredProducts.length > 0 ? (
              filteredProducts.map((product) => (
                <div className="col-lg-3 col-md-4 col-sm-6" key={product.product_id}>
                  <div className="card h-100">
                    <img
                      src={product.image_url}
                      alt={product.name}
                      className="card-img-top"
                      style={{ height: "180px", objectFit: "cover" }}
                    />
                    <div className="card-body p-3">
                      <h6 className="card-title mb-2">{product.name}</h6>
                      <p className="card-text small text-muted mb-2">{product.description}</p>
                      
                      <div className="mb-2">
                        <span className="badge bg-secondary">{product.category}</span>
                      </div>
                      
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <span className="fw-bold text-success">₹{product.price}</span>
                        <small className="text-muted">In Stock</small>
                      </div>
                      
                      <button
                        className="btn btn-primary btn-sm w-100"
                        onClick={handleLoginRedirect}
                      >
                        Add to Cart
                      </button>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="col-12 text-center py-5">
                <p className="text-muted">No products found.</p>
              </div>
            )}
          </div>
        </div>
      </main>
      <PublicFooter />
    </div>
  );
}

export default Home;
