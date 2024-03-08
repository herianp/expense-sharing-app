import React, {useEffect, useState} from "react";
import closeIcon from "../../img/close.png";
import {GroupDTO, GroupForSinglePageDto, Person} from "../../types/model";
import {useStore} from "../../store/store";

interface GroupPersonsTableProps {
    titleOfTable: string,
    singleGroup: GroupForSinglePageDto,
    columns: string[],
    groupOwnerId: number | undefined,
    handleFetchGroup: () => void,
    removeMemberFromGroup: (memberEmail: string | undefined) => void,
}


const GroupPersonsTable: React.FC<GroupPersonsTableProps> = ({
                                                                 titleOfTable,
                                                                 singleGroup,
                                                                 columns,
                                                                 groupOwnerId,
                                                                 removeMemberFromGroup,
                                                                 handleFetchGroup
                                                             }) => {
    const personListInGroup = useStore((state) => state.personListInGroup);


    useEffect(() => {

    }, [personListInGroup]);

    return (
        <div className="group-container">
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
                    {personListInGroup.map((record) => {
                        return (
                            <tr key={record.id}>
                                <th scope="row">
                                    {record.username}
                                </th>
                                <td>
                                    {record.id != groupOwnerId &&
                                        <img
                                            src={closeIcon}
                                            alt="close"
                                            style={{width: "20px", cursor: "pointer"}}
                                            onClick={(event) => {
                                                removeMemberFromGroup(record.email)
                                                handleFetchGroup()
                                            }}
                                        />}
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

export default GroupPersonsTable;
