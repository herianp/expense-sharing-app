import React from "react";
import LoginForm from "../components/LoginForm";
import "../css/Login.css";

interface LoginProps {}

const LoginPage: React.FC<LoginProps> = () => {
  return (
    <div className="loginPage">
      <LoginForm />
    </div>
  );
};

export default LoginPage;
