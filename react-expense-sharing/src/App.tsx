import React from "react";
import "./App.css";
import Header from "./components/Header";
import HomePage from "./pages/HomePage";
import ProtectedRoute from "./components/ProtectedRoute";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import DashboardPage from "./pages/DashboardPage";
import DebtsPage from "./pages/DebtsPage";
import GroupsPage from "./pages/GroupsPage";
import FriendsPage from "./pages/FriendsPage";
import HistoryPage from "./pages/HistoryPage";
import LoginPage from "./pages/LoginPage";
import RegistrationPage from "./pages/RegistrationPage";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import ExpensesPage from "./pages/ExpensesPage";

const App: React.FC = () => {
  return (
    <div className="app">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegistrationPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/reset-password" element={<ResetPasswordPage />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/debts" element={<DebtsPage />} />
            <Route path="/receivables" element={<ExpensesPage />} />
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
