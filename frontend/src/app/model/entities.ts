export interface Account {
    id: number,
    name: string,
    currentBalance: number,
    accountType: string,
    parentAccount: string|null,
    children: { id: number }[]
  }
  
export interface TopLevelAccounts {
    [accountType: string]: Account[]
}

export interface Transaction {
    id: number,
    transactionDate: Date,
    description: string,
    amount: number,
    debitAccount: {id: number, name: string, accountType: string},
    creditAccount: {id: number, name: string, accountType: string},
    transferAccount?: {id: number, name: string, accountType: string},
    balance?: number

}

export interface AccountLedger {
    ledgerAccount: {id: number, name: string},
    description: string,
    ledgerDate: Date,
    debitAmount: number,
    creditAmount: number,
    transferAccount: {id: number, name: string},
    runningTotal: number
}