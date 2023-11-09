import React, { useRef } from "react";
import "./styles.css";

interface Props {
  todo: string;
  setTodo: React.Dispatch<React.SetStateAction<string>>;
  handleAdd: (e: React.FormEvent) => void;
}

const InputField: React.FC<Props> = ({ todo, setTodo, handleAdd }: Props) => {
  const inputRef = useRef<HTMLInputElement>(null);

  return (
    <form className="input" onSubmit={(e) => {
        handleAdd(e)
        // blue() removes focus from the specified element.
        inputRef.current?.blur();
    }}>
      <input
        ref={inputRef}
        type="input"
        value={todo}
        onChange={(event) => {
          setTodo(event.target.value);
        }}
        placeholder="Enter a task"
        className="input-box"
      />
      <button className="input-submit" type="submit">
        Go
      </button>
    </form>
  );
};

export default InputField;
