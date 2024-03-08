import axios, {AxiosResponse} from "axios";

export interface AuthenticationResponse {
  personDto: Person;
  token: string;
}

export interface ErrorResponse {
  code?: number;
  errorMessage?: string;
}
// same as PersonDTO on Backend
export interface Person {
  id?: number;
  username?: string;
  password?: string;
  email?: string;
  role?: string;
  debtList?: DebtDTO[];
  expenseList?: ExpenseDTO[];
  groupList?: GroupDTO[];
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
  personIdWhoHasToBePayed?: number;
  personNameWhoHasToBePayed?: string;
  dueDate?: string | Date;
  personIdWhoIsPay?: number;
  personNameWhoIsPay?: string;
  groupId?: number;
  status?: string;
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
  status?: string;
}

export interface Group {
  id?: number;
  name?: string;
  description?: string;
  groupOwnerId?: number;
  createdAt?: string | Date;
  personList?: Person[];
  debtList?: Debt[];
  expenseList?: Expense[];
}
export interface GroupDTO {
  id?: number;
  name?: string;
  description?: string;
  groupOwnerId?: number;
  createdAt?: string | Date;
  personIds?: number[];
  debtIds?: number[];
  expenseIds?: number[];
}

export interface GroupForSinglePageDto{
  id?: number;
  name?: string;
  description?: string;
  groupOwnerId?: number;
  createdAt?: string | Date;
  personNames?: string[];
  personEmails?: string[];
  debtList?: DebtDTO[];
  expenseList?: ExpenseDTO[];
}

export interface LoginResponse {
  person: Person;
  token: string;
}

export interface statusDoneDto{
  expenseId: number;
  debtId: number;
}
