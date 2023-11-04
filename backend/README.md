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
