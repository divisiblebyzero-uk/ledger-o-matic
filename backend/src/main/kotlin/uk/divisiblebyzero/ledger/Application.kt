package uk.divisiblebyzero.ledger

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.service.AccountRepository
import uk.divisiblebyzero.ledger.service.AccountService


@SpringBootApplication
open class Application {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    public open fun createDummyData(accountRepository: AccountRepository, accountService: AccountService) = CommandLineRunner {
        logger.info("Saving dummy data")
        val assets: Account = accountRepository.save(Account("Current Assets", AccountType.ASSET, null, true))
        accountRepository.save(Account("Bank Account (Current)", AccountType.ASSET, assets, false))
        accountRepository.save(Account("Bank Account (Savings)", AccountType.ASSET, assets, false))

        accountRepository.save(Account("Opening Balances", AccountType.EQUITY, null, false))

        logger.info("Count of accounts: " + accountRepository.count())

        accountRepository.findAll().forEach { logger.info("Account: " + it.toString()) }

        //logger.info("Assets account children: ${assets.children}")

        accountService.accountHierarchy()
    }
}
fun main() {
    runApplication<Application>()
}