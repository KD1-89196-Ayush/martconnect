import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import "../../StylingSheet/AddProduct.css";
import Header from "./Header";
import Footer from "./Footer";
import { updateProduct } from "../../services/updateProductService"; //

const UpdateProduct = () => {
  const location = useLocation();
  const existingProduct = location?.state?.product || {
    id: "", // important for backend updates
    name: "",
    description: "",
    unit: "",
    price: "",
    inStock: "",
    image: null,
    category: "",
  };

  const [product, setProduct] = useState(existingProduct);

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

    const result = await updateProduct(product);
    if (result.success) {
      alert("Product updated successfully!");
      console.log("Update result:", result.data || result.message);
    } else {
      alert("Failed to update product: " + result.error);
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100">
      <Header />

      <div
        className="flex-grow-1 d-flex justify-content-center align-items-center bg-light"
        style={{ minHeight: "calc(100vh - 130px)" }}
      >
        <div className="card shadow p-4 w-100" style={{ maxWidth: "600px" }}>
          <h2 className="text-center mb-4">
            Update {product.name}{" "}
            {product.category ? `(${product.category})` : ""}
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
                required
              />
            </div>

            <div className="mb-3">
              <label className="form-label me-3 fw-bold">In Stock:</label>
              <div className="form-check form-check-inline">
                <input
                  type="radio"
                  className="form-check-input"
                  name="inStock"
                  value="Yes"
                  checked={product.inStock === "Yes"}
                  onChange={handleInputChange}
                  required
                />
                <label className="form-check-label">Yes</label>
              </div>
              <div className="form-check form-check-inline">
                <input
                  type="radio"
                  className="form-check-input"
                  name="inStock"
                  value="No"
                  checked={product.inStock === "No"}
                  onChange={handleInputChange}
                />
                <label className="form-check-label">No</label>
              </div>
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
      </div>

      <Footer />
    </div>
  );
};

export default UpdateProduct;
