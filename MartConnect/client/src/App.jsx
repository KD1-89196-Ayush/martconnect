import React, { useState, useEffect } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import SellerHome from './pages/SellerHome'
import About from './components/Seller/About'
import SellerContact from './components/Seller/contact'
import { AuthContext } from './contexts/auth.context'
import { ToastContainer } from 'react-toastify'

import AddProduct from './components/AddProduct'
import UpdateProduct from './components/UpdateProduct'
import ProductOrder from './components/ProductOrder'
import OrderDetail from './components/OrderDetails'
  
function App() {
  const [user, setUser] = useState(null)


  useEffect(() => {
    const storedUser = localStorage.getItem('user')
    if (storedUser) {
      setUser(JSON.parse(storedUser))
    }
  }, [])

  useEffect(() => {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user))
    } else {
      localStorage.removeItem('user')
    }
  }, [user])

  return (
    <>
      <AuthContext.Provider value={{ user, setUser }}>
        <Routes>
          {/* Public Routes */}
          <Route path='/' element={<Home />} />
          <Route path='/login' element={<Login />} />
          <Route path='/register' element={<Register />} />

          {/* Protected Seller Routes */}
          <Route
            path='/seller-home'
            element={user ? <SellerHome /> : <Navigate to='/login' state={{ role: 'Seller' }} />}
          />
          <Route
            path='/about'
            element={user ? <About /> : <Navigate to='/login' state={{ role: 'Seller' }} />}
          />
          <Route
            path='/seller-contact'
            element={user ? <SellerContact /> : <Navigate to='/login' state={{ role: 'Seller' }} />}
          />

          <Route
            path='/addProduct'
            element={<AddProduct/>}
          />  

            <Route
            path='/updateProduct'
            element={<UpdateProduct/>}
          />  

            <Route
            path='/productOrder'
            element={<ProductOrder/>}
          />  

            <Route
            path='/orderDetails'
            element={<OrderDetail/>}
          />  




        </Routes>

      </AuthContext.Provider>

      <ToastContainer />
    </>
  )
}

export default App
