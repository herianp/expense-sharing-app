import React, { useEffect, useState } from "react";
import { useStore } from "../store/store";
import {Debt, DebtDTO, Expense, ExpenseDTO} from "../types/model";
import eidtIcon from "../img/editIcon.png";
import sendDebtIcon from "../img/send_debt_icon.png";
import pendingDebtIcon from "../img/pending_debt_icon.png";

interface DebtsTableProps {
  columns: string[];
  list: DebtDTO[];
  titleOfTable: string;
}

function formatDate(date: string | Date): string {
  const options: Intl.DateTimeFormatOptions = {
    year: "numeric",
    month: "long",
    day: "numeric",
  };
  return new Date(date).toLocaleDateString("cs-CZ", options);
}

function daysLeft(dueDate: string | Date, createdDate: string | Date): number {
  const dueDateObj = typeof dueDate === "string" ? new Date(dueDate) : dueDate;
  const createdDateObj =
    typeof createdDate === "string" ? new Date(createdDate) : createdDate;

  const dueDateTime = dueDateObj.getTime();
  const createdDateTime = createdDateObj.getTime();

  const oneDay = 1000 * 60 * 60 * 24;
  const rozdilCasu = dueDateTime - createdDateTime;

  return Math.round(rozdilCasu / oneDay);
}


const DebtsTable: React.FC<DebtsTableProps> = ({
  columns,
  list,
  titleOfTable,
}) => {
  const person = useStore((state) => state.person);
  const setDebtAndExpenseStatusToPending = useStore((state) => state.setDebtAndExpenseStatusToPending);
  const [isPopupVisible, setIsPopupVisible] = useState(false);

  function editFinancialRecord(recordId: number | undefined) {
    console.log("TODO delete from a list");
  }

  async function setStatusToPending(id: number | undefined){
    if (id != undefined){
      await setDebtAndExpenseStatusToPending(id);
    }
  }

  // Show the popup
  const handleMouseEnter = () => {
    setIsPopupVisible(true);
  };

  // Hide the popup
  const handleMouseLeave = () => {
    setIsPopupVisible(false);
  };

  return (
    <div className="debts-container">
      <h1 style={{ textAlign: "center" }}>{titleOfTable}</h1>
      <div className="table-responsive">
        <table className="table table-dark table-striped">
          <thead>
            <tr>
              {columns.map((column) => (
                <th key={column} scope="col">
                  {column}
                </th>
              ))}
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {list.map((record) => {
              return (
                  <tr key={record.id}>
                    <th scope="row">
                      {record.personNameToPayBack}
                    </th>
                    <td>{record.description}</td>
                    <td>{record.amount}</td>
                    <td>
                      <img
                          src={eidtIcon}
                          alt="check"
                          style={{width: "20px", cursor: "pointer"}}
                          onClick={() => editFinancialRecord(record.id)}
                      />
                    </td>
                    <td onMouseLeave={handleMouseLeave}>
                      {record.status == "ACTIVE" && <img
                          onMouseEnter={handleMouseEnter}
                          src={sendDebtIcon}
                          alt="check"
                          style={{width: "20px", cursor: "pointer"}}
                          onClick={() => setStatusToPending(record.id)}
                      />}
                      {record.status == "PENDING" && <div style={{cursor: "no-drop"}}><img
                          src={pendingDebtIcon}
                          alt="check"
                          style={{width: "20px", pointerEvents: "none"}}
                      /></div>}
                      </td>
                        </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default DebtsTable;
