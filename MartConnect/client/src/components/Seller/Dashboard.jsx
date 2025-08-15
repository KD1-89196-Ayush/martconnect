import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SellerHeader from "./Header";
import Footer from "./Footer";
import { getSellerOrders } from "../../services/orderDetails";

const Dashboard = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalOrders: 0,
    pendingOrders: 0,
    successfulOrders: 0,
    failedOrders: 0,
    totalRevenue: 0
  });
  const [recentOrders, setRecentOrders] = useState([]);

  useEffect(() => {
    const seller = JSON.parse(sessionStorage.getItem('seller')) || JSON.parse(localStorage.getItem('seller'));
    if (!seller) {
      alert("Seller not logged in.");
      navigate("/login", { state: { role: "Seller" } });
      return;
    }

    loadDashboardData(seller.seller_id);
  }, [navigate]);

  const loadDashboardData = async (sellerId) => {
    try {
      setLoading(true);
      const orders = await getSellerOrders(sellerId);
      
      if (Array.isArray(orders)) {
        // Calculate statistics
        const totalOrders = orders.length;
        
        // Filter orders by payment status (handle both JSON and backend formats)
        const pendingOrders = orders.filter(order => {
          const status = order.payment_status || order.paymentStatus;
          return status === 'PENDING' || status === 'Pending';
        }).length;
        
        const successfulOrders = orders.filter(order => {
          const status = order.payment_status || order.paymentStatus;
          return status === 'PAID' || status === 'Paid';
        }).length;
        
        const failedOrders = orders.filter(order => {
          const status = order.payment_status || order.paymentStatus;
          return status === 'FAILED' || status === 'CANCELLED' || 
                 status === 'Failed' || status === 'Cancelled';
        }).length;
        
        // Calculate total revenue from successful orders
        const totalRevenue = orders
          .filter(order => {
            const status = order.payment_status || order.paymentStatus;
            return status === 'PAID' || status === 'Paid';
          })
          .reduce((sum, order) => {
            const amount = parseFloat(order.total_amount || order.totalAmount || 0);
            return sum + amount;
          }, 0);

        setStats({
          totalOrders,
          pendingOrders,
          successfulOrders,
          failedOrders,
          totalRevenue: totalRevenue.toFixed(2)
        });

        // Get recent orders (last 5)
        const recent = orders
          .sort((a, b) => new Date(b.order_date || b.orderDate) - new Date(a.order_date || a.orderDate))
          .slice(0, 5);
        setRecentOrders(recent);
      }
    } catch (error) {
      // Error loading dashboard data
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    // Handle both JSON and backend formats
    const statusUpper = status?.toUpperCase();
    switch (statusUpper) {
      case 'PAID':
        return 'success';
      case 'PENDING':
        return 'warning';
      case 'FAILED':
      case 'CANCELLED':
        return 'danger';
      default:
        return 'secondary';
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  };

  if (loading) {
    return (
      <>
        <SellerHeader />
        <div className="container mt-5 text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-3">Loading Dashboard...</p>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <div className="d-flex flex-column min-vh-100">
      <SellerHeader />
      
      <div className="container flex-grow-1 mt-4 mb-5">
        <h2 className="mb-4">Seller Dashboard</h2>
        
        {/* Statistics Cards */}
        <div className="row mb-4">
          <div className="col-md-2 col-sm-6 mb-3">
            <div className="card text-center bg-primary text-white">
              <div className="card-body">
                <h5 className="card-title">Total Orders</h5>
                <h3 className="card-text">{stats.totalOrders}</h3>
              </div>
            </div>
          </div>
          
          <div className="col-md-2 col-sm-6 mb-3">
            <div className="card text-center bg-warning text-dark">
              <div className="card-body">
                <h5 className="card-title">Pending</h5>
                <h3 className="card-text">{stats.pendingOrders}</h3>
              </div>
            </div>
          </div>
          
          <div className="col-md-2 col-sm-6 mb-3">
            <div className="card text-center bg-success text-white">
              <div className="card-body">
                <h5 className="card-title">Successful</h5>
                <h3 className="card-text">{stats.successfulOrders}</h3>
              </div>
            </div>
          </div>
          
          <div className="col-md-2 col-sm-6 mb-3">
            <div className="card text-center bg-danger text-white">
              <div className="card-body">
                <h5 className="card-title">Failed</h5>
                <h3 className="card-text">{stats.failedOrders}</h3>
              </div>
            </div>
          </div>
          
          <div className="col-md-4 col-sm-12 mb-3">
            <div className="card text-center bg-info text-white">
              <div className="card-body">
                <h5 className="card-title">Total Revenue</h5>
                <h3 className="card-text">₹{stats.totalRevenue}</h3>
              </div>
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="row mb-4">
          <div className="col-12">
            <div className="card">
              <div className="card-header">
                <h5 className="mb-0">Quick Actions</h5>
              </div>
              <div className="card-body">
                <div className="row">
                  <div className="col-md-3 col-sm-6 mb-2">
                    <button 
                      className="btn btn-primary w-100"
                      onClick={() => navigate('/add-product')}
                    >
                      Add New Product
                    </button>
                  </div>
                  <div className="col-md-3 col-sm-6 mb-2">
                    <button 
                      className="btn btn-success w-100"
                      onClick={() => navigate('/order-details')}
                    >
                      View All Orders
                    </button>
                  </div>
                  <div className="col-md-3 col-sm-6 mb-2">
                    <button 
                      className="btn btn-info w-100"
                      onClick={() => navigate('/category-management')}
                    >
                      Manage Categories
                    </button>
                  </div>
                  <div className="col-md-3 col-sm-6 mb-2">
                    <button 
                      className="btn btn-warning w-100"
                      onClick={() => navigate('/seller-home')}
                    >
                      View Products
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Recent Orders */}
        <div className="row">
          <div className="col-12">
            <div className="card">
              <div className="card-header d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Recent Orders</h5>
                <button 
                  className="btn btn-sm btn-outline-primary"
                  onClick={() => navigate('/order-details')}
                >
                  View All
                </button>
              </div>
              <div className="card-body">
                {recentOrders.length === 0 ? (
                  <p className="text-muted text-center">No orders found.</p>
                ) : (
                  <div className="table-responsive">
                    <table className="table table-hover">
                      <thead>
                        <tr>
                          <th>Order ID</th>
                          <th>Customer</th>
                          <th>Amount</th>
                          <th>Status</th>
                          <th>Date</th>
                          <th>Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        {recentOrders.map((order) => (
                          <tr key={order.order_id || order.orderId}>
                            <td>{order.order_id || order.orderId}</td>
                            <td>
                              {order.customer ? 
                                `${order.customer.first_name || order.customer.firstName} ${order.customer.last_name || order.customer.lastName}` : 
                                'Unknown'
                              }
                            </td>
                            <td>₹{order.total_amount || order.totalAmount || 0}</td>
                            <td>
                              <span className={`badge bg-${getStatusColor(order.payment_status || order.paymentStatus)}`}>
                                {order.payment_status || order.paymentStatus || 'Unknown'}
                              </span>
                            </td>
                            <td>{formatDate(order.order_date || order.orderDate)}</td>
                            <td>
                              <button 
                                className="btn btn-sm btn-outline-primary"
                                onClick={() => navigate('/order-details', { 
                                  state: { orderId: order.order_id || order.orderId } 
                                })}
                              >
                                View Details
                              </button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <Footer />
    </div>
  );
};

export default Dashboard; 