export interface Todo {
  id: number;
  todo: string;
  isDone: boolean;
}

// action v useReducer ma dva atributy: type and payload
export type Actions =
  | { type: "add"; payload: string }
  | { type: "remove"; payload: number }
  | { type: "done"; payload: number }
  | { type: "edit"; payload: [number, string] };

export const TodoReducer = (state: Todo[], action: Actions) => {
  switch (action.type) {
    case "add":
      return [
        ...state,
        { id: Date.now(), todo: action.payload, isDone: false },
      ];
    case "remove":
      return state.filter((todo) => todo.id !== action.payload);
    case "done":
      return state.map((todo) =>
        todo.id === action.payload ? { ...todo, isDone: !todo.isDone } : todo
      );
    case "edit":
      return state.map((todo) =>
        todo.id === action.payload[0] ? { ...todo, todo: action.payload[1] } : todo
      );
  }
};
