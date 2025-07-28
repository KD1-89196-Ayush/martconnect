import React, { useEffect, useState } from "react";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";
import { useNavigate } from "react-router-dom";
import { getAllProducts } from "../../services/productService";
import { toast } from "react-toastify";
import sellerData from "../../sellerdata.json";

function CustomerHome() {
  const [products, setProducts] = useState([]); //All product from db/json 
  const [search, setSearch] = useState(""); //search by product name
  const [category, setCategory] = useState(""); //filter category as per selected category in category dropdown
  const [categories, setCategories] = useState([]); //fetch categories from all product available product in setProduct
  const navigate = useNavigate(); //

  // Fetch all products and categories
  useEffect(() => {
    const loadProducts = async () => {
      try {
        const result = await getAllProducts();
        setProducts(result);
        // Extract unique categories
        const uniqueCategories = Array.from(new Set(result.map(p => p.category)));
        setCategories(uniqueCategories); //for category dropdown
      } catch (err) {
        toast.error("Failed to load products");
      }
    };
    loadProducts();
  }, []);

  // Map seller_id to shop_name for quick lookup
  const sellerIdToShopName = React.useMemo(() => {
    const map = {};
    sellerData.forEach(seller => {
      map[seller.seller_id] = seller.shop_name;
    });
    return map;
  }, []);

  const handleAddToCart = (product) => {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    cart.push({ ...product, quantity: 1 });
    localStorage.setItem("cart", JSON.stringify(cart));
    toast.success("Added to cart!");
  };

  // Filter products by search and category
  const filteredProducts = products.filter(product => {
    const matchesSearch = product.name.toLowerCase().includes(search.toLowerCase()) || product.description.toLowerCase().includes(search.toLowerCase());
    const matchesCategory = category ? product.category === category : true;
    return matchesSearch && matchesCategory;
  });

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <CustomerHeader />
      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">Shop Products</h2>
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
                    <button
                      className="btn btn-sm btn-outline-success mt-2 w-100"
                      onClick={() => handleAddToCart(product)}
                      style={{ fontSize: '0.9rem', padding: '2px 0' }}
                    >
                      Add to Cart
                    </button>
                    <div className="text-center mt-2">
                      <small className="text-muted">Sold by: {sellerIdToShopName[product.seller_id] || "Unknown Seller"}</small>
                    </div>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <p className="text-center">No products available.</p>
          )}
        </div>
      </main>
      <CustomerFooter />
    </div>
  );
}

export default CustomerHome; 