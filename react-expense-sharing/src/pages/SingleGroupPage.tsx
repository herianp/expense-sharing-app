import React, {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {ErrorResponse, GroupForSinglePageDto} from "../types/model";
import GroupPersonsTable from "../components/group/GroupPersonsTable";
import {useStore} from "../store/store";
import AddExpenseToGroupForm from "../components/group/AddExpenseToGroupForm";
import ExpenseGroupTable from "../components/group/ExpenseGroupTable";
import ModalGroupWindow from "../components/group/ModalGroupWindow";
import {debounce} from 'lodash';

interface SingleGroupProps {
    // Definice pro props
}

const SingleGroupPage: React.FC<SingleGroupProps> = (props) => {
    const [group, setGroup] = useState<GroupForSinglePageDto>({});
    const [showAddMemberModal, setShowAddMemberModal] = useState(false);
    const [errorMemberGroupDuplication, setErrorMemberGroupDuplication] = useState('');
    const [shouldFetchGroup, setShouldFetchGroup] = useState(true);

    const location = useLocation();
    const navigate = useNavigate();

    const fetchGroupById = useStore((state) => state.fetchGroupById);
    const deleteGroup = useStore((state) => state.deleteGroup);
    const person = useStore((state) => state.person);
    const addMemberToGroup = useStore((state) => state.addMemberToGroup);
    const deleteMemberFromGroup = useStore((state) => state.deleteMemberFromGroup);
    const personListInGroup = useStore((state) => state.personListInGroup);
    const calculateDebtsInGroup = useStore((state) => state.calculateDebtsInGroup);

    const fetchGroup = async () => {
        const fetchResult = await fetchGroupById(location.state.groupId);
        if (fetchResult && 'id' in fetchResult) { // Assuming 'id' is a property of GroupForSinglePageDto
            setGroup(fetchResult);
        } else {
            console.error('Failed to fetch group or group data is not available');
        }
    }

    const handleChildAction = () => {
        console.log('Data received from child.. and component can be rerendred by fetchGroup');
        fetchGroup();
    };

    async function handleDeleteGroup(groupName: string | undefined, personId: number | undefined) {
        if (groupName != undefined && personId != undefined) {
            await deleteGroup(groupName, personId);
            navigate("/dashboard");
        }
    }

    async function handleCalculateDebtsInGroup() {
        fetchGroup();
        await calculateDebtsInGroup(group.name!, person.id!);
        navigate("/dashboard")

    }

    async function handleAddMemberToGroup(memberEmail: string | undefined) {
        if (personListInGroup.length > 0) {
            if (personListInGroup.every(item => item.email != memberEmail)) {
                setErrorMemberGroupDuplication('');
                await addMemberToGroup(group.name, memberEmail)
            } else {
                setErrorMemberGroupDuplication(memberEmail + " je již členem skupiny.")
            }
        }
    }

    async function handleDeleteMemberFromGroup(memberEmail: string | undefined) {
        personListInGroup.map(item => item.email == memberEmail)
        {
            try {
                await deleteMemberFromGroup(group.name, memberEmail)
            } catch (error) {
                console.error('Failed handleDeleteMemberFromGroup: ' + error);
            }
        }

    }

    async function handleFetchGroup() {
        console.log("changed ...")
        setShouldFetchGroup(!shouldFetchGroup);
    }

    useEffect(() => {
        console.log("and now after changed ...")
        fetchGroup();
    }, [shouldFetchGroup]);

    return (
        <div className="base-container">
            <ModalGroupWindow
                show={showAddMemberModal}
                onHide={() => setShowAddMemberModal(false)}
                groupName={group.name}
                handleFetchGroup={() => handleFetchGroup}
                addMemberToGroup={(memberEmail) => handleAddMemberToGroup(memberEmail)}/>
            <div className="single-group-container">
                <div style={{marginBottom: "2%"}}>
                    <h1 style={{textAlign: "center"}}>{group.name}</h1>
                    <hr style={{border: "2px solid black"}}/>
                </div>
                <div className="group-tables">
                    {
                        group.personNames &&
                        <GroupPersonsTable titleOfTable={"Členové"} singleGroup={group} columns={["Username"]}
                                           groupOwnerId={group.groupOwnerId}
                                           handleFetchGroup={() => handleFetchGroup}
                                           removeMemberFromGroup={(memberEmail) =>
                                               handleDeleteMemberFromGroup(memberEmail)
                                           }/>
                    }
                    {
                        group.id &&
                        <AddExpenseToGroupForm onChildAction={handleChildAction} groupId={group.id}/>
                    }
                    {
                        group.expenseList &&
                        <ExpenseGroupTable columns={["Username", "Popis", "Datum vytvoření"]} list={group.expenseList}
                                           titleOfTable={"Výdaje skupiny"}/>
                    }
                </div>
                <div className="group-buttons">
                    <div>
                        <button className="btn btn-dark btn-lg" onClick={() => {
                            setShowAddMemberModal(true)
                        }}>
                            Přidat člena
                        </button>
                        {person.id == group.groupOwnerId &&
                            <button className="btn btn-danger btn-lg"
                                    onClick={() => handleDeleteGroup(group.name, person.id)}>
                                Smazat skupinu
                            </button>
                        }
                    </div>
                    {((person.id == group.groupOwnerId) && group.expenseList?.length! > 0) &&
                        <button className="btn btn-success btn-lg"
                                onClick={() => handleCalculateDebtsInGroup()}>
                            Rozpočítat dluhy skupiny
                        </button>
                    }
                </div>
                {errorMemberGroupDuplication && <p>{errorMemberGroupDuplication}</p>}
            </div>
        </div>
    );
};

export default SingleGroupPage;
