import React, {ChangeEvent, useState} from "react";
import {useStore} from "../store/store";
import {DebtDTO, ErrorResponse, Expense, ExpenseDTO, Person, PersonFriend} from "../types/model";


interface AddDebtFormProps {
}

const AddDebtForm: React.FC<AddDebtFormProps> = () => {
    const [description, setDescription] = useState("");
    const [amount, setAmount] = useState("");
    const [selectedFriendId, setSelectedFriendId] = useState("");

    const addDebt = useStore(state => state.addDebt)
    const addDebtWithExpense = useStore(state => state.addDebtWithExpense)
    const person = useStore((state) => state.person);
    const friends = useStore((state) => state.friends);

    const handleAddDebt = async (event: React.FormEvent) => {
        event.preventDefault();
        console.log("Creating debt");
        console.log("Person id: " + person.id);
        console.log("Friend id: " + selectedFriendId);
        var debtDto: DebtDTO = {
            amount: Number(amount),
            createdAt: undefined,
            description: description,
            personId: person.id,
            dueDate: undefined,
            personIdToPayBack: Number(selectedFriendId),
            personNameToPayBack: undefined,
            groupId: undefined
        }
        // await addDebt(debtDto);
        await addDebtWithExpense(debtDto);
        setSelectedFriendId(""); // Clear the email input field
        setAmount("");
        setDescription("");
    };

    return (
        <form onSubmit={handleAddDebt}>
            <div className="form-group mb-3">
                <label htmlFor="select" className="form-label">
                    Komu dlužím
                </label>
                <select className="form-select mb-3" aria-label=".form-select-lg example"
                        value={selectedFriendId}
                        onChange={(e: ChangeEvent<HTMLSelectElement>) => setSelectedFriendId(e.target.value)}
                        required>
                    <option value="">Nezvoleno</option>
                    {/* Přidána možnost pro "nezvoleno" */}
                    {friends.map((item: PersonFriend, index: number) => (
                        // Loop over each item and return an option element for it.
                        <option key={index} value={item.friendId}>
                            {item.username}
                        </option>
                    ))}
                </select>
                <div className="form-group">
                    <label htmlFor="descriptionArea" className="form-label">
                        Popis
                    </label>
                    <textarea
                        value={description}
                        className="form-control"
                        id="descriptionArea"
                        rows={3}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="amount" className="form-label">
                        Částka
                    </label>
                    <input
                        type="number"
                        value={amount}
                        className="form-control"
                        id="amount"
                        min={0}
                        onChange={(e) => setAmount(e.target.value)}
                        required
                    />
                </div>
            </div>
            <div className="d-grid">
                <button type="submit" className="btn btn-dark btn-lg">
                    Vytvořit dluh
                </button>
            </div>
        </form>
    );
};

export default AddDebtForm;
