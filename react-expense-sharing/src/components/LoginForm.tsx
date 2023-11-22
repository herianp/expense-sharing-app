import React, { useState } from "react";
import { getPersonByEmailRequest } from "../axios/axiosRequests";
import { Person } from "./model";

interface LoginFormProps {
  onSubmit: (email: string, password: string) => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ onSubmit }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [email2, setEmail2] = useState("");
  const [password2, setPassword2] = useState("");
  const [data, setData] = useState<Person | undefined>();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(email, password);
  };

  const handleRequestSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    getPersonByEmailRequest(email)
      .then((data) => {
        setData(data);
        console.log("Email from request: "+ data?.email)
      })
      .catch((error) => {
        console.error("Error in useEffect:", error);
      });
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
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
