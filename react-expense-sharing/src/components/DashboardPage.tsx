import React from "react";
import { useStore } from "../store/store";

interface DashboardProps {
  // Definice pro props
}

const DashboardPage: React.FC<DashboardProps> = (props) => {
  const person = useStore((state) => state.person);

  // useEffect(() => {
  //   getPersonByEmailRequest("honza@seznam.cz")
  //     .then((data) => {
  //       setData(data);
  //     })
  //     .catch((error) => {
  //       console.error("Error in useEffect:", error);
  //     });
  // }, [data]);

  return (
    <div>
      <h1>DashBoard</h1>
      <p>User Name: {person.username}</p>
      <p>User Email: {person.email}</p>
    </div>
  );
};

export default DashboardPage;
