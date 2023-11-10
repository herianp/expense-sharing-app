import React, { useEffect, useRef, useState } from "react";
import { Actions, Todo } from "./model";
import { AiFillEdit, AiFillDelete } from "react-icons/ai";
import { MdDone } from "react-icons/md";
import "./styles.css";

interface Props {
  todo: Todo;
  todoList: Todo[];
  dispatch: React.Dispatch<Actions>;
}


const SingleTodo: React.FC<Props> = ({
  todo,
  todoList,
  dispatch,
}: Props) => {
  const [edit, setEdit] = useState<boolean>(false);
  const [editTodo, setEditTodo] = useState<string>(todo.todo);

  const handleEdit = (id: number) => {
    if (!edit && !todo.isDone) {
      setEdit(true);
    }
  };

  const handleEditRewrite = (e: React.FormEvent, id: number) => {
    e.preventDefault();
    dispatch({type: 'edit', payload: [todo.id, todo.todo]})
    setEdit(false);
  };

  useEffect(() => {
    inputRef.current?.focus();
  }, [edit]);

  const inputRef = useRef<HTMLInputElement>(null);

  return (
    <form
      className="todo-list-single"
      onSubmit={(e) => handleEditRewrite(e, todo.id)}
    >
      {edit ? (
        <input
          ref={inputRef}
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
        <span className="icon" onClick={() => dispatch({type: 'remove', payload: todo.id})}>
          <AiFillDelete />
        </span>
        <span className="icon" onClick={() => dispatch({type: 'done', payload: todo.id})}>
          <MdDone />
        </span>
      </div>
    </form>
  );
};

export default SingleTodo;
