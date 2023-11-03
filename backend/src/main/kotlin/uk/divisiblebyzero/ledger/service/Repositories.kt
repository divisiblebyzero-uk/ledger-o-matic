package uk.divisiblebyzero.ledger.service

import org.springframework.data.jpa.repository.JpaRepository
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType


interface AccountRepository:JpaRepository<Account, Long> {
    abstract fun findByParentAccountIsNull(): List<Account>
    abstract fun findByParentAccountIsNullAndAccountType(accountType: AccountType): List<Account>
    abstract fun findByParentAccount(account:Account): List<Account>

}