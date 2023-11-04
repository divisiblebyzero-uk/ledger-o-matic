package uk.divisiblebyzero.ledger.service

import org.springframework.data.jpa.repository.JpaRepository
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.model.Transaction


interface AccountRepository:JpaRepository<Account, Long> {
    abstract fun findByParentAccountIsNull(): List<Account>
    abstract fun findByParentAccountIsNullAndAccountType(accountType: AccountType): List<Account>
    abstract fun findByParentAccount(account:Account): List<Account>

    abstract fun findOneByName(name: String):Account
}

interface TransactionRepository:JpaRepository<Transaction, Long> {

}