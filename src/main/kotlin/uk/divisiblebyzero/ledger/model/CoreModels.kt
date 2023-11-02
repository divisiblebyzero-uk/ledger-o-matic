package uk.divisiblebyzero.ledger.model

import jakarta.persistence.*


enum class AccountType(val id: String) {
    ASSET("asset"),
    EQUITY("equity"),
    EXPENSE("expense"),
    INCOME("income"),
    LIABILITY("liability"),
}
@Entity
data class Account(var name: String,
                   var accountType: AccountType,
                   @ManyToOne(fetch=FetchType.EAGER)
                   @JoinColumn(name = "parentId")
                   var parentAccount: Account?,

                   var placeholder: Boolean,
                   @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?=null) {

}
