import React, { useState } from "react";
import { useStore } from "../store/store";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom"; // Import useNavigate


const HomePage: React.FC = () => {
  const person = useStore((state) => state.person);

  const navigate = useNavigate();

  return (
    <div>
      <h2>Už tě nebaví si pamatovat kdo ti dluží?</h2>
      <h2>Píšeš si to všude možně, ale stejně na to zapomeneš?</h2>
      <h2>Máme řešení přímo pro tebe!</h2>
      <h2>Zaregistruj se a měj pod kontrolou všechny dluhy.</h2>
      <Button
              variant="secondary"
              className="btn-header"
              onClick={() => navigate("/register")}
            >
              Registruj se u nás
            </Button>
    </div>
  );
};

export default HomePage;
