import React, { useState } from "react";
import { Todo } from "./model";
import { AiFillEdit, AiFillDelete } from "react-icons/ai";
import { MdDone } from "react-icons/md";
import "./styles.css";
import TodoList from "./TodoList";

interface Props {
  todo: Todo;
  todoList: Todo[];
  setTodoList: React.Dispatch<React.SetStateAction<Todo[]>>;
}

const SingleTodo: React.FC<Props> = ({
  todo,
  todoList,
  setTodoList,
}: Props) => {
  const [edit, setEdit] = useState<boolean>(false);
  const [editTodo, setEditTodo] = useState<string>(todo.todo);

  const handleDone = (id: number) => {
    setTodoList(
      todoList.map((todo) =>
        todo.id === id ? { ...todo, isDone: !todo.isDone } : todo
      )
    );
  };

  const handleDelete = (id: number) => {
    setTodoList(todoList.filter((todo) => todo.id !== id));
  };

  const handleEdit = (id: number) => {
    if (!edit && !todo.isDone) {
      setEdit(true);
    }
  };
  
  const handleEditRewrite = (e:React.FormEvent, id: number) => {
    e.preventDefault();

    setTodoList(todoList.map((todo) => (
        todo.id===id? {...todo, todo:editTodo} : todo
    )))
    setEdit(false);
  };

  

  return (
    <form className="todo-list-single" onSubmit={(e) => handleEditRewrite(e, todo.id)}>
      {edit ? (
        <input
          value={editTodo}
          onChange={(e) => setEditTodo(e.target.value)}
          className="todo-list-single--text"
        />
      ) : todo.isDone ? (
        <s className="todo-list-single--text">{todo.todo}</s>
      ) : (
        <span className="todo-list-single--text">{todo.todo}</span>
      )}
      <div>
        <span className="icon" onClick={() => handleEdit(todo.id)}>
          <AiFillEdit />
        </span>
        <span className="icon" onClick={() => handleDelete(todo.id)}>
          <AiFillDelete />
        </span>
        <span className="icon" onClick={() => handleDone(todo.id)}>
          <MdDone />
        </span>
      </div>
    </form>
  );
};

export default SingleTodo;
