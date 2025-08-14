import React, { useState, useEffect } from "react";
import Header from "./Header";
import Footer from "./Footer";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../StylingSheet/AddProduct.css";
import { addProduct } from "../../services/addProduct";
import { fetchCategories } from "../../services/categoryService";
import { useNavigate } from "react-router-dom";

const AddProduct = () => {
  const [category, setCategory] = useState("");
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [product, setProduct] = useState({
    name: "",
    description: "",
    unit: "",
    price: "",
    stock: "",
    image: null,
  });

  const navigate = useNavigate();
  const seller = JSON.parse(sessionStorage.getItem('seller')) || JSON.parse(localStorage.getItem('seller'));

  // Fetch categories on component mount
  useEffect(() => {
    const loadCategories = async () => {
      try {
        const categoriesData = await fetchCategories();
        setCategories(categoriesData);
      } catch (error) {
        // Failed to load categories
      } finally {
        setLoading(false);
      }
    };
    loadCategories();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProduct({ ...product, [name]: value });
  };

  const handleImageChange = (e) => {
    setProduct({ ...product, image: e.target.files[0] });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Find the category ID from the selected category name
    const selectedCategory = categories.find(cat => cat.name === category);
    if (!selectedCategory) {
      alert("Please select a valid category");
      return;
    }

    const productData = {
      ...product,
      category: {
        categoryId: selectedCategory.categoryId
      },
      seller: {
        sellerId: seller?.seller_id
      },
      stock: parseInt(product.stock) || 0
    };

    const result = await addProduct(productData);

    if (result.success) {
      alert("Product added successfully!");
      setProduct({
        name: "",
        description: "",
        unit: "",
        price: "",
        stock: "",
        image: null,
      });
      setCategory("");
    } else {
      alert("Failed to add product: " + (result.error || "Unknown error"));
    }
  };

  if (loading) {
    return (
      <div className="d-flex flex-column min-vh-100 bg-light">
        <Header />
        <main className="container flex-grow-1 py-5 d-flex justify-content-center align-items-center">
          <div className="text-center">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
            <p className="mt-3">Loading categories...</p>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <Header />

      <main className="container flex-grow-1 py-5 d-flex flex-column align-items-center">
        <button className="btn btn-secondary mb-3 align-self-start" onClick={() => navigate(-1)}>
          Go Back
        </button>
        <div className="card shadow p-4 w-100" style={{ maxWidth: "600px" }}>
          <h2 className="text-center mb-4">Add a Product to the Shop</h2>

          {/* Category Buttons */}
          <div className="mb-4 text-center">
            <div className="btn-group flex-wrap" role="group">
              {categories.map((cat) => (
                <button
                  key={cat.categoryId}
                  type="button"
                  className={`btn btn-outline-primary m-1 ${
                    category === cat.name ? "active" : ""
                  }`}
                  onClick={() => setCategory(cat.name)}
                >
                  {cat.name}
                </button>
              ))}
            </div>
            
            {/* Category Dropdown Alternative */}
            <div className="mt-3">
              <select
                className="form-select"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                style={{ maxWidth: "300px", margin: "0 auto" }}
              >
                <option value="">Select Category</option>
                {categories.map((cat) => (
                  <option key={cat.categoryId} value={cat.name}>
                    {cat.name}
                  </option>
                ))}
              </select>
              <small className="form-text text-muted">
                Or use the dropdown above to select a category
              </small>
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
                    Enter the initial stock quantity available for this product
                  </small>
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
