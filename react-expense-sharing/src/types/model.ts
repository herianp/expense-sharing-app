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
  personFriends?: Set<PersonFriend>;
}

export interface PersonFriend {
  id: number;
  person: Person;
  friendEmail: string;
}

export interface Debt {
  id: number;
  amount: number;
  createdAt: string | Date;
  description: string;
  dueDate: string | Date; 
  personIdToPayBack: number;
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
