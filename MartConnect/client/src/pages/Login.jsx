import React, { useContext, useState } from 'react';
import { toast } from 'react-toastify';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { loginUser } from '../services/user';
import { AuthContext } from '../contexts/auth.context';
import sellerData from '../sellerdata.json';

function Login() {
  const { setUser } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const role = location.state?.role || 'User';

  const onLogin = async () => {
    if (email.trim() === '') {
      toast.warn('Please enter email');
    } else if (password.trim() === '') {
      toast.warn('Please enter password');
    } else {
      if (role === 'Seller') {
        const seller = sellerData.find(
          (s) => s.email === email && s.password === password
        );

        if (seller) {
          const sellerUser = {
            role: 'Seller',
            seller_id: seller.seller_id,
            first_name: seller.first_name,
            last_name: seller.last_name,
            shop_name: seller.shop_name,
            email: seller.email,
          };

          setUser(sellerUser);
          localStorage.setItem('user', JSON.stringify(sellerUser));
          toast.success(`Welcome ${seller.first_name}`);
          navigate('/seller-home', { state: { seller: sellerUser } });
        } else {
          toast.error('Invalid seller credentials');
        }
      } else {
        const result = await loginUser(email, password);
        if (!result) {
          toast.error('Error while login');
        } else if (result.status === 'success') {
          const { firstName, lastName, token } = result.data;
          const customerUser = {
            role: 'User',
            first_name: firstName,
            last_name: lastName,
            token,
          };
          setUser(customerUser);
          localStorage.setItem('user', JSON.stringify(customerUser));
          toast.success('Welcome to application');
          navigate('/home/properties');
        } else {
          toast.error('Invalid email or password');
        }
      }
    }
  };

  return (
    <div className='container d-flex justify-content-center align-items-center' style={{ minHeight: '100vh' }}>
      <div className='card shadow-sm p-4 w-100' style={{ maxWidth: '400px' }}>
        <h3 className='text-center mb-4 text-primary'>{role} Login</h3>

        <div className='mb-3'>
          <label className='form-label'>Email</label>
          <input
            onChange={(e) => setEmail(e.target.value)}
            type='email'
            className='form-control'
            placeholder='username@test.com'
          />
        </div>

        <div className='mb-3'>
          <label className='form-label'>Password</label>
          <input
            onChange={(e) => setPassword(e.target.value)}
            type='password'
            className='form-control'
            placeholder='******'
          />
        </div>

        <div className='mb-3 text-center'>
          <span>
            Don’t have an account?{' '}
            <Link to='/register' className='text-decoration-none'>
              Register here
            </Link>
          </span>
        </div>

        <button onClick={onLogin} className='btn btn-success w-100'>
          Login
        </button>
      </div>
    </div>
  );
}

export default Login;
