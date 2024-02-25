import React from "react";
import ResetPasswordForm from "../components/ResetPasswordForm";
import "../css/ResetPassword.css";

interface LoginProps {}

const LoginPage: React.FC<LoginProps> = () => {
  return (
    <div className="resetPage">
      <ResetPasswordForm />
    </div>
  );
};

export default LoginPage;
