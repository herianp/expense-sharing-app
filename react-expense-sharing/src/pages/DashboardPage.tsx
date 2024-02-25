import React, { useEffect } from "react";
import { useStore } from "../store/store";
import "../css/Dashboard.css";
import CustomTable from "../components/CustomTable";

interface DashboardProps {
  // Definice pro props
}

const DashboardPage: React.FC<DashboardProps> = (props) => {
  const person = useStore((state) => state.person);
  const debtList = useStore((state) => state.debts);
  const expenseList = useStore((state) => state.expenses);

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

  function daysLeft(
    dueDate: string | Date,
    createdDate: string | Date
  ): number {
    const dueDateObj =
      typeof dueDate === "string" ? new Date(dueDate) : dueDate;
    const createdDateObj =
      typeof createdDate === "string" ? new Date(createdDate) : createdDate;

    const dueDateTime = dueDateObj.getTime();
    const createdDateTime = createdDateObj.getTime();

    const oneDay = 1000 * 60 * 60 * 24;
    const rozdilCasu = dueDateTime - createdDateTime;

    return Math.round(rozdilCasu / oneDay);
  }

  return (
    <div className="base-container">
      <CustomTable 
      columns={["Username", "Popis", "Částka"]} 
      list={debtList} 
      titleOfTable={"Komu dlužím"}/>
      <CustomTable 
      columns={["Username", "Popis", "Částka"]} 
      list={expenseList} 
      titleOfTable={"Kdo dluží mě"}/>
    </div>
  );
};

export default DashboardPage;
