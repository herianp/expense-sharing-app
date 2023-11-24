import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useStore } from '../store/store'; // Import your Zustand store

const ProtectedRoute: React.FC = () => {
  const isAuthenticated = useStore(state => state.isAuthenticated);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />; // Renders child routes if authenticated
};

export default ProtectedRoute;