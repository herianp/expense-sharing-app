import React, { useState } from "react";
import RegistrationForm from "./RegistrationForm";

interface LoginProps {}

const LoginPage: React.FC<LoginProps> = () => {


  return (
    <div>
      <h1>REGISTRATION</h1>
      <RegistrationForm />
    </div>
  );
};

export default LoginPage;
