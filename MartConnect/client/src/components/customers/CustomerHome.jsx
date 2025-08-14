import React, { useEffect, useState } from "react";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";
import { useNavigate } from "react-router-dom";
import { getAllProducts } from "../../services/productService";
import { toast } from "react-toastify";

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
        
        if (!result || !Array.isArray(result)) {
          toast.error("Failed to load products - invalid data format");
          return;
        }
        
        // Validate that products have required fields (handle both backend and frontend field names)
        const validProducts = result.filter(product => {
          // Check for backend field names first, then frontend fallback
          const hasSellerId = product.sellerId || product.seller_id;
          const hasProductId = product.productId || product.product_id;
          
          if (!hasSellerId) {
            return false;
          }
          if (!hasProductId) {
            return false;
          }
          return true;
        });
        
        // If no valid products found, use all products as fallback
        const productsToShow = validProducts.length > 0 ? validProducts : result;
        
        setProducts(productsToShow);
        // Extract unique categories (handle both backend and frontend field names)
        const uniqueCategories = Array.from(new Set(productsToShow.map(p => {
          // Backend returns 'category' as the category name
          return p.category || p.category_name || p.categoryName;
        }).filter(cat => cat))); // Filter out null/undefined values
        setCategories(uniqueCategories); //for category dropdown
      } catch (err) {
        toast.error("Failed to load products: " + err.message);
      }
    };
    loadProducts();
  }, []);

  // Map seller_id to shop_name for quick lookup (handle both frontend and backend sellers)
  const sellerIdToShopName = React.useMemo(() => {
    const map = {};
    
    // Backend sellers (from database)
    const backendSellers = {
      1: 'FreshMart Grocery',
      2: 'Organic Food Store', 
      3: 'Daily Needs Supermarket',
      4: 'Health Food Corner',
      5: 'Grocery Hub'
    };
    
    // Use backend mappings
    Object.assign(map, backendSellers);
    
    return map;
  }, []);

  const handleAddToCart = (product) => {
    if (product.stock <= 0) {
      toast.error("Product is out of stock!");
      return;
    }
    
    // Handle both backend and frontend field names
    const sellerId = product.sellerId || product.seller_id;
    const productId = product.productId || product.product_id;
    
    // Ensure seller_id is included in the cart item
    if (!sellerId) {
      toast.error("Product seller information is missing!");
      return;
    }
    
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    
    // Check if product already exists in cart
    const existingItemIndex = cart.findIndex(item => {
      const itemProductId = item.productId || item.product_id;
      return itemProductId === productId;
    });
    
    if (existingItemIndex !== -1) {
      // Update existing item quantity
      cart[existingItemIndex].quantity += 1;
    } else {
      // Add new item with all necessary fields
      cart.push({
        ...product,
        quantity: 1,
        seller_id: sellerId, // Ensure seller_id is explicitly included
        product_id: productId // Ensure product_id is explicitly included
      });
    }
    
    localStorage.setItem("cart", JSON.stringify(cart));
    toast.success("Added to cart!");
  };

  // Filter products by search and category
  const filteredProducts = products.filter(product => {
    const matchesSearch = product.name.toLowerCase().includes(search.toLowerCase()) || 
                         (product.description && product.description.toLowerCase().includes(search.toLowerCase()));
    
    // Handle both backend and frontend category field names
    const productCategory = product.category || product.category_name || product.categoryName;
    const matchesCategory = category ? productCategory === category : true;
    
    return matchesSearch && matchesCategory;
  });

  // Helper function to get stock status
  const getStockStatus = (stock) => {
    if (stock <= 0) return { text: "Out of Stock", color: "danger" };
    if (stock <= 5) return { text: `Only ${stock} left`, color: "warning" };
    return { text: `In Stock (${stock})`, color: "success" };
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <CustomerHeader />
      <main className="flex-grow-1 container py-4">
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
            filteredProducts.map((product) => {
              const stockStatus = getStockStatus(product.stock || 0);
              const isOutOfStock = product.stock <= 0;
              
              const productId = product.productId || product.product_id;
              const sellerId = product.sellerId || product.seller_id;
              const imageUrl = product.imageUrl || product.image_url;
              const category = product.category || product.category_name;
              
              return (
                <div className="col-lg-3 col-md-4 col-sm-6" key={productId}>
                  <div className="card h-100">
                    <img
                      src={imageUrl}
                      alt={product.name}
                      className="card-img-top"
                      style={{ height: "180px", objectFit: "cover" }}
                    />
                    <div className="card-body p-3">
                      <h6 className="card-title mb-2">{product.name}</h6>
                      <p className="card-text small text-muted mb-2">{product.description}</p>
                      
                      <div className="mb-2">
                        <span className={`badge bg-${stockStatus.color}`}>
                          {stockStatus.text}
                        </span>
                      </div>
                      
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <span className="badge bg-secondary">{category}</span>
                        <span className="fw-bold text-success">₹{product.price}</span>
                      </div>
                      
                      <button
                        className={`btn btn-sm w-100 ${isOutOfStock ? 'btn-secondary disabled' : 'btn-primary'}`}
                        onClick={() => handleAddToCart(product)}
                        disabled={isOutOfStock}
                      >
                        {isOutOfStock ? 'Out of Stock' : 'Add to Cart'}
                      </button>
                      
                      <small className="text-muted d-block mt-2">
                        Sold by: {sellerIdToShopName[sellerId] || "Unknown Seller"}
                      </small>
                    </div>
                  </div>
                </div>
              );
            })
          ) : (
            <div className="col-12 text-center py-5">
              <p className="text-muted">No products found.</p>
            </div>
          )}
        </div>
      </main>
      <CustomerFooter />
    </div>
  );
}

export default CustomerHome; 