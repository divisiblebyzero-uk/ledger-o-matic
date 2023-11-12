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
        return transactionRepository.accountLedgerBetweenDates(account.get(), fromDate, toDate);
    }

    @QueryMapping
    fun monthlySummary(@Argument fromDate: LocalDate, @Argument toDate: LocalDate): List<MonthlyAccountTotal> {
        logger.info("Running monthlySummary with fromDate: $fromDate, toDate: $toDate")

        val list: MutableList<MonthlyAccountTotal> = mutableListOf<MonthlyAccountTotal>()

        var monthStart: LocalDate = fromDate.withDayOfMonth(1);
        while (monthStart < toDate) {
            val monthEnd = monthStart.plusMonths(1).minusDays(1)
            logger.info("Running sum for fromDate: $monthStart, toDate: $monthEnd")
            list.add(MonthlyAccountTotal(monthStart, transactionRepository.sumByAccount(monthStart, monthEnd)))
            monthStart = monthStart.plusMonths(1)
        }
        return list
    }
}