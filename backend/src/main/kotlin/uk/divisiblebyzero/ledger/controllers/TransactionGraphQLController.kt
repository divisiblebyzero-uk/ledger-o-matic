package uk.divisiblebyzero.ledger.controllers

import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import uk.divisiblebyzero.ledger.model.*
import uk.divisiblebyzero.ledger.service.AccountRepository
import uk.divisiblebyzero.ledger.service.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Controller
public class TransactionGraphQLController(private val transactionRepository: TransactionRepository, private val accountRepository: AccountRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)
    @QueryMapping
    fun transactions(): List<Transaction> {
        return transactionRepository.findAll()
    }

    @QueryMapping
    fun transactionsForAccount(@Argument accountId: Long): List<Transaction> {
        logger.info("Having a go. ${accountId}")
        val account = accountRepository.findById(accountId);
        logger.info("Found account: ${account}")
        if (account != null) {
            logger.info("Found account really")
            return transactionRepository.transactionsForAccount(account.get())
        } else return ArrayList<Transaction>();
    }

    @QueryMapping
    fun accountLedger(@Argument accountId: Long, @Argument fromDate: LocalDate, @Argument toDate: LocalDate): List<AccountLedger> {
        logger.info("Loading account ledger for ${accountId} between $fromDate and $toDate")
        val account = accountRepository.findById(accountId)
        if (account == null) {
            logger.info("Account not found: $accountId")
            return ArrayList<AccountLedger>()
        }
        var accountLedgers = transactionRepository.accountLedgerBetweenDates(account.get(), fromDate, toDate);
        var startingBalance = transactionRepository.sumCreditTransactionsByAccountAtStartOfDate(account.get(), fromDate).times(BigDecimal(account.get().accountType.creditDirection))
            .plus(transactionRepository.sumDebitTransactionsByAccountAtStartOfDate(account.get(), fromDate).times(BigDecimal(account.get().accountType.debitDirection)))
        var runningTotal = startingBalance;
        accountLedgers.forEach {
            runningTotal = runningTotal.plus(it.creditAmount.times(BigDecimal(account.get().accountType.creditDirection))).plus(it.debitAmount.times(BigDecimal(account.get().accountType.debitDirection)))
            it.runningTotal = runningTotal
        }

        return accountLedgers;
    }

    @QueryMapping
    fun sumCredits(@Argument accountType: AccountType, @Argument fromDate: LocalDate, @Argument toDate: LocalDate): List<AccountTotal> {
        logger.info("Running sumCredits with accountType: $accountType, fromDate: $fromDate, toDate: $toDate")
        return transactionRepository.sumCredits(accountType, fromDate, toDate)
    }
    @QueryMapping
    fun sumDebits(@Argument accountType: AccountType, @Argument fromDate: LocalDate, @Argument toDate: LocalDate): List<AccountTotal> {
        return transactionRepository.sumDebits(accountType, fromDate, toDate)
    }
}