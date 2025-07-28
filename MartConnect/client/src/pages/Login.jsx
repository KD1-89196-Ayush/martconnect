import React, { useContext, useState } from 'react';
import { toast } from 'react-toastify';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { loginUser } from '../services/user';
import { AuthContext } from '../contexts/auth.context';
import sellerData from '../sellerdata.json';
import customerData from '../customers.json';

function Login() {
  const { setUser } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const [selectedRole, setSelectedRole] = useState(location.state?.role || 'User');
  console.log('LOGIN DEBUG: Role from location state:', location.state?.role);
  console.log('LOGIN DEBUG: Final role being used:', selectedRole);

  const onLogin = async () => {
    if (email.trim() === '') {
      toast.warn('Please enter email');
    } else if (password.trim() === '') {
      toast.warn('Please enter password');
    } else {
      const result = await loginUser(email, password, selectedRole);
      if (!result) {
        toast.error('Error while login');
      } else if (result.status === 'success') {
        if (selectedRole === 'Seller') {
          const { firstName, lastName, token, seller_id, email: userEmail, shop_name, shop_address } = result.data;
          const sellerUser = {
            role: 'Seller',
            seller_id,
            first_name: firstName,
            last_name: lastName,
            token,
            email: userEmail,
            shop_name,
            shop_address
          };
          setUser(sellerUser);
          localStorage.setItem('user', JSON.stringify(sellerUser));
          localStorage.setItem('seller', JSON.stringify(sellerUser));
          toast.success(`Welcome ${firstName}`);
          navigate('/seller-home', { state: { seller: sellerUser } });
        } else {
          const { firstName, lastName, token, customer_id, email: userEmail } = result.data;
          const customerUser = {
            role: 'User',
            first_name: firstName,
            last_name: lastName,
            token,
            customer_id,
            email: userEmail,
          };
          setUser(customerUser);
          localStorage.setItem('user', JSON.stringify(customerUser));
          localStorage.setItem('customer', JSON.stringify(customerUser));//sessionStorage
          toast.success('Welcome to application');
          navigate('/customer-home');
        }
      } else {
        toast.error('Invalid email or password');
      }
    }
  };

  return (
    <div className='container d-flex justify-content-center align-items-center' style={{ minHeight: '100vh' }}>
      <div className='card shadow-sm p-4 w-100' style={{ maxWidth: '400px' }}>
        <h3 className='text-center mb-4 text-primary'>{selectedRole} Login</h3>

        <div className='mb-3'>
          <label className='form-label'>Role</label>
          <select 
            className='form-control' 
            value={selectedRole} 
            onChange={(e) => setSelectedRole(e.target.value)}
          >
            <option value='User'>Customer</option>
            <option value='Seller'>Seller</option>
          </select>
        </div>

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
            Don't have an account?{' '}
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
