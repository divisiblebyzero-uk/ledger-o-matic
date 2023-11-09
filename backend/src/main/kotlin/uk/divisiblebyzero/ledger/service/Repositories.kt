package uk.divisiblebyzero.ledger.service

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountLedger
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.model.Transaction
import java.math.BigDecimal
import java.time.LocalDate


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
    @Query("select coalesce(sum(amount), 0) from Transaction where creditAccount = ?1 and transactionDate<?2")
    abstract fun sumCreditTransactionsByAccountAtStartOfDate(account:Account, date:LocalDate):BigDecimal
    @Query("select coalesce(sum(amount), 0) from Transaction where debitAccount = ?1 and transactionDate<?2")
    abstract fun sumDebitTransactionsByAccountAtStartOfDate(account:Account, date:LocalDate):BigDecimal
    @Query("select t from Transaction t where creditAccount = ?1 or debitAccount = ?1")
    abstract fun transactionsForAccount(account:Account):List<Transaction>
    @Query("select t from Transaction t where (creditAccount = ?1 or debitAccount = ?1) and transactionDate >= ?2 and transactionDate <= ?3")
    abstract fun transactionsForAccountBetweenDates(account: Account, fromDate: LocalDate, toDate: LocalDate): List<Transaction>

    @Query(value = "select new uk.divisiblebyzero.ledger.model.AccountLedger(ca as ledgerAccount, t as transaction, t.transactionDate as ledgerDate, t.description as description, cast(0.0 as BigDecimal) as debitAmount, amount as creditAmount, da as transferAccount) from Transaction t, Account ca, Account da where t.creditAccount=ca and t.debitAccount=da and t.creditAccount=?1 and t.transactionDate >= ?2 and t.transactionDate <= ?3" +
            "UNION " +
            "select new uk.divisiblebyzero.ledger.model.AccountLedger(da as ledgerAccount, t as transaction, t.transactionDate as ledgerDate, t.description as description, amount as debitAmount, cast(0.0 as BigDecimal) as creditAmount, ca as transferAccount) from Transaction t, Account ca, Account da where t.creditAccount=ca and t.debitAccount=da and t.debitAccount=?1 and t.transactionDate >= ?2 and t.transactionDate <= ?3"
            //"where (creditAccount = ?1 or debitAccount = ?1) and transactionDate >= ?2 and transactionDate <= ?3"
            )
    abstract fun accountLedgerBetweenDates(account: Account, fromDate: LocalDate, toDate: LocalDate): List<AccountLedger>

    @Query(value = "select ?1 as ledgerAccount, t as transaction, t.transactionDate as ledgerDate, t.description as description, 0 as debitAmount, 0 as creditAmount, ?1 as transferAccount from Transaction t ")
    abstract fun testIt(account: Account, fromDate: LocalDate, toDate: LocalDate): List<Array<Any>>

}