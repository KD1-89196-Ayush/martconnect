import React, { useState } from "react";
import { toast } from "react-toastify";
import CustomerHeader from "./Header";
import CustomerFooter from "./Footer";

function CustomerContact() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    if (name.trim() === "" || email.trim() === "" || message.trim() === "") {
      toast.warn("Please fill in all fields.");
    } else {
      toast.success("Message sent successfully!");
      setName("");
      setEmail("");
      setMessage("");
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <CustomerHeader />

      <main className="container my-5 flex-grow-1">
        <h2 className="text-center mb-4">Contact Support</h2>

        <div className="row">
          <div className="col-md-6">
            <form onSubmit={handleSubmit} className="p-4 shadow-sm bg-white rounded">
              <div className="mb-3">
                <label className="form-label">Full Name</label>
                <input
                  type="text"
                  className="form-control"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Enter your name"
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Email address</label>
                <input
                  type="email"
                  className="form-control"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Enter your email"
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Your Message</label>
                <textarea
                  className="form-control"
                  rows="4"
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  placeholder="Write your message..."
                ></textarea>
              </div>

              <button type="submit" className="btn btn-primary w-100">
                Send Message
              </button>
            </form>
          </div>

          <div className="col-md-6 d-flex flex-column justify-content-center p-4">
            <h5>Need Help?</h5>
            <p>
              For any questions about your orders, account, or shopping experience, contact us using the form.
            </p>
            <p>
              Our support team is available Monday to Saturday, 10:00 AM – 6:00 PM IST.
            </p>
            <p><strong>Email:</strong> support@martconnect.in</p>
            <p><strong>Phone:</strong> +91 98765 43210</p>
            <p><strong>Address:</strong> MartConnect Customer Support Center, Mumbai, India</p>
          </div>
        </div>
      </main>

      <CustomerFooter />
    </div>
  );
}

export default CustomerContact; 