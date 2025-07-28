import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Header from "./Header";
import Footer from "./Footer";
import {
  getProductsBySeller,
  deleteProductById,
} from "../../services/sellerProductService";
import { toast } from "react-toastify";
<<<<<<< HEAD:MartConnect/client/src/components/Seller/SellerHome.jsx
import { useNavigate } from "react-router-dom";
=======
import { useSearchParams } from "react-router-dom";

>>>>>>> c55a0062c6a92872129c2994e0a04aeb43482e32:MartConnect/client/src/pages/SellerHome.jsx

function SellerHome() {
  const location = useLocation();
  const [products, setProducts] = useState([]);
  const [searchParams] = useSearchParams();
  const searchQuery = searchParams.get("search")?.toLowerCase() || "";

  const filteredProducts = products.filter(product =>
    product.name.toLowerCase().includes(searchQuery)
  );


  // Get seller from navigation or session
  const seller = location.state?.seller ||
    JSON.parse(sessionStorage.getItem("seller")) || {
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
      } catch (err) {
        toast.error("Failed to load seller products");
      }
    };

    loadProducts();
  }, [seller.seller_id]);

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

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <Header />

      <div className="text-center py-3 bg-light">
        <h5 className="m-0">Welcome @{seller.shop_name}</h5>
      </div>

      <main className="flex-grow-1 container py-4">
        <h2 className="text-center mb-4">Your Products</h2>
        <div className="row row-cols-1 row-cols-md-3 g-4">
          {filteredProducts.length > 0 ? (
            filteredProducts.map((product) => (
              <div className="col" key={product.product_id}>
                <div className="card h-100 shadow-sm d-flex flex-column">
                  <img
                    src={product.image_url}
                    alt={product.name}
                    className="card-img-top"
                    style={{ height: "200px", objectFit: "cover" }}
                  />
                  <div className="card-body d-flex flex-column position-relative">
                    <h5 className="card-title">{product.name}</h5>
                    <p className="card-text">{product.description}</p>
                    <p className="fw-bold text-success">₹{product.price}</p>

                    <div className="d-flex justify-content-between align-items-end mt-auto">
                      <span className="badge bg-secondary">
                        {product.category}
                      </span>
                      <div className="d-flex gap-2">
                        <button
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => handleUpdate(product)}
                        >
                          Update
                        </button>

                        <button
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => handleDelete(product.product_id)}
                        >
                          Delete
                        </button>
                      </div>
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
