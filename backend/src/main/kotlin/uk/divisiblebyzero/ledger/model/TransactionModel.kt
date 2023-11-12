package uk.divisiblebyzero.ledger.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
class Transaction(
    val transactionDate: LocalDate,
    val description: String,
    val amount: BigDecimal,
    @ManyToOne val debitAccount: Account,
    @ManyToOne val creditAccount: Account,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
) {
    override fun toString(): String {
        return "Transaction(transactionDate=$transactionDate, description='$description', amount=$amount, debitAccount=$debitAccount, creditAccount=$creditAccount, id=$id)"
    }
}

interface AccountLedger {
    abstract fun getLedgerAccount(): Account
    abstract fun getTransaction(): Transaction
    abstract fun getDescription(): String
    abstract fun getLedgerDate(): LocalDate
    abstract fun getDebitAmount(): BigDecimal
    abstract fun getCreditAmount(): BigDecimal
    abstract fun getTransferAccount(): Account
    abstract fun getRunningTotal(): BigDecimal

}

interface AccountTotal {
    abstract fun getAccount(): Account
    abstract fun getAmount(): BigDecimal
}

open class MonthlyAccountTotal (
    val month: LocalDate,
    val accountTotals: List<AccountTotal>
)