import axios, { AxiosResponse } from "axios";
import { AuthenticationResponse, Person } from "../components/model";
import { getCookie } from "typescript-cookie";

// INTERCEPTORS
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("auth_token");
    console.log("Interceptors: " + token);
    if (token && token != undefined) {
      config.headers["Authorization"] = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
// HEADERS
export const setAuthHeader = (token: string) => {
  window.localStorage.setItem("auth_token", token);
};

export const getAuthHeader = (token: string | null) => {
  return window.localStorage.getItem("auth_token");
};

// REQUESTS
export const getPersonByEmailRequest = async (email: string) => {
  const url: string = `http://localhost:8080/api/person/${email}`;
  try {
    const response: AxiosResponse<Person, any> = await axios.get<Person>(url);
    console.log("getPersonByEmailRequest response:", response.data);
    return response.data;
  } catch (error) {
    console.log("Error fetching data:", error);
    throw error;
  }
};

export const login = async (email: string, password: string) => {
  const url: string = "http://localhost:8080/api/v1/auth/authenticate";
  try {
    console.log("Login method");
    const response: AxiosResponse = await axios.post(url, { email, password });
    const response_data: AuthenticationResponse | null = response.data;

    if (!response_data) {
      console.error("No response data");
      return;
    }

    const token = response_data.token;
    console.log("Response fetched");

    if (token) {
      // Funkce pro nastavení hlavičky autorizace (definujte podle potřeb)
      setAuthHeader(token);
      console.log("Token stored in localStorage:", token);

      // Nastavení výchozí hlavičky autorizace pro všechny následující požadavky
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    } else {
      console.error("Token not found in the response headers.");
    }
  } catch (error) {
    console.error("Login failed:", error);
  }
};

export const logout = () => {
  window.localStorage.removeItem("auth_token");
  delete axios.defaults.headers.common["Authorization"];
};
