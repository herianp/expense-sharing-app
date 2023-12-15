import React from "react";
import "./App.css";
import Header from "./components/Header";
import HomePage from "./components/HomePage";
import ProtectedRoute from "./components/ProtectedRoute";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import DashboardPage from "./components/DashboardPage";
import DebtsPage from "./components/DebtsPage";
import GroupsPage from "./components/GroupsPage";
import FriendsPage from "./components/FriendsPage";
import HistoryPage from "./components/HistoryPage";
import LoginPage from "./components/LoginPage";
import RegistrationPage from "./components/RegistrationPage";

const App: React.FC = () => {
  return (
    <div className="app">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegistrationPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/debts" element={<DebtsPage />} />
            <Route path="/groups" element={<GroupsPage />} />
            <Route path="/friends" element={<FriendsPage />} />
            <Route path="/history" element={<HistoryPage />} />
            {/* Add other protected routes here */}
          </Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
};

export default App;
