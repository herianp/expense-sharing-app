import React from "react";
import "./styles.css";
import { Actions, Todo } from "./model";
import SingleTodo from "./SingleTodo";

interface Props {
  todoList: Todo[];
  dispatch: React.Dispatch<Actions>;
}

const TodoList: React.FC<Props> = ({ todoList, dispatch }: Props) => {
  return (
    <div className="todo-list">
      {todoList.map((todo) => (
        <SingleTodo
          todo={todo}
          key={todo.id}
          todoList={todoList}
          dispatch={dispatch}
        />
      ))}
    </div>
  );
};

export default TodoList;
