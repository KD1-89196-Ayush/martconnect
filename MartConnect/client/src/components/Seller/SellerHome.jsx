import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Header from "./Header";
import Footer from "./Footer";
import {
  getProductsBySeller,
  deleteProductById,
} from "../../services/sellerProductService";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

function SellerHome() {
  const location = useLocation();
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("");
  const [categories, setCategories] = useState([]);

  // Get seller from navigation or session
  const seller = location.state?.seller ||
    JSON.parse(sessionStorage.getItem("seller")) ||
    JSON.parse(localStorage.getItem("seller")) || {
      seller_id: -1,
      first_name: "Seller",
      shop_name: "Unknown Shop",
    };

  // Save seller to sessionStorage if passed from login
  useEffect(() => {
    if (location.state?.seller) {
      sessionStorage.setItem("seller", JSON.stringify(location.state.seller));
    }
  }, [location.state?.seller]);

  // Fetch products for this seller
  useEffect(() => {
    const loadProducts = async () => {
      try {
        const result = await getProductsBySeller(seller.seller_id);
        setProducts(result);
        // Extract unique categories for this seller (handle backend format)
        const uniqueCategories = Array.from(new Set(result.map(p => 
          p.category || p.category_name || p.categoryName
        ).filter(cat => cat)));
        setCategories(uniqueCategories);
      } catch (err) {
        toast.error("Failed to load seller products");
      }
    };
    loadProducts();
  }, [seller.seller_id]);

  // Filter products by search and category
  const filteredProducts = products.filter(product => {
    const matchesSearch = product.name.toLowerCase().includes(search.toLowerCase()) || 
                         (product.description && product.description.toLowerCase().includes(search.toLowerCase()));
    // Handle both backend and frontend category field names
    const productCategory = product.category || product.category_name || product.categoryName;
    const matchesCategory = category ? productCategory === category : true;
    return matchesSearch && matchesCategory;
  });

  const navigate = useNavigate();

  const handleUpdate = (product) => {
    navigate("/update-product", { state: { product } }); // ✅ navigate with state
  };

  const handleDelete = async (productId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this product?"
    );
    if (!confirmDelete) return;

    try {
      const result = await deleteProductById(productId);
      if (result && result.status === "success") {
        toast.success("Product deleted successfully");
        setProducts(products.filter((p) => p.product_id !== productId));
      } else {
        toast.error("Failed to delete product");
      }
    } catch (error) {
      toast.error("Error deleting product");
    }
  };

  // Map seller_id to shop_name for quick lookup
  const sellerIdToShopName = React.useMemo(() => {
    const map = {};
    // Use the current seller's shop name
    map[seller.seller_id] = seller.shop_name;
    return map;
  }, [seller.seller_id, seller.shop_name]);

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <Header />

      <div className="text-center py-3 bg-light">
        <h5 className="m-0">Welcome <b>{seller.shop_name}</b></h5>
      </div>

      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">Your Products</h2>
        
        <div className="d-flex flex-wrap justify-content-between align-items-center mb-4 gap-2">
          <input
            type="text"
            className="form-control"
            style={{ maxWidth: 300 }}
            placeholder="Search products..."
            value={search}
            onChange={e => setSearch(e.target.value)}
          />
          <div className="dropdown">
            <button className="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
              {category || "Filter by Category"}
            </button>
            <ul className="dropdown-menu">
              <li><button className="dropdown-item" onClick={() => setCategory("")}>All</button></li>
              {categories.map(cat => (
                <li key={cat}><button className="dropdown-item" onClick={() => setCategory(cat)}>{cat}</button></li>
              ))}
            </ul>
          </div>
        </div>
        <div className="row row-cols-2 row-cols-md-5 g-2 justify-content-center">
          {filteredProducts.length > 0 ? (
            filteredProducts.map((product) => (
              <div className="col d-flex justify-content-center" key={product.product_id}>
                <div className="card h-100 shadow border-primary rounded-4" style={{ minHeight: 320, maxWidth: 240, width: '100%' }}>
                  <img
                    src={product.image_url}
                    alt={product.name}
                    className="card-img-top p-1 rounded-3"
                    style={{ height: "120px", objectFit: "contain", background: "#f8f9fa" }}
                  />
                  <div className="card-body d-flex flex-column p-1">
                    <h6 className="card-title text-truncate mb-1" style={{ fontSize: '1rem' }}>{product.name}</h6>
                    <p className="card-text small text-muted mb-1" style={{ minHeight: 24, fontSize: '0.85rem' }}>{product.description}</p>
                    <div className="d-flex justify-content-between align-items-center mt-auto">
                      <span className="badge bg-secondary" style={{ fontSize: '0.75rem' }}>{product.category}</span>
                      <span className="fw-bold text-success" style={{ fontSize: '0.95rem' }}>₹{product.price}</span>
                    </div>
                    <div className="d-flex gap-2 mt-2">
                      <button
                        className="btn btn-sm btn-outline-primary w-50"
                        onClick={() => handleUpdate(product)}
                        style={{ fontSize: '0.9rem', padding: '2px 0' }}
                      >
                        Update
                      </button>
                      <button
                        className="btn btn-sm btn-outline-danger w-50"
                        onClick={() => handleDelete(product.product_id)}
                        style={{ fontSize: '0.9rem', padding: '2px 0' }}
                      >
                        Delete
                      </button>
                    </div>
                    <div className="text-center mt-2">
                      <small className="text-muted">Sold by: {sellerIdToShopName[product.seller_id] || "Unknown Seller"}</small>
                    </div>
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
