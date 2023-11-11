# Ledger-o-Matic back end

## GraphQL
### Sample queries

    query getAccountById {
        account(id:1) {
            id
            name
            children {
                id
                name
            }
        }
    }

    query getTopLevelAccounts {
        topLevelAccounts(accountType:EQUITY) {
            id
            name
            accountType
            parentAccount {
                id
                name
                accountType
            }
        }
    }

    query getAllAccounts {
        accounts {
            id
            name
            accountType
            parentAccount {
                id
                name
                accountType
            }
        }
    }

    query ledger{
        accountLedger(accountId:2,fromDate:"2023-11-01",toDate:"2023-11-15"){
            ledgerAccount{id, name}
            transaction{id,description}
            description
            ledgerDate
            debitAmount
            creditAmount
            transferAccount{id,name}
            runningTotal
        }
    }


    query getTransactionsForAccount{
        transactionsForAccount(accountId:2) {
            id
            transactionDate
            description
            amount
            debitAccount{name}
            creditAccount{name}
        }
    }

    query getAllTransactions {
        transactions{
            id
            transactionDate
            description
            amount
            debitAccount{name}
            creditAccount{name}
        }
    }

    query sumCredits {
        sumCredits(accountType:"INCOME",fromDate:"2023-11-01",toDate:"2023-11-30") {
            account {
               id, name
            }
            amount
        }
    }
    
    query sumDebits {
        sumDebits(accountType:"EXPENSE",fromDate:"2023-11-01",toDate:"2023-11-30") {
            account {
               id, name
            }
            amount
        }
    }
