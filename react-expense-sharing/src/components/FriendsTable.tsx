import React from "react";
import { useStore } from "../store/store";
import { PersonFriend } from "../types/model";
import closeIcon from "../img/close.png";

interface FriendsTableProps {
    titleOfTable: string,
    list: PersonFriend[],
    columns: string[]
}

function deleteFriend(Id: number){

}

const FriendsTable: React.FC<FriendsTableProps> = ({titleOfTable, list, columns}) => {
  const friends = useStore((state) => state.friends);
  const deleteFriend = useStore((state) => state.deleteFriend);

  const handleDeleteFriend = async (event: React.FormEvent, email: string) => {
    event.preventDefault();
    console.log("Deleting friend");
    await deleteFriend(email);
  };
  return (
    <div className="debts-container">
      <h1 style={{ textAlign: "center" }}>{titleOfTable}</h1>
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
                  {record.username}
                  </th>
                  <td>{record.friendEmail}</td>
                  <td>
                    <img
                      src={closeIcon}
                      alt="close"
                      style={{ width: "20px", cursor: "pointer" }}
                      onClick={(event) => handleDeleteFriend(event, record.friendEmail)}
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

export default FriendsTable;
