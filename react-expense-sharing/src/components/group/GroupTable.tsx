import React from 'react';
import groupIcon from "../../img/group_enter.png";
import {GroupDTO} from "../../types/model";
import {Link} from "react-router-dom";

interface GroupTableProps {
    columns: string[];
    list: GroupDTO[];
    titleOfTable: string;
}

function formatDate(input: Date | string | undefined): string {
    let date: Date;
    if (input === undefined) {
        return 'N/A'; //
    }
    if (input instanceof Date) {
        date = input;
    } else {
        {
            date = new Date(input);
            if (isNaN(date.getTime())) { // Check if the date is invalid
                throw new Error('Invalid date string');
            }
        }
    }

    return date.toISOString().split('T')[0];
}

const GroupTable: React.FC<GroupTableProps> = ({
                                                   columns,
                                                   list,
                                                   titleOfTable,
                                               }) => {
    return (
        <div className="group-container">
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
                    </tr>
                    </thead>
                    <tbody>
                    {list.map((record) => {
                        return (
                            <tr key={record.id}>
                                <th scope="row">
                                    {record.name}
                                </th>
                                <td>{record.createdAt instanceof Date ? record.createdAt.toISOString().split('T')[0] : formatDate(record.createdAt)}</td>
                                <td>
                                    <Link to={`/group/${record.id}`}
                                          state={{groupId: record.id } as GroupDTO}>
                                    <img
                                        src={groupIcon}
                                        alt="Enter Group"
                                        style={{width: "20px", cursor: "pointer"}}
                                        onClick={() => console.log("Vstup do skupiny")}
                                    />
                                    </Link>
                                </td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default GroupTable;