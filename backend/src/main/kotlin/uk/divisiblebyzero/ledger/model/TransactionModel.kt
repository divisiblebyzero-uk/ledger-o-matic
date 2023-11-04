package uk.divisiblebyzero.ledger.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class Transaction(
    val transactionDate: LocalDate,
    val description: String,
    val amount: BigDecimal,
    @ManyToOne val debitAccount: Account,
    @ManyToOne val creditAccount: Account,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)