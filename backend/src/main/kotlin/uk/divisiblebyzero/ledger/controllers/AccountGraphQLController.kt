package uk.divisiblebyzero.ledger.controllers

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.service.AccountRepository
import uk.divisiblebyzero.ledger.service.TransactionRepository
import java.math.BigDecimal
import java.util.*
import kotlin.time.times

@Controller
public class AccountGraphQLController(val accountRepository: AccountRepository, val transactionRepository: TransactionRepository) {

    @QueryMapping
    fun accounts(): List<Account> {
        return accountRepository.findAll()
    }

    @QueryMapping
    fun account(@Argument id: Long): Optional<Account> {
        return accountRepository.findById(id)
    }

    @QueryMapping
    fun topLevelAccounts(@Argument accountType: AccountType): List<Account> {
        return accountRepository.findByParentAccountIsNullAndAccountType(accountType)
    }

    @SchemaMapping
    fun children(parentAccount: Account): List<Account> {
        return accountRepository.findByParentAccount(parentAccount)
    }

    @SchemaMapping
    fun currentBalance(parentAccount: Account): BigDecimal {
        val credits = transactionRepository.sumCreditTransactionsByAccount(parentAccount)
        val debits = transactionRepository.sumDebitTransactionsByAccount(parentAccount)

        return credits.times(BigDecimal(parentAccount.accountType.creditDirection))
            .plus(debits.times(BigDecimal(parentAccount.accountType.debitDirection)));
    }
}