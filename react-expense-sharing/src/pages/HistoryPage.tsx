import React from "react";
import { useStore } from "../store/store";

interface HistoryProps {
  // Definice pro props
}

const HistoryPage: React.FC<HistoryProps> = (props) => {
  const person = useStore((state) => state.person);

  return (
    <div>
      <h1>History</h1>
      <p>User email: {person.email}</p>
    </div>
  );
};

export default HistoryPage;
