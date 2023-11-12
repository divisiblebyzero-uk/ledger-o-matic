package uk.divisiblebyzero.ledger.service

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uk.divisiblebyzero.ledger.model.*
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

    @Query(value = """
            select  ca as ledgerAccount, 
                    t as transaction,
                    t.transactionDate as ledgerDate, 
                    t.description as description, 
                    case when ca=?1 then t.amount else cast(0.0 as BigDecimal) end as debitAmount, 
                    case when da=?1 then t.amount else cast(0.0 as BigDecimal) end as creditAmount, 
                    da as transferAccount,
                    sum (case when da=?1 then t.amount else cast(0.0 as BigDecimal) end - case when ca=?1 then t.amount else cast(0.0 as BigDecimal) end) 
                      over (order by t.transactionDate asc rows unbounded preceding) as runningTotal
               from Transaction t,
                    Account ca,
                    Account da
              where t.creditAccount=ca
                and t.debitAccount=da
                and (t.creditAccount=?1 or t.debitAccount=?1) 
                and t.transactionDate >= ?2 
                and t.transactionDate <= ?3
              order by t.transactionDate
            """
            )
    abstract fun accountLedgerBetweenDates(account: Account, fromDate: LocalDate, toDate: LocalDate, startingBalance: BigDecimal = BigDecimal.ZERO): List<AccountLedger>

    @Query(value = "select a as account, amount as amount from Transaction t, Account a where t.debitAccount = a and a.accountType = ?1 and t.transactionDate >= ?2 and t.transactionDate <= ?3")
    abstract fun sumDebits(accountType: AccountType, fromDate: LocalDate, toDate: LocalDate): List<AccountTotal>

    @Query(value = "select a as account, amount as amount from Transaction t, Account a where t.creditAccount = a and a.accountType = ?1 and t.transactionDate >= ?2 and t.transactionDate <= ?3")
    abstract fun sumCredits(accountType: AccountType, fromDate: LocalDate, toDate: LocalDate): List<AccountTotal>

    @Query(value = """
        select a as account, 
               sum(case when t.debitAccount = a then amount else 0-amount end) as amount 
          from Transaction t, 
               Account a 
         where (t.creditAccount = a or t.debitAccount = a) and t.transactionDate >= ?1 and t.transactionDate <= ?2
         group by a
        """)
    abstract fun sumByAccount(fromDate: LocalDate, toDate: LocalDate): List<AccountTotal>
}

/*

select a1_0.id,
       a1_0.account_type,
       a1_0.name,
       a1_0.parent_id,
       a1_0.placeholder,
       t1_0.id,
       t1_0.amount,
       t1_0.credit_account_id,
       t1_0.debit_account_id,
       t1_0.description,
       t1_0.transaction_date,
       case when a1_0.id=? then cast(0.0 as numeric(38,2)) else t1_0.amount end,
       case when a2_0.id=? then t1_0.amount else cast(0.0 as numeric(38,2)) end,
       a2_0.id,
       a2_0.account_type,
       a2_0.name,
       a2_0.parent_id,
       a2_0.placeholder
  from transaction t1_0,
       account a1_0,
       account a2_0
 where t1_0.credit_account_id=a1_0.id
   and t1_0.debit_account_id=a2_0.id
   and (t1_0.credit_account_id=? or t1_0.debit_account_id=?)
   and t1_0.transaction_date>=? and t1_0.transaction_date<=?

 */