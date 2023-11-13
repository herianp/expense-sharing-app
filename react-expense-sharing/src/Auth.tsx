import React, { useState, useEffect } from "react";
import { getPersonByEmailRequest, setAuthHeader } from "./axios/axiosRequests";
import { Person } from "./components/model";

interface AuthProps {
  // Definice pro props
}

const AuthContent: React.FC<AuthProps> = (props) => {
  const [data, setData] = useState<Person | undefined>();

  useEffect(() => {
    getPersonByEmailRequest("petr@seznam.cz")
      .then((data) => {
        setData(data);
      })
      .catch((error) => {
        console.error("Error in useEffect:", error);
      });
  }, []);

  return (
    <div>
      <h1>Auth</h1>
      <p>User email: {data?.email}</p>
    </div>
  );
};

export default AuthContent;
