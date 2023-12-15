import React from "react";
import { useStore } from "../store/store";

interface DebtsProps {
  // Definice pro props
}

const DebtsPage: React.FC<DebtsProps> = (props) => {
  const person = useStore((state) => state.person);

  return (
    <div>
      <h1>Debts</h1>
      {person.debtList?.map((debt) => (
        <div key={debt.id}>
          <p>Description: {debt.description}</p>
          <p>Amount: {debt.amount}</p>
          <p>Created At: {debt.createdAt.toString()}</p>
          <p>Due Date: {debt.dueDate.toString()}</p>
        </div>
      ))}
    </div>
  );
};

export default DebtsPage;
