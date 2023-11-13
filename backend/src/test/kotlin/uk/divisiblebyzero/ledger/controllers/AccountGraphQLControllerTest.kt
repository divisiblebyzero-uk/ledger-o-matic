package uk.divisiblebyzero.ledger.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.Test
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.service.AccountRepository
import uk.divisiblebyzero.ledger.service.TransactionRepository
import java.util.*

class AccountGraphQLControllerTest {
    val accountRepository: AccountRepository = mockk()
    val transactionRepository: TransactionRepository = mockk()
    val accountGraphQLController: AccountGraphQLController = AccountGraphQLController(accountRepository, transactionRepository)
    val account: Account = Account("Account Name", AccountType.EQUITY, null, false)

    @Test
    fun whenFindOneByName_thenReturnAccount() {
        every { accountRepository.findById(1)} returns Optional.of(account)

        val result = accountGraphQLController.account(1)

        verify(exactly = 1) { accountRepository.findById(1)}
        Assert.assertEquals(Optional.of(account), result)

    }
}
