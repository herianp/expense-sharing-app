import React from "react";
import {useStore} from "../store/store";
import GroupTable from "../components/group/GroupTable";
import AddGroupForm from "../components/group/AddGroupForm";

interface GroupsProps {
    // Definice pro props
}

const GroupsPage: React.FC<GroupsProps> = (props) => {
    const groups = useStore((state) => state.groups);

    return (
        <div className="base-container">
            <AddGroupForm/>
            <GroupTable  columns={["Název","Datum vytvoření"]} list={groups} titleOfTable={"Skupiny"}/>
        </div>
    );
};

export default GroupsPage;
