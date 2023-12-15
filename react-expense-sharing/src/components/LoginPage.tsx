import React from "react";
import LoginForm from "./LoginForm";

interface LoginProps {}

const LoginPage: React.FC<LoginProps> = () => {
  return (
    <div>
      <h1>LOGIN</h1>
      <LoginForm />
    </div>
  );
};

export default LoginPage;
