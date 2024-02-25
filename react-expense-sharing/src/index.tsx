import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import "bootstrap/dist/css/bootstrap.css";
import "./css/Expenses.css";
import "./css/Dashboard.css";
import "./css/Base.css";
import "./css/Friend.css";
import "./css/Header.css";
import "./css/Login.css";
import "./css/ResetPassword.css";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
 
root.render(
  <React.StrictMode>
      <App />
  </React.StrictMode>
);
