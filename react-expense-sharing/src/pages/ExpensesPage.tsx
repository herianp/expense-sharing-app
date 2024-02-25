import React from "react";
import {useStore} from "../store/store";
import ExpensesTable from "../components/ExpensesTable";
import AddExpenseForm from "../components/AddExpenseForm";

interface ExpensesProps {
}

const ExpensesPage: React.FC<ExpensesProps> = (props) => {
    const expenseList = useStore((state) => state.expenses);

    return (
        <div className="base-container">
            <AddExpenseForm/>
            <ExpensesTable columns={["Username", "Popis", "Částka"]} list={expenseList} titleOfTable={"Pohledávky"}/>
        </div>
    );
};

export default ExpensesPage;
