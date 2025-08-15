import React, { useContext, useState } from 'react';
import { toast } from 'react-toastify';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { loginUser } from '../services/user';
import { AuthContext } from '../contexts/auth.context';

function Login() {
  const { setUser } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const [selectedRole, setSelectedRole] = useState(location.state?.role || 'User');

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
          const { firstName, lastName, token, customer_id, email: userEmail, phone, address } = result.data;
          const customerUser = {
            role: 'User',
            first_name: firstName,
            last_name: lastName,
            token,
            customer_id,
            email: userEmail,
            phone,
            address,
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
    <div className="min-vh-100 d-flex align-items-center justify-content-center bg-light">
      <div className="card shadow-sm" style={{ maxWidth: '400px', width: '100%' }}>
        <div className="card-body p-4">
          <div className="text-center mb-4">
            <h4 className="text-primary mb-2">Welcome Back</h4>
            <p className="text-muted">Sign in to your account</p>
          </div>

          <div className="mb-3">
            <label className="form-label">Role</label>
            <select 
              className="form-select" 
              value={selectedRole} 
              onChange={(e) => setSelectedRole(e.target.value)}
            >
              <option value='User'>Customer</option>
              <option value='Seller'>Seller</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Email</label>
            <input
              onChange={(e) => setEmail(e.target.value)}
              type="email"
              className="form-control"
              placeholder="Enter your email"
            />
          </div>

          <div className="mb-4">
            <label className="form-label">Password</label>
            <input
              onChange={(e) => setPassword(e.target.value)}
              type="password"
              className="form-control"
              placeholder="Enter your password"
            />
          </div>

          <button
            onClick={onLogin}
            className="btn btn-primary w-100 mb-3"
          >
            Sign In
          </button>

          <div className="text-center">
            <p className="text-muted mb-0">
              Don't have an account?{' '}
              <Link to="/register" className="text-primary text-decoration-none">
                Register here
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
