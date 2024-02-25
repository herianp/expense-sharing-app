import React from "react";
import {useStore} from "../store/store";
import DebtsTable from "../components/DebtsTable";
import AddDebtForm from "../components/AddDebtForm";

interface DebtsProps {
    // Definice pro props
}

const DebtsPage: React.FC<DebtsProps> = (props) => {
    const debtList = useStore((state) => state.debts);

    return (
        <div className="base-container">
            <AddDebtForm/>
            <DebtsTable columns={["Username", "Popis", "Částka"]} list={debtList} titleOfTable={"Dluhy"}/>
        </div>
    );
};

export default DebtsPage;
