package uk.divisiblebyzero.ledger.model

import jakarta.persistence.*


enum class AccountType(val id: String, val debitDirection: Int, val creditDirection: Int) {
    ASSET("asset", 1, -1),
    EQUITY("equity", -1, 1),
    EXPENSE("expense", 1, -1),
    INCOME("income", -1, 1),
    LIABILITY("liability", -1, 1),
    BANK("bank", 1, -1)
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

/**
 * Represents a GnuCash account as exported by the system. Format taken from the header of a CSV:
 * "Type","Full Account Name","Account Name","Account Code","Description","Account Colour","Notes","Symbol","Namespace","Hidden","Tax Info","Placeholder"
 *
 * Example can be found in /src/test/resources/accounts-gnucash.csv
 */
data class GnuCashAccount(
    val type: String,
    val fullAccountName: String,
    val accountName: String,
    val accountCode: String,
    val accountDescription: String,
    val accountColour: String,
    val notes: String,
    val symbol: String,
    val namespace: String,
    val hidden: String,
    val taxInfo: String,
    val placeholder: String
)
