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
)

@Entity
open class AccountLedger (
    @ManyToOne
    val ledgerAccount: Account,
    @OneToOne
    val transaction: Transaction,
    val description: String,
    val ledgerDate: LocalDate,
    val debitAmount: BigDecimal,
    val creditAmount: BigDecimal,
    @ManyToOne
    val transferAccount: Account,
    var runningTotal: BigDecimal,
    @Id
    val id: Long? = null
) {
    override fun toString(): String {
        return "AccountLedger(ledgerAccount=${ledgerAccount.name}, transaction=${transaction.id}, description='$description', ledgerDate=$ledgerDate, debitAmount=$debitAmount, creditAmount=$creditAmount, transferAccount=${transferAccount.name}, runningTotal=$runningTotal, id=$id)"
    }
}

interface AccountTotal {
    abstract fun getAccount(): Account
    abstract fun getAmount(): BigDecimal
}