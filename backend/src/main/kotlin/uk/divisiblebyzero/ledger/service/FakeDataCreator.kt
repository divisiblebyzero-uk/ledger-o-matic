package uk.divisiblebyzero.ledger.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.model.Transaction
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class FakeDataCreator(val accountRepository: AccountRepository, val transactionRepository: TransactionRepository) {
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
        accountRepository.save(Account("Interest Income", AccountType.INCOME))

        accountRepository.save(Account("Credit Card", AccountType.LIABILITY))
        accountRepository.save(Account("Mortgage", AccountType.LIABILITY))

        logger.info("Count of accounts: " + accountRepository.count())

        accountRepository.findAll().forEach { logger.info("Account: " + it.toString()) }
    }

    fun addTransaction(date: LocalDate, description: String, amount: BigDecimal, debitAccount: String, creditAccount: String) {
        transactionRepository.save(
            Transaction(
                transactionDate = date,
                description = description,
                amount = amount,
                debitAccount = accountRepository.findOneByName(debitAccount),
                creditAccount = accountRepository.findOneByName(creditAccount)
            )
        )
    }

    fun addRecurringTransaction(startDate: LocalDate, recurringDays: Long, recurringMonths: Long, description: String, amount: BigDecimal, debitAccount: String, creditAccount: String) {
        var date: LocalDate = startDate;
        while (date <= LocalDate.now()) {
            addTransaction(date, description, amount, debitAccount, creditAccount)
            date = date.plusDays(recurringDays)
            date = date.plusMonths(recurringMonths)
        }
    }

    fun addRecurringInterest(startDate: LocalDate, recurringDays: Long, recurringMonths: Long, description: String, interestRate: BigDecimal, debitAccountName: String, creditAccountName: String) {
        var date: LocalDate = startDate;
        var monthlyInterestRate = interestRate.divide(BigDecimal(12), 6, RoundingMode.HALF_UP)
        val debitAccount = accountRepository.findOneByName(debitAccountName)
        var balance = transactionRepository.sumDebitTransactionsByAccountAtStartOfDate(debitAccount, startDate)
        while (date <= LocalDate.now()) {
            val interestAmount = monthlyInterestRate.times(balance);
            balance = balance.plus(interestAmount);
            addTransaction(date, description, interestAmount, debitAccountName, creditAccountName)
            date = date.plusDays(recurringDays)
            date = date.plusMonths(recurringMonths)
        }
    }

    fun createFakeData() {
        logger.info("Saving dummy data")
        createAccounts()

        addTransaction(
            LocalDate.now().withDayOfYear(1),
            "Opening balance",
            BigDecimal(1000),
            debitAccount = "Bank Account (Current)",
            creditAccount = "Opening Balances"
        )
        addTransaction(
            LocalDate.now().withDayOfYear(1),
            "Opening balance",
            BigDecimal(12000),
            debitAccount = "Bank Account (Savings)",
            creditAccount = "Opening Balances"
        )
        addTransaction(
            LocalDate.now().withDayOfYear(1),
            "Opening balance",
            BigDecimal(250000),
            debitAccount = "Opening Balances",
            creditAccount = "Mortgage"
        )

        addRecurringTransaction(LocalDate.now().withDayOfYear(15), 0, 1, "Salary", BigDecimal(1000), debitAccount = "Bank Account (Current)", creditAccount = "Salary")
        addRecurringInterest(LocalDate.now().withDayOfYear(20), 0, 1, "Savings Interest", BigDecimal(0.06), "Bank Account (Savings)", "Interest Income")

        addRecurringTransaction(LocalDate.now().withDayOfYear(15), 0, 1, "Mortgage interest", BigDecimal(100), debitAccount = "Interest", creditAccount = "Mortgage")
        addRecurringTransaction(LocalDate.now().withDayOfYear(20), 0, 1, "Mortgage monthly payment", BigDecimal(150), debitAccount = "Mortgage", creditAccount = "Bank Account (Current)")

        addRecurringTransaction(LocalDate.now().withDayOfYear(2), 0, 1, "ISP Bill", BigDecimal(20), debitAccount = "ISP", creditAccount = "Bank Account (Current)")
        addRecurringTransaction(LocalDate.now().withDayOfYear(21), 0, 1, "Gas / Electic Bill", BigDecimal(90), debitAccount = "Gas / Electric", creditAccount = "Bank Account (Current)")
        addRecurringTransaction(LocalDate.now().withDayOfYear(3), 4, 0, "Dinner at restaurant", BigDecimal(50), debitAccount = "Dining Out", creditAccount = "Bank Account (Current)")
        addRecurringTransaction(LocalDate.now().withDayOfYear(3), 18, 0, "Buying clothes", BigDecimal(80), debitAccount = "Clothes", creditAccount = "Bank Account (Current)")

        addRecurringTransaction(LocalDate.now().withDayOfYear(1), 0, 1, "Monthly savings", BigDecimal(200), debitAccount = "Bank Account (Savings)", creditAccount = "Bank Account (Current)")
    }
    fun printResults() {

        transactionRepository.findAll().forEach {
            logger.info("Transaction record: $it")
        }

        transactionRepository.accountLedgerBetweenDates(
            accountRepository.findOneByName("Bank Account (Current)"),
            LocalDate.now().withMonth(1).withDayOfMonth(1),
            LocalDate.now().withDayOfMonth(15)
        ).forEach {
            logger.info("${it.getLedgerDate()} ${it.getRunningTotal()} :: ${it.getLedgerAccount().name} --> ${it.getTransferAccount().name} ${it.getDebitAmount()} ${it.getCreditAmount()}")
        }

        transactionRepository.sumCredits(
            AccountType.INCOME,
            LocalDate.now().withDayOfMonth(1),
            LocalDate.now().withDayOfMonth(15)
        ).forEach {
            logger.info("${it.getAccount().name} - ${it.getAmount()}")
        }

        transactionRepository.sumDebits(
            AccountType.EXPENSE,
            LocalDate.now().withDayOfMonth(1),
            LocalDate.now().withDayOfMonth(15)
        ).forEach {
            logger.info("${it.getAccount().name} - ${it.getAmount()}")
        }

        transactionRepository.sumByAccount(
            LocalDate.now().withDayOfMonth(1),
            LocalDate.now().withDayOfMonth(15)
        )
    }
}