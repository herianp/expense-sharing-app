import React, { useEffect, useReducer, useRef, useState } from "react";
import "./App.css";
import InputField from "./components/InputField";
import { Todo, TodoReducer } from "./components/model";
import TodoList from "./components/TodoList";

const App: React.FC = () => {
  const [todo, setTodo] = useState<string>("");
  // const [todoList, setTodoList] = useState<Todo[]>([]);
  const [todoList, dispatch] = useReducer(TodoReducer, [])

  const handleAdd = (e: React.FormEvent) => {
    //  stops the form from being submitted in the traditional way
    //      (which would cause the page to reload).
    e.preventDefault();

    if (todo) {
      dispatch({type: 'add', payload: todo})
      setTodo("");
    }
  };

  return (
    <div className="app">
      <span className="heading">Taskify</span>
      <InputField todo={todo} setTodo={setTodo} handleAdd={handleAdd} />
      <TodoList todoList={todoList} dispatch={dispatch} />
    </div>
  );
};

export default App;

