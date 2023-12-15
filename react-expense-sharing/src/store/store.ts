import { create } from "zustand";
import axios, { AxiosResponse } from "axios";
import { devtools, persist } from "zustand/middleware";
import {
  PersonFriend,
  Debt,
  Expense,
  AuthenticationResponse,
  Person,
} from "../types/model";

interface StoreState {
  friends: PersonFriend[];
  debts: Debt[];
  expenses: Expense[];
  person: Person;
  isAuthenticated: boolean;
  addFriend: (email: string) => Promise<void>;
  addDebt: (debtData: Debt) => Promise<void>;
  addExpense: (expenseData: Expense) => Promise<void>;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  registration: (userName: string, email: string, password: string) => Promise<void>;
}

export const useStore = create<StoreState>()(
  devtools(
    persist(
      (set) => ({
        friends: [],
        debts: [],
        expenses: [],
        person: {},
        isAuthenticated: false,

        addFriend: async (email) => {
          try {
            const response: AxiosResponse<PersonFriend, any> = await axios.post(
              `http://localhost:8080/api/person/friend/${email}`
            );
            set((state) => ({ friends: [...state.friends, response.data] }));
          } catch (error) {
            console.error("Failed to add friend", error);
            // Handle the error appropriately
          }
        },

        addDebt: async (debtData) => {
          // Similar implementation for debts
        },

        addExpense: async (expenseData) => {
          // Similar implementation for expenses
        },

        login: async (email, password) => {
          const url = "http://localhost:8080/api/v1/auth/authenticate";
          try {
            console.log("Login method");
            window.localStorage.removeItem("auth_token");
            const response: AxiosResponse<AuthenticationResponse, any> =
              await axios.post(url, { email, password });
            const response_data: AuthenticationResponse | null = response.data;

            if (!response_data) {
              console.error("No response data");
              return;
            }
            console.log("Response data: " + response.data.personDto);
            const { token, personDto } = response_data;
            console.log("token: " + token + " | personDto: " + personDto);
            if (token) {
              set({ person: personDto });
              set({ isAuthenticated: true });
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

        logout: () => {
          useStore.persist.clearStorage();
          localStorage.removeItem("auth_token");
        },

        registration: async (userName, email, password) => {
          const url = "http://localhost:8080/api/v1/auth/authenticate/register";
          try {
            console.log("Registration method");
            window.localStorage.removeItem("auth_token");
            const response: AxiosResponse<AuthenticationResponse, any> =
              await axios.post(url, { userName, email, password });
            const response_data: AuthenticationResponse | null = response.data;

            if (!response_data) {
              console.error("No response data");
              return;
            }
            console.log("Response data: " + response.data.personDto);
            const { personDto, token } = response_data;
            console.log("token: " + token + " | personDto: " + personDto);
            if (token) {
              set({ person: personDto });
              set({ isAuthenticated: true });
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
            console.error("Registration failed:", error);
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
