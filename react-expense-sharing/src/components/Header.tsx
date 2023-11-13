import React from 'react';
import { Button } from 'react-bootstrap';
import '../css/Header.css'; // Předpokládá se soubor s CSS
import reactLogo from '../img/react_icon.png';

interface HeaderProps {
  // zde můžete přidat vlastní props, pokud jsou potřeba
}

const Header: React.FC<HeaderProps> = () => {
  return (
    <header className="header">
      <div className="left-section">
        <img src={reactLogo} alt="React Logo" className="react-logo" />
        <Button variant="secondary" className="btn-header">Home</Button>
        <Button variant="secondary" className="btn-header">Debts</Button>
      </div>
      <div className="right-section">
        <Button variant="secondary" className="btn-header">Contact</Button>
        <Button variant="secondary" className="btn-header">Registration</Button>
        <Button variant="secondary" className="btn-header">Login</Button>
      </div>
    </header>
  );
};

export default Header;