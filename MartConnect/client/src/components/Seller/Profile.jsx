import React from 'react';

function SellerProfile({ show, onClose }) {
  const seller = JSON.parse(sessionStorage.getItem('seller')) || JSON.parse(localStorage.getItem('seller')) || {};
  if (!show) return null;
  return (
    <div className="offcanvas offcanvas-end show" tabIndex="-1" style={{ visibility: 'visible', width: 400, background: '#fff', zIndex: 1050 }}>
      <div className="offcanvas-header" style={{ backgroundColor: '#add8e6', color: '#fff' }}>
        <h5 className="offcanvas-title">Seller Profile</h5>
        <button type="button" className="btn-close text-reset" aria-label="Close" onClick={onClose}></button>
      </div>
      <div className="offcanvas-body">
        <p><b>First Name:</b> {seller.first_name}</p>
        <p><b>Last Name:</b> {seller.last_name}</p>
        <p><b>Email:</b> {seller.email}</p>
        <p><b>Phone:</b> {seller.phone}</p>
        <p><b>Shop Name:</b> {seller.shop_name}</p>
        <p><b>Shop Address:</b> {seller.shop_address}</p>
      </div>
    </div>
  );
}

export default SellerProfile; 