import React from "react";
import eidtIcon from "../../img/editIcon.png";
import {ExpenseDTO} from "../../types/model";
import {useStore} from "../../store/store";

interface ExpenseTableProps {
    columns: string[];
    list: ExpenseDTO[];
    titleOfTable: string;
}

const ExpenseGroupTable: React.FC<ExpenseTableProps> = ({list, columns, titleOfTable}) => {
    const person = useStore((state) => state.person);

    function editFinancialRecord(recordId: number | undefined) {
        console.log("TODO edit...");
    }

    function getGroupNameById(groupId: number | undefined) {
        let groupName: string | undefined = "";
        if (person.groupList) {
            const group = person.groupList.find(x => x.id === groupId);
            groupName = group ? group.name : undefined;
        }
        return groupName;
    }

    return (
        <div className="debts-container">
            <h2 style={{textAlign: "center"}}>{titleOfTable}</h2>
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
                                    {record.personNameWhoHasToBePayed}
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
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ExpenseGroupTable;
