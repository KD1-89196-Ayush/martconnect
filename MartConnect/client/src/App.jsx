import React, { useState } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import Home from './pages/Home'
import { AuthContext } from './contexts/auth.context'
import { ToastContainer } from 'react-toastify'
import Login from './pages/Login'
import Register from './pages/Register'
import AddProduct from './components/AddProduct'
  
function App() {
  // state for user context
  const [user, setUser] = useState(null)

  return (
    <>
      <AuthContext.Provider value={{ user, setUser }}>
        <Routes>
          <Route path='/' element={<Home />} />

          <Route path='/login' element={<Login />} />
          <Route path='/register' element={<Register />} />

          {/* Protected route */}
          <Route
            path='/home'
            element={user ? <Home /> : <Navigate to='/' />}
          />

          <Route
            path='/addProduct'
            element={<AddProduct/>}
          />  
        </Routes>
      </AuthContext.Provider>

      <ToastContainer />
    </>
  )
}

export default App
