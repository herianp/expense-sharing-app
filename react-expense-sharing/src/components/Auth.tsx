import React from "react";
import { useStore } from "../store/store";

interface AuthProps {
  // Definice pro props
}

const AuthContent: React.FC<AuthProps> = (props) => {
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
      <h1>Auth</h1>
      <p>User email: {person.email}</p>
    </div>
  );
};

export default AuthContent;
