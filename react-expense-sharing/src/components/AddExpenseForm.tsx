import React, {ChangeEvent, useState} from "react";
import {useStore} from "../store/store";
import {ErrorResponse, Expense, ExpenseDTO, Person, PersonFriend} from "../types/model";


interface AddExpenseFormProps {
}

const AddExpenseForm: React.FC<AddExpenseFormProps> = () => {
    const [description, setDescription] = useState("");
    const [amount, setAmount] = useState("");
    const [selectedFriendId, setSelectedFriendId] = useState("");

    const addExpense = useStore(state => state.addExpense)
    const addExpenseWithDebt = useStore(state => state.addExpenseWithDebt)
    const person = useStore((state) => state.person);
    const friends = useStore((state) => state.friends);

    const handleAddExpense = async (event: React.FormEvent) => {
        event.preventDefault();
        console.log("Creating expense");
        console.log("Person id: " + person.id);
        console.log("Friend id: " + selectedFriendId);
        var expenseDto: ExpenseDTO = {
            amount: Number(amount),
            createdAt: undefined,
            description: description,
            personIdWhoHasToBePayed: person.id,
            dueDate: undefined,
            personIdWhoIsPay: Number(selectedFriendId),
            personNameWhoIsPay: undefined,
            groupId: undefined
        }
        // await addExpense(expenseDto);
        await addExpenseWithDebt(expenseDto);
        setSelectedFriendId(""); // Clear the email input field
        setAmount("");
        setDescription("");
    };

    return (
        <form onSubmit={handleAddExpense}>
            <div className="form-group mb-3">
                <label htmlFor="select" className="form-label">
                    Kdo mi dluží
                </label>
                <select className="form-select mb-3" aria-label=".form-select-lg example"
                        value={selectedFriendId}
                        onChange={(e: ChangeEvent<HTMLSelectElement>) => setSelectedFriendId(e.target.value)}
                        id="select"
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
                Vytvořit pohledávku
                </button>
            </div>
        </form>
    );
};

export default AddExpenseForm;
