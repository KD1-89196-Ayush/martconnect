import React, { useState } from "react";
import Header from "./Header";
import Footer from "./Footer";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../StylingSheet/AddProduct.css";
import { addProduct } from "../../services/addProduct";

const AddProduct = () => {
  const [category, setCategory] = useState("");
  const [product, setProduct] = useState({
    name: "",
    description: "",
    unit: "",
    price: "",
    inStock: "",
    image: null,
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProduct({ ...product, [name]: value });
  };

  const handleImageChange = (e) => {
    setProduct({ ...product, image: e.target.files[0] });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const productData = {
      ...product,
      category,
    };

    const result = await addProduct(productData);

    if (result.success) {
      alert(" Product added successfully!");
      setProduct({
        name: "",
        description: "",
        unit: "",
        price: "",
        inStock: "",
        image: null,
      });
      setCategory("");
    } else {
      alert(" Failed to add product: " + (result.error || "Unknown error"));
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <Header />

      <main className="container flex-grow-1 py-5 d-flex justify-content-center align-items-center">
        <div className="card shadow p-4 w-100" style={{ maxWidth: "600px" }}>
          <h2 className="text-center mb-4">Add a Product to the Shop</h2>

          {/* Category Buttons */}
          <div className="mb-4 text-center">
            <div className="btn-group flex-wrap" role="group">
              {["Grains", "Dairy Product", "Soaps", "Edible Oil"].map((cat) => (
                <button
                  key={cat}
                  type="button"
                  className={`btn btn-outline-primary m-1 ${
                    category === cat ? "active" : ""
                  }`}
                  onClick={() => setCategory(cat)}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>

          {/* Form */}
          {category && (
            <>
              <h5 className="text-center text-primary mb-3">
                {category} Product Details
              </h5>

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
                    required
                  />
                </div>

                <button type="submit" className="btn btn-success w-100">
                  Add Product
                </button>
              </form>
            </>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default AddProduct;
