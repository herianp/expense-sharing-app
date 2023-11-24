import React from "react";
import "./App.css";
import Header from "./components/Header";
import Auth from "./components/Auth";
import HomePage from "./components/HomePage";
import ProtectedRoute from "./components/ProtectedRoute";
import Authentication from "./components/Authentication";
import { BrowserRouter, Routes, Route } from "react-router-dom";

const App: React.FC = () => {
  return (
    <div className="app">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/login" element={<Authentication />} />
          <Route path="/" element={<HomePage />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/debts" element={<Auth />} />
            {/* Add other protected routes here */}
          </Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
};

export default App;
