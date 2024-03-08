import React, { useState } from "react";
import {useStore} from "../../store/store";
import {ErrorResponse} from "../../types/model";


interface AddGroupFormProps {}

const AddGroupForm: React.FC<AddGroupFormProps> = () => {
  const [groupName, setGroupName] = useState("");
  const [errorResponse, setErrorResponse] = useState<ErrorResponse | null>(null);

  const createGroup = useStore((state) => state.createGroup);
  const person = useStore((state) => state.person);

  const handleCreateGroup = async (event: React.FormEvent) => {
    event.preventDefault();
    console.log("Creating friend");

    // Clear previous error responses
    setErrorResponse(null);
    let result;
    if (person.id != undefined){
      result = await createGroup(groupName,person.id);
    }
    if (result) {
      // If there is an error response, update state to reflect that
      setErrorResponse(result);
      console.error('Error adding friend:', result.errorMessage);
      // Here you could also handle the display of the error message to the user
    } else {
      console.log('Group added successfully');
      // Friend added, you could update UI or state as needed
    }

    setGroupName(""); // Clear the email input field
  };

  return (
    <form onSubmit={handleCreateGroup}>
      <h1>Vytvoření skupiny</h1>
        <div className="mb-3">
          <label htmlFor="groupName" className="form-label">
            Název
          </label>
          <input
            type="text"
            value={groupName}
            className="form-control"
            id="groupName"
            onChange={(e) => setGroupName(e.target.value)}
            required
          />
        </div>
        {errorResponse && <p>Error: {errorResponse.errorMessage}</p>}
        <div className="d-grid">
          <button type="submit" className="btn btn-dark btn-lg">
            Vytvořit
          </button>
        </div>
      </form>
  );
};

export default AddGroupForm;
