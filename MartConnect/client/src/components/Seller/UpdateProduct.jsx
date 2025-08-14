import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../../StylingSheet/AddProduct.css";
import Header from "./Header";
import Footer from "./Footer";
import { updateProduct } from "../../services/updateProductService";

const UpdateProduct = () => {
  const location = useLocation();
  const existingProduct = location?.state?.product || {
    id: "",
    name: "",
    description: "",
    unit: "",
    price: "",
    stock: "",
    image: null,
    category: "",
  };

  const [product, setProduct] = useState(existingProduct);
  const navigate = useNavigate();
  const seller = JSON.parse(sessionStorage.getItem('seller')) || JSON.parse(localStorage.getItem('seller'));

  useEffect(() => {
    if (location?.state?.product) {
      setProduct(location.state.product);
    }
  }, [location]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProduct((prev) => ({ ...prev, [name]: value }));
  };

  const handleImageChange = (e) => {
    setProduct((prev) => ({ ...prev, image: e.target.files[0] }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Try different possible ID field names
    const productId = product.product_id || product.id || product.productId;
    
    if (!productId) {
      alert('Product ID missing. Cannot update. Please check the product data.');
      return;
    }
    
    const updatedProduct = {
      name: product.name,
      description: product.description,
      price: parseFloat(product.price) || 0,
      unit: product.unit,
      stock: parseInt(product.stock) || 0,
      imageUrl: product.imageUrl || product.image
    };
    
    const result = await updateProduct(productId, updatedProduct);
    if (result.success) {
      alert("Product updated successfully!");
      navigate('/seller-home');
    } else {
      alert("Failed to update product: " + result.error);
    }
  };

  // Get category name for display
  const getCategoryName = () => {
    if (product.category && typeof product.category === 'object' && product.category.name) {
      return product.category.name;
    }
    if (typeof product.category === 'string') {
      return product.category;
    }
    return "";
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <Header />
      <main className="container flex-grow-1 py-5 d-flex flex-column align-items-center">
        <button className="btn btn-secondary mb-3 align-self-start" onClick={() => navigate(-1)}>
          Go Back
        </button>
        <div className="card shadow p-4 w-100" style={{ maxWidth: "600px" }}>
          <h2 className="text-center mb-4">
            Update {product.name}{" "}
            {getCategoryName() ? `(${getCategoryName()})` : ""}
          </h2>

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <input
                type="text"
                name="name"
                className="form-control"
                placeholder="Product Name"
                value={product.name}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="mb-3">
              <textarea
                name="description"
                className="form-control"
                placeholder="Description"
                value={product.description}
                onChange={handleInputChange}
                rows={3}
                required
              ></textarea>
            </div>

            <div className="mb-3">
              <select
                name="unit"
                className="form-select"
                value={product.unit}
                onChange={handleInputChange}
                required
              >
                <option value="">Select Unit</option>
                <option value="Kilogram">Kilogram</option>
                <option value="Gram">Gram</option>
                <option value="Liter">Liter</option>
                <option value="Quantity">Quantity</option>
              </select>
            </div>

            <div className="mb-3">
              <input
                type="number"
                name="price"
                className="form-control"
                placeholder="Price"
                value={product.price}
                onChange={handleInputChange}
                min="0"
                step="0.01"
                required
              />
            </div>

            <div className="mb-3">
              <input
                type="number"
                name="stock"
                className="form-control"
                placeholder="Stock Quantity"
                value={product.stock}
                onChange={handleInputChange}
                min="0"
                required
              />
              <small className="form-text text-muted">
                Current stock quantity available for this product
              </small>
            </div>

            <div className="mb-3">
              <label className="form-label fw-bold">Upload Image:</label>
              <input
                type="file"
                className="form-control"
                accept="image/*"
                onChange={handleImageChange}
              />
            </div>

            <button type="submit" className="btn btn-primary w-100">
              Update Product
            </button>
          </form>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default UpdateProduct;
