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
    try {
    await loginAction(email, password);
      navigate("/dashboard");
      window.location.reload();
    }
    catch (error){
      console.log(error)
    }
  };

  return (
    <div className="loginForm">
      <h1>Vítejte, přihlaste se prosím.</h1>
      <form onSubmit={handleLogin}>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">
            E-mail
          </label>
          <input
            type="email"
            className="form-control"
            id="email"
            aria-describedby="emailHelp"
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="password" className="form-label">
            Heslo
          </label>
          <input
            type="password"
            className="form-control"
            id="password"
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="d-grid">
          <button type="submit" className="btn btn-dark btn-lg">
            Přihlásit se
          </button>
          <div className="text-center">
            <span>
              Zapomenuté heslo?&nbsp;
              <a href="/reset-password" className="link-light">
                Obnova hesla
              </a>
            </span>
          </div>
        </div>
      </form>
    </div>
  );
};

export default LoginForm;
