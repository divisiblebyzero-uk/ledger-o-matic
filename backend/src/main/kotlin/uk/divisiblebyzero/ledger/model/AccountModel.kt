package uk.divisiblebyzero.ledger.model

import jakarta.persistence.*


enum class AccountType(val id: String, val debitDirection: Int, val creditDirection: Int) {
    ASSET("asset", 1, -1),
    EQUITY("equity", -1, 1),
    EXPENSE("expense", 1, -1),
    INCOME("income", -1, 1),
    LIABILITY("liability", -1, 1),
}
@Entity
class Account(var name: String,
                   var accountType: AccountType,
                   @ManyToOne(fetch=FetchType.EAGER)
                   @JoinColumn(name = "parentId")
                   var parentAccount: Account? = null,

                   var placeholder: Boolean = false,
                   @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?=null) {
    override fun toString(): String {
        return "Account(name='$name', accountType=$accountType, parentAccount=$parentAccount, placeholder=$placeholder, id=$id)"
    }
}
