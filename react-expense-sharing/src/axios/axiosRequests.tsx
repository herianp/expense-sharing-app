import axios from "axios";

export const getPersonByEmailRequest = async (email: string) => {
  const url = `http://localhost:8080/api/person/${email}`;
  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
};

export const setAuthHeader = (token: string | null) => {
  if (token === null) {
    window.localStorage.removeItem("auth_token");
  } else {
    window.localStorage.setItem("auth_token", token);
  }
};

export const login = async (username: string, password: string) => {
  try {
    const response = await axios.post("/api/login", { username, password });
    const token = response.data.token;
    localStorage.setItem("token", token);
    // Nastavení hlavičky pro budoucí požadavky
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } catch (error) {
    console.error("Login failed:", error);
    // Zpracování chyb
  }
};

export const logout = () => {
  localStorage.removeItem("token");
  delete axios.defaults.headers.common["Authorization"];
};
