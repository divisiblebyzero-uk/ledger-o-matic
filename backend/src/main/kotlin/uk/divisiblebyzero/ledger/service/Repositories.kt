package uk.divisiblebyzero.ledger.service

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.model.Transaction
import java.math.BigDecimal


interface AccountRepository:JpaRepository<Account, Long> {
    abstract fun findByParentAccountIsNull(): List<Account>
    abstract fun findByParentAccountIsNullAndAccountType(accountType: AccountType): List<Account>
    abstract fun findByParentAccount(account:Account): List<Account>

    abstract fun findOneByName(name: String):Account
}

interface TransactionRepository:JpaRepository<Transaction, Long> {
    @Query("select coalesce(sum(amount), 0) from Transaction where creditAccount = ?1")
    abstract fun sumCreditTransactionsByAccount(account:Account):BigDecimal
    @Query("select coalesce(sum(amount), 0) from Transaction where debitAccount = ?1")
    abstract fun sumDebitTransactionsByAccount(account:Account):BigDecimal

    @Query("select t from Transaction t where creditAccount = ?1 or debitAccount = ?1")
    abstract fun transactionsForAccount(account:Account):List<Transaction>
}