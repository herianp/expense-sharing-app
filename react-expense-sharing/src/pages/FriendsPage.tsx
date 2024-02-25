import React, { useState } from "react";
import { useStore } from "../store/store";
import "../css/Friend.css";
import FriendsTable from "../components/FriendsTable";
import { TIMEOUT } from "dns";
import AddFriendForm from "../components/AddFriendForm";

interface FriendsProps {
  // Definice pro props
}

const FriendsPage: React.FC<FriendsProps> = (props) => {
  const [email, setEmail] = useState("");

  const addFriend = useStore((state) => state.addFriend);
  const friends = useStore((state) => state.friends);

  const handleAddFriend = async (event: any) => {
    event.preventDefault();
    console.log("Create friend")
    await addFriend(email);
    setEmail("");
  };

  // Similar for debts and expenses

  return (
    <div className="base-container">
      <AddFriendForm />

      <FriendsTable titleOfTable={"Přátelé"} list={friends} columns={["Username","E-mail"]}/> 
    </div>
  );
};

export default FriendsPage;
