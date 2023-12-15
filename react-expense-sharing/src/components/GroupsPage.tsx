import React from "react";
import { useStore } from "../store/store";

interface GroupsProps {
  // Definice pro props
}

const GroupsPage: React.FC<GroupsProps> = (props) => {
  const person = useStore((state) => state.person);

  return (
    <div>
      <h1>Groups</h1>
      {person.groupList?.map((group) => (
        <div key={group.id}>
          <p>Name: {group.name}</p>
          <p>Description: {group.description}</p>
        </div>
      ))}
    </div>
  );
};

export default GroupsPage;
