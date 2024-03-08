import React, {useEffect, useState} from "react";
import {useStore} from "../store/store";
import {Debt, Expense, ExpenseDTO} from "../types/model";
import eidtIcon from "../img/editIcon.png";
import checkExpenseIcon from "../img/check.png";
import waitingForDebtAction from "../img/waiting_for_debt_action.png";

interface ExpensesTableProps {
    columns: string[];
    list: ExpenseDTO[];
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


const ExpensesTable: React.FC<ExpensesTableProps> = ({
                                                         columns,
                                                         list,
                                                         titleOfTable,
                                                     }) => {
    const person = useStore((state) => state.person);
    const setExpenseAndDebtStatusToDone = useStore((state) => state.setExpenseAndDebtStatusToDone);

    function editFinancialRecord(recordId: number | undefined) {
        console.log("TODO delete from a list");
    }

    function getGroupNameById(groupId: number | undefined) {
        let groupName: string | undefined = "";
        if (person.groupList) {
            const group = person.groupList.find(x => x.id === groupId);
            groupName = group ? group.name : undefined;
        }
        return groupName;
    }

    async function setStatusToDone(id: number | undefined){
        if (id != undefined){
            await setExpenseAndDebtStatusToDone(id);
        }
    }

    return (
        <div className="debts-container">
            <h1 style={{textAlign: "center"}}>{titleOfTable}</h1>
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
                            record.personNameWhoIsPay &&
                            <tr key={record.id}>
                                <th scope="row">
                                    {record.personNameWhoIsPay ?? getGroupNameById(record.groupId)}
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
                                <td>
                                    {record.status == "ACTIVE" && <img
                                        src={waitingForDebtAction}
                                        alt="check"
                                        style={{width: "20px", cursor: "no-drop"}}
                                    />}
                                        {record.status == "PENDING" && <img
                                        src={checkExpenseIcon}
                                        alt="check"
                                        style={{width: "20px", cursor: "pointer"}}
                                        onClick={() => setStatusToDone(record.id)}
                                    />}
                                </td>
                            </tr>

                        )
                            ;
                    })}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ExpensesTable;
