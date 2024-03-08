import {create} from "zustand";
import axios, {AxiosError, AxiosResponse} from "axios";
import {devtools, persist} from "zustand/middleware";
import {
    PersonFriend,
    AuthenticationResponse,
    Person,
    ErrorResponse, ExpenseDTO, DebtDTO, GroupDTO, Group, GroupForSinglePageDto, statusDoneDto,
} from "../types/model";

interface StoreState {
    friends: PersonFriend[];
    debts: DebtDTO[];
    expenses: ExpenseDTO[];
    groups: GroupDTO[],
    person: Person;
    personListInGroup: Person[],
    isAuthenticated: boolean;
    errorResponse: ErrorResponse;
    clearErrorResponse: () => void;
    addFriend: (email: string) => Promise<ErrorResponse | void>;
    addGroup: (groupName: string, personId: string) => Promise<ErrorResponse | void>;
    deleteFriend: (email: string) => Promise<ErrorResponse | void>;
    addDebt: (debtData: DebtDTO) => Promise<ErrorResponse | void>;
    addDebtWithExpense: (debtData: DebtDTO) => Promise<ErrorResponse | void>;
    setDebtAndExpenseStatusToPending: (debtId: number) => Promise<ErrorResponse | void>;
    setExpenseAndDebtStatusToDone: (debtId: number) => Promise<ErrorResponse | void>;
    addExpense: (expenseData: ExpenseDTO) => Promise<ErrorResponse | void>;
    addExpenseWithDebt: (expenseData: ExpenseDTO) => Promise<ErrorResponse | void>;
    fetchGroupById: (groupId: number) => Promise<ErrorResponse | GroupForSinglePageDto | void>;
    createGroup: (groupName: string, personId: number) => Promise<ErrorResponse | void>;
    deleteGroup: (groupName: string, personId: number) => Promise<ErrorResponse | void>;
    addMemberToGroup: (groupName: string | undefined, memberEmail: string | undefined) => Promise<ErrorResponse | void>;
    deleteMemberFromGroup: (groupName: string | undefined, memberEmail: string | undefined) => Promise<ErrorResponse | void>;
    fetchPersonsInGroup: (groupName: string | undefined) => Promise<ErrorResponse | Person | void>;
    calculateDebtsInGroup: (groupName: string, personId: number) => Promise<ErrorResponse | void>;
    login: (email: string, password: string) => Promise<void>;
    resetPassword: (email: string, password: string) => Promise<void>;
    logout: () => void;
    registration: (
        userName: string,
        email: string,
        password: string
    ) => Promise<ErrorResponse | void>;
}

//SET INTERCEPTORS FOR AUTHENTICATION
axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("auth_token");
        if (token && token != undefined) {
            config.headers["Authorization"] = `Bearer ${token}`;
            console.log("Authorization header set:", config.headers["Authorization"]); // Verify the header is set
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export const useStore = create<StoreState>()(
    devtools(
        persist(
            (set, get) =>
                ({
                    friends: [],
                    debts: [],
                    expenses: [],
                    groups: [],
                    person: {},
                    personListInGroup: [],
                    isAuthenticated: false,
                    errorResponse: {},

                    clearErrorResponse: () => {
                        set({errorResponse: {}});
                    },

                    addFriend: async (email) => {
                        try {
                            const response: AxiosResponse<PersonFriend, any> = await axios.post(
                                `http://localhost:8080/api/person/friend/${email}`
                            );
                            set((state) => ({friends: [...state.friends, response.data]}));
                        } catch (error) {
                            console.error("Failed to add friend", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    addGroup: async (groupName, personId) => {
                        try {
                            const response: AxiosResponse<GroupDTO, any> = await axios.post(
                                `http://localhost:8080/api/group/${groupName}/${personId}`
                            );
                            set((state) => ({groups: [...state.groups, response.data]}));
                        } catch (error) {
                            console.error("Failed to create group", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    deleteFriend: async (email) => {
                        try {
                            console.log("Delete friend..");
                            const response: AxiosResponse<PersonFriend, any> = await axios.delete(
                                `http://localhost:8080/api/person/friend/delete/${email}`
                            );

                            set((state) => ({friends: state.friends.filter(friend => friend.friendEmail !== email)}));
                        } catch (error) {
                            console.error("Failed to delete friend", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    addDebt: async (debtData: DebtDTO) => {
                        console.log("Add debts {store}: " + debtData)
                        try {
                            const response: AxiosResponse<DebtDTO, any> = await axios.post(
                                `http://localhost:8080/api/debt`, debtData
                            );
                            set((state) => ({debts: [...state.debts, response.data]}));
                        } catch (error) {
                            console.error("Failed to create debt", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    addDebtWithExpense: async (debtData: DebtDTO) => {
                        console.log("Add debts {store}: " + debtData)
                        try {
                            const response: AxiosResponse<DebtDTO, any> = await axios.post(
                                `http://localhost:8080/api/debt/withExpense`, debtData
                            );
                            set((state) => ({debts: [...state.debts, response.data]}));
                        } catch (error) {
                            console.error("Failed to create debt", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    setDebtAndExpenseStatusToPending: async (debtId) => {
                        try {
                            const response: AxiosResponse<DebtDTO, any> = await axios.post(`http://localhost:8080/api/debt/statusPending/${debtId}`);
                            console.log("ResponseDATA: " + response.data);
                            set((state) => ({
                                debts: state.debts.map(item =>
                                    item.id === debtId ? response.data : item // Update the matching debt, otherwise return the debt unchanged
                                )
                            }));
                            const {debts} = get();
                            set((state) => ({
                                person: {
                                    ...state.person, // Copy all existing person properties
                                    debts: [debts], // Safely update groupList, ensuring it's not undefined
                                },
                            }));
                        } catch (error) {
                            console.error("Failed to set status", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    setExpenseAndDebtStatusToDone: async (expenseId) => {
                        try {
                            const response: AxiosResponse<statusDoneDto, any> = await axios.post(`http://localhost:8080/api/expense/statusDone/${expenseId}`);
                            //DELETE Expense from cache list
                            set((state) => ({
                                expenses: state.expenses.filter(item => item.id !== expenseId)
                            }));
                            const {expenses} = get();
                            set((state) => ({
                                person: {
                                    ...state.person, // Copy all existing person properties
                                    expenses: [expenses], // Safely update groupList, ensuring it's not undefined
                                },
                            }));
                            //Delete debt from cache list
                            set((state) => ({
                                debts: state.debts.filter(item => item.id !== response.data.debtId)
                            }));
                            const {debts} = get();
                            set((state) => ({
                                person: {
                                    ...state.person, // Copy all existing person properties
                                    debts: [debts], // Safely update groupList, ensuring it's not undefined
                                },
                            }));
                        } catch (error) {
                            console.error("Failed to set status", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    addExpense: async (expenseData: ExpenseDTO) => {
                        try {
                            const response: AxiosResponse<ExpenseDTO, any> = await axios.post(
                                `http://localhost:8080/api/expense`, expenseData
                            );
                            set((state) => ({expenses: [...state.expenses, response.data]}));
                        } catch (error) {
                            console.error("Failed to create expense", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    addExpenseWithDebt: async (expenseData: ExpenseDTO) => {
                        try {
                            const response: AxiosResponse<ExpenseDTO, any> = await axios.post(
                                `http://localhost:8080/api/expense/withDebt`, expenseData
                            );
                            set((state) => ({expenses: [...state.expenses, response.data]}));
                        } catch (error) {
                            console.error("Failed to create expense", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    fetchGroupById: async (groupId: number) => {
                        console.log("Fetching group with id: " + groupId);
                        try {
                            const response: AxiosResponse<GroupForSinglePageDto, any> = await axios.get(
                                `http://localhost:8080/api/group/id/${groupId}`
                            );
                            const {fetchPersonsInGroup} = get();
                            fetchPersonsInGroup(response.data.name);
                            return response.data;
                        } catch (error) {
                            console.error("Failed to get group by id", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            throw error;
                        }
                    },

                    createGroup: async (groupName: string, personId: number) => {
                        try {
                            const response: AxiosResponse<GroupDTO, any> = await axios.post(
                                `http://localhost:8080/api/group/${groupName}/${personId}`
                            );
                            set((state) => ({
                                groups: [...state.groups, response.data]
                            }));
                            set((state) => ({
                                person: {
                                    ...state.person, // Copy all existing person properties
                                    groupList: [...state.person.groupList || [], response.data], // Safely update groupList, ensuring it's not undefined
                                },
                            }));
                        } catch (error) {
                            console.error("Failed to create expense", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    deleteGroup: async (groupName: string, personId: number) => {
                        try {
                            const response: AxiosResponse<String, any> = await axios.post(
                                `http://localhost:8080/api/group/delete/${groupName}/${personId}`
                            );
                            set((state) => ({
                                groups: state.groups.filter(group => group.name !== groupName)
                            }));
                            set((state) => ({
                                person: {
                                    ...state.person, // Copy all existing person properties
                                    groupList: state.person.groupList?.filter(group => group.name !== groupName) || [] // Safely update groupList, ensuring it's not undefined
                                },
                            }));
                        } catch (error) {
                            console.error("Failed to create expense", error);
                            if (axios.isAxiosError(error) && error.response) {

                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData;
                                }
                            }
                            throw error;
                        }
                    },

                    addMemberToGroup: async (groupName, memberEmail) => {
                        try {
                            const response: AxiosResponse<Person, any> = await axios.post(
                                `http://localhost:8080/api/group/${groupName}/member/${memberEmail}`
                            );

                            set((state) => ({
                                groups: state.groups.map(group =>
                                    group.name === groupName
                                        ? {
                                            ...group, personIds: [...(group.personIds || []), response.data.id!]
                                        } // Add person to the specific group
                                        : group // Leave other groups unchanged
                                ),
                            }));
                            const {fetchPersonsInGroup} = get();
                            fetchPersonsInGroup(groupName);
                        } catch (error) {
                            console.error("Failed to create expense", error);
                            if (axios.isAxiosError(error) && error.response) {

                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData;
                                }
                            }
                            throw error;
                        }
                    },

                    deleteMemberFromGroup: async (groupName, memberEmail) => {
                        try {

                            const response: AxiosResponse<number, any> = await axios.post(
                                `http://localhost:8080/api/group/delete/${groupName}/member/${memberEmail}`
                            );

                            set((state) => ({
                                groups: state.groups.map(group =>
                                    group.name === groupName
                                        ? {
                                            ...group,
                                            personIds: group.personIds?.filter(personId => personId != response.data) || []
                                        }
                                        : group
                                ),
                            }));
                            const {fetchPersonsInGroup} = get();
                            fetchPersonsInGroup(groupName);

                        } catch (error) {
                            console.error("Failed to create expense", error);
                            if (axios.isAxiosError(error) && error.response) {

                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData;
                                }
                            }
                            throw error;
                        }
                    },

                    fetchPersonsInGroup: async (groupName) => {
                        try {
                            console.log("Fetch personList in group method");
                            const response: AxiosResponse<Person[], any> =
                                await axios.get(`http://localhost:8080/api/person/group/${groupName}`);
                            const response_data: Person[] | null = response.data;
                            if (!response_data) {
                                console.error("No response data");
                                return;
                            }
                            set({personListInGroup: response_data})
                        } catch (error) {
                            console.error("Fetch personList failed:", error);
                        }
                    },

                    calculateDebtsInGroup: async (groupName: string, personId: number) => {
                        try {
                            await axios.post(
                                `http://localhost:8080/api/group/close/${groupName}/person/${personId}`
                            );
                            set((state) => ({
                                groups: state.groups.filter(group => group.name !== groupName)
                            }));
                            set((state) => ({
                                person: {
                                    ...state.person, // Copy all existing person properties
                                    groupList: state.person.groupList?.filter(group => group.name !== groupName) || [] // Safely update groupList, ensuring it's not undefined
                                },
                            }));
                            const responseDebtList: AxiosResponse<DebtDTO[], any> = await axios.get(
                                `http://localhost:8080/api/debt/person/${personId}`
                            );
                            set({debts: responseDebtList.data});

                            const responseExpenseList: AxiosResponse<ExpenseDTO[], any> = await axios.get(
                                `http://localhost:8080/api/expense/person/${personId}`
                            );
                            set({expenses: responseExpenseList.data});

                        } catch (error) {
                            console.error("Failed to calcualte group", error);
                            if (axios.isAxiosError(error) && error.response) {
                                // Check if the error response matches the ErrorResponse type
                                if (error.response.status === 400 || error.response.status === 404) {
                                    const errorData: ErrorResponse = {
                                        code: error.response.status,
                                        errorMessage: error.response.data.errorMessage,
                                    };
                                    return errorData; // Return the error response
                                }
                            }
                            // If it's not an AxiosError or doesn't have a response, throw the error to be caught by the calling function
                            throw error;
                        }
                    },

                    login: async (email, password) => {
                        const url = "http://localhost:8080/api/v1/auth/authenticate";
                        try {
                            console.log("Login method");
                            window.localStorage.removeItem("auth_token");
                            const response: AxiosResponse<AuthenticationResponse, any> =
                                await axios.post(url, {email, password});
                            const response_data: AuthenticationResponse | null = response.data;

                            if (!response_data) {
                                console.error("No response data");
                                return;
                            }
                            console.log("Response data: " + response.data.personDto);
                            const {token, personDto} = response_data;
                            console.log("token: " + token + " | personDto: " + personDto);
                            if (token) {
                                set({person: personDto});
                                set({debts: personDto.debtList});
                                set({expenses: personDto.expenseList});
                                set({groups: personDto.groupList});
                                set({friends: personDto.personFriends});
                                set({isAuthenticated: true});
                                // Optionally set token in localStorage and as the default header
                                window.localStorage.setItem("auth_token", token);
                                axios.defaults.headers.common[
                                    "Authorization"
                                    ] = `Bearer ${token}`;
                                console.log("Token stored and auth data set in the store.");
                            } else {
                                console.error("Token not found in the response data.");
                            }
                        } catch (error) {
                            console.error("Login failed:", error);
                        }
                    },

                    resetPassword: async (email, password) => {
                        const url = "http://localhost:8080/api/v1/auth/resetPassword";
                        try {
                            console.log("ResetPassword method");
                            const response: AxiosResponse<String, any> =
                                await axios.post(url, {email, password});
                            const response_data: String | null = response.data;

                            if (!response_data) {
                                console.error("No response data");
                                return;
                            }
                        } catch (error) {
                            console.error("Reset password failed:", error);
                        }
                    },

                    logout: () => {
                        useStore.persist.clearStorage();
                        localStorage.removeItem("auth_token");
                    },

                    registration: async (userName, email, password) => {
                        const url = "http://localhost:8080/api/v1/auth/register";
                        try {
                            console.log("Registration method...");
                            window.localStorage.removeItem("auth_token");
                            set({errorResponse: {}});
                            const response: AxiosResponse<
                                AuthenticationResponse | ErrorResponse,
                                any
                            > = await axios.post(url, {userName, email, password});
                            const response_data: AuthenticationResponse | ErrorResponse | null =
                                response.data;
                            if (!response_data) {
                                console.error("No response data");
                                return;
                            }
                            if ("personDto" in response_data) {
                                const {personDto, token} =
                                    response_data as AuthenticationResponse;
                                console.log("token: " + token + " | personDto: " + personDto);
                                if (token) {
                                    set({person: personDto});
                                    set({isAuthenticated: true});
                                    // Optionally set token in localStorage and as the default header
                                    window.localStorage.setItem("auth_token", token);
                                    axios.defaults.headers.common[
                                        "Authorization"
                                        ] = `Bearer ${token}`;
                                    console.log("Token stored and auth data set in the store.");
                                } else {
                                    console.error("Token not found in the response data.");
                                }
                            }
                            return
                        } catch (error: unknown) {
                            if (axios.isAxiosError(error) && error.response) {
                                set({errorResponse: error.response.data});
                                console.log("Axios error:", error.response.data);
                                return error.response.data;
                            } else {
                                // The error is not from Axios
                                console.error("Unexpected error:", error);
                            }
                            throw error;
                        }
                    },
                }),
            {
                name: "my-storage", // Name of the storage (used as a key in localStorage)
                getStorage: () => localStorage, // Define the storage type (localStorage or sessionStorage)
            }
        )
    )
);
