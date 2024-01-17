import React, { useEffect } from "react";
import { useStore } from "../store/store";
import "../css/Dashboard.css";

interface DashboardProps {
  // Definice pro props
}

const DashboardPage: React.FC<DashboardProps> = (props) => {
  const person = useStore((state) => state.person);
  const debtList = useStore((state) => state.debts);

  useEffect(() => {
    console.log(debtList);
  });

  function formatDate(date: string | Date): string {
    const options: Intl.DateTimeFormatOptions = {
      year: "numeric",
      month: "long",
      day: "numeric",
    };
    return new Date(date).toLocaleDateString("cs-CZ", options);
  }

  function daysLeft(dueDate: string | Date, createdDate: string | Date): number {
    const dueDateObj = typeof dueDate === 'string' ? new Date(dueDate) : dueDate;
    const createdDateObj = typeof createdDate === 'string' ? new Date(createdDate) : createdDate;
  
    const dueDateTime = dueDateObj.getTime();
    const createdDateTime = createdDateObj.getTime();
  
    const oneDay = 1000 * 60 * 60 * 24;
    const rozdilCasu = dueDateTime - createdDateTime;
  
    return Math.round(rozdilCasu / oneDay);
  }

  return (
    <div className="dashboard-container">
      <div className="debts-container">
        <h1 style={{textAlign:"center"}}>Receivables</h1>
        <div className="table-responsive">
        <table className="table table-dark table-striped">
          <thead>
            <tr>
              <th scope="col">Debtor</th>
              <th scope="col">Amount</th>
              <th scope="col">Description</th>
              <th scope="col">CreatedAt</th>
              <th scope="col">DueDate</th>
              <th scope="col">DayLeft</th>
            </tr>
          </thead>
          <tbody>
            {debtList.map((debt) => (
              <tr>
                <th scope="row">{debt.personNameToPayBack}</th>
                <td>{debt.amount}</td>
                <td>{debt.description}</td>
                <td>{formatDate(debt.createdAt)}</td>
                <td>{formatDate(debt.dueDate)}</td>
                <td>{daysLeft(debt.dueDate, debt.createdAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </div>
      <div className="debts-container">
        <h1 style={{textAlign:"center"}}>Liabilities</h1>
        <div className="table-responsive">
        <table className="table table-dark table-striped">
          <thead>
            <tr>
              <th scope="col">Creditor</th>
              <th scope="col">Amount</th>
              <th scope="col">Description</th>
              <th scope="col">CreatedAt</th>
              <th scope="col">DueDate</th>
              <th scope="col">DayLeft</th>
            </tr>
          </thead>
          <tbody>
            {debtList.map((debt) => (
              <tr>
                <th scope="row">{debt.personNameToPayBack}</th>
                <td>{debt.amount}</td>
                <td>{debt.description}</td>
                <td>{formatDate(debt.createdAt)}</td>
                <td>{formatDate(debt.dueDate)}</td>
                <td>{daysLeft(debt.dueDate, debt.createdAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
