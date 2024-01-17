import React, { useState } from "react";
import { useStore } from "../store/store";
import { useNavigate } from "react-router-dom"; // Import useNavigate

interface LoginFormProps {}

const LoginForm: React.FC<LoginFormProps> = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const loginAction = useStore((state) => state.login);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    //zajistuje nam aby se po submit nerefreshla stranka
    e.preventDefault();

    await loginAction(email, password);
    navigate("/dashboard");
    window.location.reload();
  };

  return (
    <div className="loginForm">
      <h1>LOGIN FORM</h1>
      <form onSubmit={handleLogin}>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">
            Email address
          </label>
          <input
            type="email"
            className="form-control"
            id="email"
            aria-describedby="emailHelp"
            onChange={(e) => setEmail(e.target.value)}
          />
          <div id="emailHelp" className="form-text">
            We'll never share your email with anyone else.
          </div>
        </div>
        <div className="mb-3">
          <label htmlFor="password" className="form-label">
            Password
          </label>
          <input
            type="password"
            className="form-control"
            id="password"
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <div className="d-grid">
          <button type="submit" className="btn btn-dark btn-lg">
            Přihlásit se
          </button>
        </div>
      </form>
    </div>
  );
};

export default LoginForm;
