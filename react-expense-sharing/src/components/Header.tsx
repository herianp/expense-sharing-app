import React from "react";
import { Button } from "react-bootstrap";
import "../css/Header.css"; // Předpokládá se soubor s CSS
import reactLogo from "../img/react_icon.png";
import { useStore } from "../store/store";
import { useNavigate } from "react-router-dom"; // Import useNavigate

interface HeaderProps {
  // zde můžete přidat vlastní props, pokud jsou potřeba
}

const Header: React.FC<HeaderProps> = () => {
  const { isAuthenticated, logoutAction } = useStore((state) => ({
    isAuthenticated: state.isAuthenticated,
    logoutAction: state.logout,
  }));
  const navigate = useNavigate();

  const handleLogout = async () => {
    console.log("Log out");
    logoutAction();
    navigateTo("/login");
    window.location.reload();
  };

  const navigateTo = (path: string) => {
    navigate(path);
  };

  return (
    <header className="header">
      <div className="left-section">
        <img src={reactLogo} alt="React Logo" className="react-logo" />
        {isAuthenticated ? (
          <>
            <Button
              onClick={() => navigateTo("/dashboard")}
              variant="secondary"
              className="btn-header"
            >
              Dashboard
            </Button>
            <Button
              onClick={() => navigateTo("/debts")}
              variant="secondary"
              className="btn-header"
            >
              Debts
            </Button>
            <Button
              onClick={() => navigateTo("/groups")}
              variant="secondary"
              className="btn-header"
            >
              Groups
            </Button>
            <Button
              onClick={() => navigateTo("/friends")}
              variant="secondary"
              className="btn-header"
            >
              Friends
            </Button>
            <Button
              onClick={() => navigateTo("/history")}
              variant="secondary"
              className="btn-header"
            >
              History
            </Button>
          </>
        ) : (
          <Button
            onClick={() => navigateTo("/home")}
            variant="secondary"
            className="btn-header"
          >
            Home
          </Button>
        )}
      </div>
      <div className="right-section">
        {isAuthenticated ? (
          <Button
            variant="secondary"
            className="btn-header"
            onClick={handleLogout}
          >
            Logout
          </Button>
        ) : (
          <>
            <Button
              variant="secondary"
              className="btn-header"
              onClick={() => navigateTo("/register")}
            >
              Registration
            </Button>
            <Button
              variant="secondary"
              className="btn-header"
              onClick={() => navigateTo("/login")}
            >
              Login
            </Button>
          </>
        )}
      </div>
    </header>
  );
};

export default Header;
