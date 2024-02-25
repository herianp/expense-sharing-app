import React, { useState } from "react";
import { useStore } from "../store/store";
import { ErrorResponse } from "../types/model";


interface AddFriendFormProps {}

const AddFriendForm: React.FC<AddFriendFormProps> = () => {
  const [email, setEmail] = useState("");
  const [errorResponse, setErrorResponse] = useState<ErrorResponse | null>(null);


  const addFriend = useStore((state) => state.addFriend);
  const friends = useStore((state) => state.friends);

  const handleAddFriend = async (event: React.FormEvent) => {
    event.preventDefault();
    console.log("Creating friend");

    // Clear previous error responses
    setErrorResponse(null);

    const result = await addFriend(email);
    if (result) {
      // If there is an error response, update state to reflect that
      setErrorResponse(result);
      console.error('Error adding friend:', result.errorMessage);
      // Here you could also handle the display of the error message to the user
    } else {
      console.log('Friend added successfully');
      // Friend added, you could update UI or state as needed
    }

    setEmail(""); // Clear the email input field
  };

  return (
    <form onSubmit={handleAddFriend}>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">
            E-mail
          </label>
          <input
            type="email"
            value={email}
            className="form-control"
            id="email"
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        {errorResponse && <p>Error: {errorResponse.errorMessage}</p>}
        <div className="d-grid">
          <button type="submit" className="btn btn-dark btn-lg">
            Přidat přítele
          </button>
        </div>
      </form>
  );
};

export default AddFriendForm;
