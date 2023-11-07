package uk.divisiblebyzero.ledger.controllers

import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.model.Transaction
import uk.divisiblebyzero.ledger.service.AccountRepository
import uk.divisiblebyzero.ledger.service.TransactionRepository
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

}