import React, { useEffect, useState } from "react";
import { useStore } from "../store/store";
import { ErrorResponse, useNavigate } from "react-router-dom"; // Import useNavigate

interface RegistrationFormProps {}

const RegistrationForm: React.FC<RegistrationFormProps> = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirmation, setPasswordConfirmation] = useState("");
  const [uniqueUsernameError, setUniqueUsernameError] = useState("");
  const [uniqueEmailError, setUniqueEmailError] = useState("");
  const [passwordConfirmationError, setPasswordConfirmationError] =
    useState("");

  const errorResponse = useStore((state) => state.errorResponse);
  const registrationAction = useStore((state) => state.registration);
  const clearErrorResponseAction = useStore(
    (state) => state.clearErrorResponse
  );
  const navigate = useNavigate();

  const clearErrors = () => {
    setUniqueUsernameError("");
    setUniqueEmailError("");
    setPasswordConfirmationError("");
  };

  const handleRegistratino = async (e: React.FormEvent) => {
    e.preventDefault();
    clearErrors();
    if (password !== passwordConfirmation) {
      setPasswordConfirmationError("Hesla se neshodují.");
      return;
    }
    console.log("ErrorResponse before:" + errorResponse.errorMessage);

    try {
      const response = await registrationAction(username, email, password);
      console.log("Response from registration: " + response);

      if (response && "errorMessage" in response) {
        console.log("Response from registration:", response.errorMessage);
      } else {
          navigate("/dashboard");
          window.location.reload();
      }
    } catch (error) {
      console.log(error);
    }


  };

  useEffect(() => {
    console.log("ErrorResponse useEffect:" + errorResponse.errorMessage);
    if (errorResponse.errorMessage) {
      if (errorResponse.errorMessage === "Username is not unique!") {
        setUniqueUsernameError("Username is not unique!");
      } else if (errorResponse.errorMessage === "E-mail is not unique!") {
        setUniqueEmailError("E-mail is not unique!");
      }
    }
  }, [errorResponse]);

  useEffect(() => {
    console.log("Mount");
    clearErrors();
    clearErrorResponseAction();
  }, []);

  return (
    <div className="loginForm">
      <h1>Vytvořte si účet</h1>
      <form onSubmit={handleRegistratino}>
        <div className="mb-3">
          <label htmlFor="username" className="form-label">
            Username
          </label>
          <input
            required
            type="text"
            className="form-control"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          {uniqueUsernameError && (
            <div className="text-warning">{uniqueUsernameError}</div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="email" className="form-label">
            E-mail
          </label>
          <input
            required
            type="email"
            className="form-control"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          {uniqueEmailError && (
            <div className="text-warning">{uniqueEmailError}</div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="password" className="form-label">
            Heslo
          </label>
          <input
            required
            pattern=".{5,}" // Příklad, heslo musí mít alespoň 5 znaků
            title="Heslo musí mít alespoň 5 znaků."
            type="password"
            className="form-control"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="passwordConfirmation" className="form-label">
            Potvrzení Hesla
          </label>
          <input
            required
            type="password"
            className="form-control"
            id="passwordConfirmation"
            value={passwordConfirmation}
            onChange={(e) => setPasswordConfirmation(e.target.value)}
          />
          {passwordConfirmationError && (
            <div className="text-warning">{passwordConfirmationError}</div>
          )}
        </div>
        <div className="d-grid">
          <button type="submit" className="btn btn-dark btn-lg">
            Vytvořit
          </button>
        </div>
      </form>
    </div>
  );
};

export default RegistrationForm;
