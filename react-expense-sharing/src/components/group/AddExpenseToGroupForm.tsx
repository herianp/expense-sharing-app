import React, {ChangeEvent, useState} from "react";
import {useStore} from "../../store/store";
import {ErrorResponse, Expense, ExpenseDTO, Person, PersonFriend} from "../../types/model";


interface AddExpenseToGroupFormProps {
    groupId: number,
    onChildAction: () => void;
}

const AddExpenseToGroupForm: React.FC<AddExpenseToGroupFormProps> = ({groupId, onChildAction}) => {
    const [description, setDescription] = useState("");
    const [amount, setAmount] = useState("");
    const [selectedMemberId, setSelectedMemberId] = useState("");

    const addExpense = useStore(state => state.addExpense)
    const person = useStore((state) => state.person);
    const friends = useStore((state) => state.friends);
    const personListInGroup = useStore((state) => state.personListInGroup);

    const handleAddExpense = async (event: React.FormEvent) => {
        event.preventDefault();
        var expenseDto: ExpenseDTO = {
            amount: Number(amount),
            createdAt: undefined,
            description: description,
            personIdWhoHasToBePayed: Number(selectedMemberId),
            dueDate: undefined,
            personIdWhoIsPay: undefined,
            personNameWhoIsPay: undefined,
            groupId: groupId
        }
        await addExpense(expenseDto);
        setSelectedMemberId(""); // Clear the email input field
        setAmount("");
        setDescription("");
        onChildAction();
    };

    return (
        <form onSubmit={handleAddExpense}>
            <h2>Vytvořit pohledávku</h2>
            <div className="form-group mb-3">
                <label htmlFor="select" className="form-label">
                    Kdo platil
                </label>
                <select className="form-select mb-3" aria-label=".form-select-lg example"
                        value={selectedMemberId}
                        onChange={(e: ChangeEvent<HTMLSelectElement>) => setSelectedMemberId(e.target.value)}
                        id="select"
                        required>
                    <option value="">Nezvoleno</option>
                    {personListInGroup.map((item) => (
                        // Loop over each item and return an option element for it.
                        <option key={item.id} value={item.id}>
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
                    Vytvořit
                </button>
            </div>
        </form>
    );
};

export default AddExpenseToGroupForm;
