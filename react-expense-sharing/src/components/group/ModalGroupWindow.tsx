import React, {useState} from "react";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import {useStore} from "../../store/store";

interface ModalGroupWindowProps {
    show: boolean;
    onHide: () => void;
    groupName: string | undefined;
    handleFetchGroup: () => void,
    addMemberToGroup: (memberEmail: string | undefined) => void;
}

const ModalGroupWindow: React.FC<ModalGroupWindowProps> = ({
                                                               show, onHide,
                                                               groupName,
                                                               addMemberToGroup,
                                                               handleFetchGroup
                                                           }) => {
    const [memberEmail, setMemberEmail] = React.useState('');
    const [isEmailValid, setIsEmailValid] = useState(false);
    const friends = useStore((state) => state.friends);
    const [selectedFriendEmail, setSelectedFriendEmail] = useState("");



    const handleAddMember = () => {
        if (selectedFriendEmail != undefined && selectedFriendEmail != "") {
            addMemberToGroup(selectedFriendEmail);
            onHide();
            handleFetchGroup()
            setSelectedFriendEmail('');
        }
    };

    const validateEmail = (email: string) => {
        // Basic validation for demonstration; consider more comprehensive validation for production
        const isValid = email.length > 0 && /\S+@\S+\.\S+/.test(email);
        setIsEmailValid(isValid);
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Přidání člena do skupiny</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                        <Form.Label>E-mail</Form.Label>
                        <Form.Select
                            aria-label="Select friend"
                            value={selectedFriendEmail}
                            onChange={(e) => setSelectedFriendEmail(e.target.value)}
                            required
                        >
                            <option value="">Nezvoleno</option> {/* Option for "none selected" */}
                            {friends.map((item, index) => (
                                <option key={index} value={item.friendEmail}>
                                    {item.username}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Zrušit
                </Button>
                <Button variant="primary" onClick={handleAddMember}>
                    Přidat
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalGroupWindow;
