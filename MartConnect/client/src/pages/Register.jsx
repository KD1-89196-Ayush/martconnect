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
    <div className='container d-flex justify-content-center align-items-center' style={{ minHeight: '100vh' }}>
      <div className='card shadow-sm p-4 w-100' style={{ maxWidth: '500px' }}>
        <h3 className='text-center mb-4 text-primary'>Register</h3>
        <div className='mb-3 d-flex justify-content-center gap-2'>
          <button
            className={`btn btn-${userType === 'Customer' ? 'primary' : 'outline-primary'}`}
            onClick={() => setUserType('Customer')}
          >
            Customer
          </button>
          <button
            className={`btn btn-${userType === 'Seller' ? 'primary' : 'outline-primary'}`}
            onClick={() => setUserType('Seller')}
          >
            Seller
          </button>
        </div>
        <div className='row'>
          <div className='col-md-6 mb-3'>
            <label className='form-label'>First Name</label>
            <input
              onChange={e => setFirstName(e.target.value)}
              type='text'
              className='form-control'
              value={firstName}
            />
          </div>
          <div className='col-md-6 mb-3'>
            <label className='form-label'>Last Name</label>
            <input
              onChange={e => setLastName(e.target.value)}
              type='text'
              className='form-control'
              value={lastName}
            />
          </div>
        </div>
        <div className='mb-3'>
          <label className='form-label'>Email</label>
          <input
            onChange={e => setEmail(e.target.value)}
            type='email'
            className='form-control'
            value={email}
          />
        </div>
        <div className='mb-3'>
          <label className='form-label'>Phone Number</label>
          <input
            onChange={e => setPhone(e.target.value)}
            type='tel'
            className='form-control'
            value={phone}
          />
        </div>
        <div className='mb-3'>
          <label className='form-label'>Password</label>
          <input
            onChange={e => setPassword(e.target.value)}
            type='password'
            className='form-control'
            value={password}
          />
        </div>
        <div className='mb-3'>
          <label className='form-label'>Confirm Password</label>
          <input
            onChange={e => setConfirmPassword(e.target.value)}
            type='password'
            className='form-control'
            value={confirmPassword}
          />
        </div>
        {userType === 'Customer' && (
          <div className='mb-3'>
            <label className='form-label'>Address</label>
            <textarea
              className='form-control'
              value={address}
              onChange={e => setAddress(e.target.value)}
              rows={3}
              placeholder='Enter your full delivery address'
            />
          </div>
        )}
        {userType === 'Seller' && (
          <>
            <div className='mb-3'>
              <label className='form-label'>Shop Name</label>
              <input
                type='text'
                className='form-control'
                value={shopName}
                onChange={e => setShopName(e.target.value)}
                placeholder='Enter your shop name'
              />
            </div>
            <div className='mb-3'>
              <label className='form-label'>Shop Address</label>
              <textarea
                className='form-control'
                value={shopAddress}
                onChange={e => setShopAddress(e.target.value)}
                rows={2}
                placeholder='Enter your shop address'
              />
            </div>
          </>
        )}
        <div className='text-center mb-3'>
          <span>
            Already have an account?{' '}
            <button onClick={onBack} className='btn btn-link p-0 text-decoration-none'>
              Login here
            </button>
          </span>
        </div>
        <button onClick={onRegister} className='btn btn-success w-100'>
          Register
        </button>
      </div>
    </div>
  );
}

export default Register;
