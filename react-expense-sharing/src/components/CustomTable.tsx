import React, { useEffect, useState } from "react";
import { useStore } from "../store/store";
import {Debt, Expense, ExpenseDTO} from "../types/model";
import checkIcon from "../img/check.png";

interface CustomTableProps {
  columns: string[];
  list: ExpenseDTO[] | Debt[];
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

function deleteFinancialRecord(recordId: number|undefined) {
  console.log("TODO delete from a list");
}

const CustomTable: React.FC<CustomTableProps> = ({
  columns,
  list,
  titleOfTable,
}) => {
  const person = useStore((state) => state.person);

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
            </tr>
          </thead>
          <tbody>
            {list.map((record) => {

              return (
                <tr key={record.id}>
                  <th scope="row">
                    {"personNameToPayBack" in record
                      ? record.personNameToPayBack
                      : record.personNameWhoIsPay}
                  </th>
                  <td>{record.description}</td>
                  <td>{record.amount}</td>
                  <td>
                    <img
                      src={checkIcon}
                      alt="check"
                      style={{ width: "20px", cursor: "pointer" }}
                      onClick={() => deleteFinancialRecord(record.id)}
                    />
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

export default CustomTable;
