export interface AuthenticationResponse {
  personDto: Person;
  token: string;
}

export interface ErrorResponse {
  code?: number;
  errorMessage?: string;
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
  username: string;
  friendId: number;
  friendEmail: string;
}

export interface FinancialRecord {
  id: number;
  amount: number;
  createdAt: string | Date;
  description: string;
  person: Person;
  dueDate: string | Date;
}

export interface Debt extends FinancialRecord {
  personIdToPayBack: number;
  personNameToPayBack: string;
}

export interface Expense extends FinancialRecord {
  personIdWhoIsPay: number;
  personNameWhoIsPay: string;
}

export interface ExpenseDTO {
  id?: number;
  amount?: number;
  createdAt?: string | Date;
  description?: string;
  personId?: number;
  dueDate?: string | Date;
  personIdWhoIsPay?: number;
  personNameWhoIsPay?: string;
  groupId?: number;
}

export interface DebtDTO {
  id?: number;
  amount?: number;
  createdAt?: string | Date;
  description?: string;
  personId?: number;
  dueDate?: string | Date;
  personIdToPayBack?: number;
  personNameToPayBack?: string;
  groupId?: number;
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
