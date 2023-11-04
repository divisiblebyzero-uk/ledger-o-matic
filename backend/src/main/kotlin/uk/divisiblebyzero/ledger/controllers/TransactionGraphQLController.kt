package uk.divisiblebyzero.ledger.controllers

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
public class TransactionGraphQLController(private val transactionRepository: TransactionRepository) {

    @QueryMapping
    fun transactions(): List<Transaction> {
        return transactionRepository.findAll()
    }
}