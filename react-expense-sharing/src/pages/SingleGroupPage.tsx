import React from "react";
import { useStore } from "../store/store";

interface SingleGroupProps {
  // Definice pro props
}

const SingleGroupPage: React.FC<SingleGroupProps> = (props) => {
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
      <h1>Single group</h1>
      <p>User email: {person.email}</p>
    </div>
  );
};

export default SingleGroupPage;
