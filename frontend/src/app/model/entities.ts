export interface Account {
    id: number,
    name: string,
    currentBalance: number,
    accountType: string,
    parentAccount: string|null,
    children: string[]
  }
  
export interface TopLevelAccounts {
    [accountType: string]: Account[]
}

export interface Transaction {
    id: number,
    transactionDate: Date,
    description: string,
    amount: number,
    debitAccount: {id: number, name: string},
    creditAccount: {id: number, name: string},
    transferAccount?: {id: number, name: string},
    balance?: number

}
  