package uk.divisiblebyzero.ledger.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.model.Transaction
import java.math.BigDecimal
import java.time.LocalDate

@Service
class FakeDataCreator(val accountRepository: AccountRepository, val accountService: AccountService, val transactionRepository: TransactionRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)
    fun createAccounts() {
        val assets: Account = accountRepository.save(Account("Current Assets", AccountType.ASSET))
        accountRepository.save(Account("Bank Account (Current)", AccountType.ASSET, assets))
        accountRepository.save(Account("Bank Account (Savings)", AccountType.ASSET, assets))

        accountRepository.save(Account("Opening Balances", AccountType.EQUITY))

        val bills: Account = accountRepository.save(Account("Bills", AccountType.EXPENSE))
        accountRepository.save(Account("Gas / Electric", AccountType.EXPENSE, bills))
        accountRepository.save(Account("ISP", AccountType.EXPENSE, bills))

        val luxuries: Account = accountRepository.save(Account("Luxuries", AccountType.EXPENSE))
        accountRepository.save(Account("Clothes", AccountType.EXPENSE, luxuries))
        accountRepository.save(Account("Dining Out", AccountType.EXPENSE, luxuries))

        accountRepository.save(Account("Interest", AccountType.EXPENSE))

        accountRepository.save(Account("Salary", AccountType.INCOME))

        accountRepository.save(Account("Credit Card", AccountType.LIABILITY))
        accountRepository.save(Account("Mortgage", AccountType.LIABILITY))

        logger.info("Count of accounts: " + accountRepository.count())

        accountRepository.findAll().forEach { logger.info("Account: " + it.toString()) }
        accountService.accountHierarchy()
    }

    fun addTransaction(dayOfMonth: Int, description: String, amount: BigDecimal, debitAccount: String, creditAccount: String) {
        transactionRepository.save(Transaction(
            transactionDate = LocalDate.now().withDayOfMonth(dayOfMonth),
            description = description,
            amount = amount,
            debitAccount = accountRepository.findOneByName(debitAccount),
            creditAccount = accountRepository.findOneByName(creditAccount)
        ))
    }
    fun createFakeData() {
        logger.info("Saving dummy data")
        createAccounts()

        addTransaction(1, "Opening balance", BigDecimal(1000), debitAccount = "Bank Account (Current)", creditAccount = "Opening Balances")
        addTransaction(1, "Opening balance", BigDecimal(250000), debitAccount = "Opening Balances", creditAccount = "Mortgage")

        addTransaction(2, "Salary", BigDecimal(1000), debitAccount = "Bank Account (Current)", creditAccount = "Salary")
        addTransaction(3, "Dinner at restaurant", BigDecimal(100), debitAccount = "Dining Out", creditAccount = "Bank Account (Current)")

        addTransaction(15, "Mortgage interest", BigDecimal(250), debitAccount = "Interest", creditAccount = "Mortgage")
        addTransaction(16, "Mortgage payment", BigDecimal(400), debitAccount = "Mortgage", creditAccount = "Bank Account (Current)")

        transactionRepository.findAll().forEach {
            logger.info("Transaction record: $it")
        }

        transactionRepository.accountLedgerBetweenDates(accountRepository.findOneByName("Bank Account (Current)"), LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(15)).forEach {
            logger.info("$it")
        }
    }
}