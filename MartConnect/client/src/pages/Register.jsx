import React, { useState } from 'react';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../services/user';
import axios from "axios";

function Register() {
  const [userType, setUserType] = useState('Customer');
  // Common fields
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  // Customer fields
  const [address, setAddress] = useState('');
  // Seller fields
  const [shopName, setShopName] = useState('');
  const [shopAddress, setShopAddress] = useState('');
  const navigate = useNavigate();

  const onBack = () => navigate(-1);

  const onRegister = async () => {
    if (!firstName) return toast.warn('Please enter first name');
    if (!lastName) return toast.warn('Please enter last name');
    if (!email) return toast.warn('Please enter email');
    if (!phone) return toast.warn('Please enter phone number');
    if (!password) return toast.warn('Please enter password');
    if (!confirmPassword) return toast.warn('Please confirm password');
    if (password !== confirmPassword) return toast.warn('Passwords do not match');
    if (userType === 'Customer' && !address) return toast.warn('Please enter your address');
    if (userType === 'Seller' && !shopName) return toast.warn('Please enter your shop name');
    if (userType === 'Seller' && !shopAddress) return toast.warn('Please enter your shop address');

    const result = await registerUser(
      firstName,
      lastName,
      email,
      phone,
      password,
      userType,
      address,
      shopName,
      shopAddress
    );
    if (!result) {
      toast.error('Error while registering the user');
    } else if (result.status === 'success') {
      toast.success('Successfully registered');
      navigate('/login', { state: { role: userType } });
    } else {
      toast.error(result.error || 'Error while registering the user');
    }
  };

  return (
    <div className="min-vh-100 d-flex align-items-center justify-content-center bg-light">
      <div className="card shadow-sm" style={{ maxWidth: '500px', width: '100%' }}>
        <div className="card-body p-4">
          <div className="text-center mb-4">
            <h4 className="text-primary mb-2">Create Account</h4>
            <p className="text-muted">Join MartConnect today</p>
          </div>

          <div className="mb-3">
            <label className="form-label">Account Type</label>
            <div className="d-flex gap-2">
              <button
                className={`btn flex-fill ${userType === 'Customer' ? 'btn-primary' : 'btn-outline-primary'}`}
                onClick={() => setUserType('Customer')}
              >
                Customer
              </button>
              <button
                className={`btn flex-fill ${userType === 'Seller' ? 'btn-primary' : 'btn-outline-primary'}`}
                onClick={() => setUserType('Seller')}
              >
                Seller
              </button>
            </div>
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label">First Name</label>
              <input
                onChange={e => setFirstName(e.target.value)}
                type="text"
                className="form-control"
                value={firstName}
                placeholder="Enter first name"
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Last Name</label>
              <input
                onChange={e => setLastName(e.target.value)}
                type="text"
                className="form-control"
                value={lastName}
                placeholder="Enter last name"
              />
            </div>
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label">Email</label>
              <input
                onChange={e => setEmail(e.target.value)}
                type="email"
                className="form-control"
                value={email}
                placeholder="Enter email"
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Phone</label>
              <input
                onChange={e => setPhone(e.target.value)}
                type="tel"
                className="form-control"
                value={phone}
                placeholder="Enter phone"
              />
            </div>
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label">Password</label>
              <input
                onChange={e => setPassword(e.target.value)}
                type="password"
                className="form-control"
                value={password}
                placeholder="Enter password"
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Confirm Password</label>
              <input
                onChange={e => setConfirmPassword(e.target.value)}
                type="password"
                className="form-control"
                value={confirmPassword}
                placeholder="Confirm password"
              />
            </div>
          </div>

          {userType === 'Customer' && (
            <div className="mb-3">
              <label className="form-label">Address</label>
              <textarea
                onChange={e => setAddress(e.target.value)}
                className="form-control"
                value={address}
                placeholder="Enter your address"
                rows="3"
              ></textarea>
            </div>
          )}

          {userType === 'Seller' && (
            <div className="row g-3 mb-3">
              <div className="col-md-6">
                <label className="form-label">Shop Name</label>
                <input
                  onChange={e => setShopName(e.target.value)}
                  type="text"
                  className="form-control"
                  value={shopName}
                  placeholder="Enter shop name"
                />
              </div>
              <div className="col-md-6">
                <label className="form-label">Shop Address</label>
                <input
                  onChange={e => setShopAddress(e.target.value)}
                  type="text"
                  className="form-control"
                  value={shopAddress}
                  placeholder="Enter shop address"
                />
              </div>
            </div>
          )}

          <button
            onClick={onRegister}
            className="btn btn-primary w-100 mb-3"
          >
            Create Account
          </button>

          <div className="text-center">
            <p className="text-muted mb-0">
              Already have an account?{' '}
              <button 
                onClick={()=>navigate("/login")}
                className="text-primary text-decoration-none border-0 bg-transparent"
              >
                Sign in here
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;
