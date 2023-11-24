import React, { useState } from "react";
import { useStore } from "../store/store";
import { PersonFriend, Debt, Expense } from "../types/model";

const HomePage: React.FC = () => {
  const [email, setEmail] = useState("");

  const addFriend = useStore((state) => state.addFriend);
  const friends = useStore((state) => state.friends);

  const handleAddFriend = async () => {
    await addFriend(email);
    setEmail("");
  };

  // Similar for debts and expenses

  return (
    <div>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Enter friend's email"
      />
      <button onClick={handleAddFriend}>Add Friend</button>
      {friends.map((friend) => (
        <div key={friend.id}>{friend.friendEmail}</div>
      ))}
    </div>
  );
};

export default HomePage;
