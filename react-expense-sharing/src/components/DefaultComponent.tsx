import React from "react";
import { useStore } from "../store/store";


interface DefaultComponentProps {}

const DefaultComponent: React.FC<DefaultComponentProps> = () => {
  const person = useStore((state) => state.person);

  return (
    <div>
      
    </div>
  );
};

export default DefaultComponent;
