import React, { useState, useEffect } from "react";
import "../StylingSheet/AddProduct.css"; // Using the same CSS file


const UpdateProduct = ({ location }) => {
  const existingProduct = location?.state?.product || {
    name: "",
    description: "",
    unit: "",
    price: "",
    inStock: "",
    image: null,
    category: "", // make sure this exists
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

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Updated product:", product);
    // You can send this updated data to your backend
  };

  return (
    <div className="container-fluid bg-light min-vh-100 d-flex justify-content-center align-items-center">
      <div className="card shadow p-4 w-100" style={{ maxWidth: "600px", marginLeft: "90px" }}>
        
        {/* ✅ Updated Heading */}
        <h2 className="text-center mb-4">
          Update {product.name} {product.category ? `(${product.category})` : ""}
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
            <label className="form-label me-3">In Stock:</label>
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
            <label className="form-label">Upload Image:</label>
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
  );
};

export default UpdateProduct;
