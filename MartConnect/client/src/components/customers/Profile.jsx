import React from 'react';

function CustomerProfile({ show, onClose }) {
  const customer = JSON.parse(sessionStorage.getItem('customer')) || JSON.parse(localStorage.getItem('user')) || {};
  if (!show) return null;
  return (
    <div className="offcanvas offcanvas-end show" tabIndex="-1" style={{ visibility: 'visible', width: 400, background: '#fff', zIndex: 1050 }}>
      <div className="offcanvas-header" style={{ backgroundColor: '#add8e6', color: '#fff' }}>
        <h5 className="offcanvas-title">Customer Profile</h5>
        <button type="button" className="btn-close text-reset" aria-label="Close" onClick={onClose}></button>
      </div>
      <div className="offcanvas-body" style={{ color: '#000' }}>
        <p><b>First Name:</b> {customer.first_name}</p>
        <p><b>Last Name:</b> {customer.last_name}</p>
        <p><b>Email:</b> {customer.email}</p>
        <p><b>Phone:</b> {customer.phone}</p>
        <p><b>Address:</b> {customer.address}</p>
      </div>
    </div>
  );
}

export default CustomerProfile; 