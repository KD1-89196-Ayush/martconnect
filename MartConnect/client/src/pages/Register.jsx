import React, { useState } from 'react';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../services/user';

function Register() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const navigate = useNavigate();

  const onBack = () => {
    navigate(-1);
  };

  const onRegister = async () => {
    if (firstName.length === 0) {
      toast.warn('Please enter first name');
    } else if (lastName.length === 0) {
      toast.warn('Please enter last name');
    } else if (email.length === 0) {
      toast.warn('Please enter email');
    } else if (phone.length === 0) {
      toast.warn('Please enter phone number');
    } else if (password.length === 0) {
      toast.warn('Please enter password');
    } else if (confirmPassword.length === 0) {
      toast.warn('Please confirm password');
    } else if (password !== confirmPassword) {
      toast.warn('Passwords do not match');
    } else {
      const result = await registerUser(
        firstName,
        lastName,
        email,
        phone,
        password
      );
      if (!result) {
        toast.error('Error while registering the user');
      } else if (result.status === 'success') {
        toast.success('Successfully registered');
        navigate(-1);
      } else {
        toast.error('Error while registering the user');
      }
    }
  };

  return (
    <div className='container d-flex justify-content-center align-items-center' style={{ minHeight: '100vh' }}>
      <div className='card shadow-sm p-4 w-100' style={{ maxWidth: '500px' }}>
        <h3 className='text-center mb-4 text-primary'>Register</h3>

        <div className='row'>
          <div className='col-md-6 mb-3'>
            <label className='form-label'>First Name</label>
            <input
              onChange={(e) => setFirstName(e.target.value)}
              type='text'
              className='form-control'
              value={firstName}
            />
          </div>
          <div className='col-md-6 mb-3'>
            <label className='form-label'>Last Name</label>
            <input
              onChange={(e) => setLastName(e.target.value)}
              type='text'
              className='form-control'
              value={lastName}
            />
          </div>
        </div>

        <div className='mb-3'>
          <label className='form-label'>Email</label>
          <input
            onChange={(e) => setEmail(e.target.value)}
            type='email'
            className='form-control'
            value={email}
          />
        </div>

        <div className='mb-3'>
          <label className='form-label'>Phone Number</label>
          <input
            onChange={(e) => setPhone(e.target.value)}
            type='tel'
            className='form-control'
            value={phone}
          />
        </div>

        <div className='mb-3'>
          <label className='form-label'>Password</label>
          <input
            onChange={(e) => setPassword(e.target.value)}
            type='password'
            className='form-control'
            value={password}
          />
        </div>

        <div className='mb-3'>
          <label className='form-label'>Confirm Password</label>
          <input
            onChange={(e) => setConfirmPassword(e.target.value)}
            type='password'
            className='form-control'
            value={confirmPassword}
          />
        </div>

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
