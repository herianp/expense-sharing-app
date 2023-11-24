import React, { useState } from "react";
import LoginForm from "./LoginForm";
import RegistrationForm from "./RegistrationForm";

interface AuthenticationProps {}

const Authentication: React.FC<AuthenticationProps> = () => {
    const [activeForm, setActiveForm] = useState<"login" | "register">("login");

    const handleRegistration = (username: string, email: string, password: string) => {
        console.log("Login attempt with:", email, password);
      };

    const getButtonStyle = (formType: "login" | "register") => {
        return {
          backgroundColor: activeForm === formType ? "green" : "white",
          color: activeForm === formType ? "white" : "black",
        };
      };
  return (
    <div>
      <button
        style={getButtonStyle("login")}
        onClick={() => setActiveForm("login")}
      >
        Login
      </button>
      <button
        style={getButtonStyle("register")}
        onClick={() => setActiveForm("register")}
      >
        Register
      </button>

      {activeForm === "login" ? (
        <LoginForm />
      ) : (
        <RegistrationForm onSubmit={handleRegistration} />
      )}
    </div>
  );
};

export default Authentication;
