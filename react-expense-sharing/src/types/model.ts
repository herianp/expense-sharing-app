export interface AuthenticationResponse {
  personDto: Person;
  token: string;
}

export interface Person {
  id?: number;
  username?: string;
  password?: string;
  email?: string;
  role?: string;
  debtList?: Debt[];
  expenseList?: Expense[];
  groupList?: Group[];
  personFriends?: PersonFriend[];
}

export interface PersonFriend {
  id: number;
  personId: number;
  friendEmail: string;
}

export interface Debt {
  id: number;
  amount: number;
  createdAt: string | Date;
  description: string;
  dueDate: string | Date; 
  personIdToPayBack: number;
  personNameToPayBack: string;
  person: Person;
}

export interface Expense {
  id: number;
  amount: number;
  createdAt: string | Date; 
  description: string;
  person: Person;
}

export interface Group {
  id: number;
  name: string;
  description: string;
  createdAt: string | Date; 
  personList: Person[];
}

export interface LoginResponse {
  person: Person;
  token: string;
}
