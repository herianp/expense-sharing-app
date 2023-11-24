import React, { useState } from "react";
import { getPersonByEmailRequest } from "../axios/axiosRequests";
import { Person } from "../types/model";
import { useStore } from "../store/store";
import { useNavigate } from "react-router-dom"; // Import useNavigate

interface LoginFormProps {}

const LoginForm: React.FC<LoginFormProps> = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [email2, setEmail2] = useState("");
  const [password2, setPassword2] = useState("");
  const [data, setData] = useState<Person | undefined>();

  const loginAction = useStore((state) => state.login);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    //zajistuje nam aby se po submit nerefreshla stranka
    e.preventDefault();

    await loginAction(email, password);
    navigate("/debts");
    window.location.reload();
  };

  const handleRequestSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    getPersonByEmailRequest(email2)
      .then((data) => {
        setData(data);
        console.log("Email from request: " + data?.email);
      })
      .catch((error) => {
        console.error("Error in useEffect:", error);
      });
  };

  return (
    <div>
      <form onSubmit={handleLogin}>
        <div>
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit">Login</button>
      </form>

      <form onSubmit={handleRequestSubmit}>
        <div>
          <label htmlFor="email2">Email2:</label>
          <input
            type="email"
            id="email2"
            value={email2}
            onChange={(e) => setEmail2(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="password2">Password:</label>
          <input
            type="password"
            id="password2"
            value={password2}
            onChange={(e) => setPassword2(e.target.value)}
          />
        </div>
        <button type="submit">Send request</button>
      </form>
    </div>
  );
};

export default LoginForm;
