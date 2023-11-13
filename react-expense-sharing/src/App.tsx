import React, { useReducer, useState } from "react";
import "./App.css";
import InputField from "./components/InputField";
import { TodoReducer } from "./components/model";
import TodoList from "./components/TodoList";
import Header from "./components/Header";
import Auth from "./Auth";
import LoginForm from "./components/LoginForm";
import RegistrationForm from "./components/RegistrationForm";

const App: React.FC = () => {
  const [todo, setTodo] = useState<string>("");
  const [todoList, dispatch] = useReducer(TodoReducer, []);
  const [activeForm, setActiveForm] = useState<"login" | "register">("login");

  const handleAdd = (e: React.FormEvent) => {
    //  stops the form from being submitted in the traditional way
    //      (which would cause the page to reload).
    e.preventDefault();

    if (todo) {
      dispatch({ type: "add", payload: todo });
      setTodo("");
    }
  };

  const handleLogin = (email: string, password: string) => {
    console.log("Login attempt with:", email, password);
  };

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
    <div className="app">
      <Header />
      <InputField todo={todo} setTodo={setTodo} handleAdd={handleAdd} />
      <TodoList todoList={todoList} dispatch={dispatch} />
      <Auth />
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
        <LoginForm onSubmit={handleLogin} />
      ) : (
        <RegistrationForm onSubmit={handleRegistration}/>
      )}
    </div>
  );
};

export default App;
