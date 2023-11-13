package uk.divisiblebyzero.ledger.service

import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.Test
import uk.divisiblebyzero.ledger.model.AccountType

class ImportServiceTest {

    val accountRepository: AccountRepository = mockk()
    val importService: ImportService = ImportService(accountRepository)

    val csvString = "\"Type\",\"Full Account Name\",\"Account Name\",\"Account Code\",\"Description\",\"Account Colour\",\"Notes\",\"Symbol\",\"Namespace\",\"Hidden\",\"Tax Info\",\"Placeholder\"\n" +
            "\"ASSET\",\"Assets\",\"Assets\",\"\",\"Assets\",\"\",\"\",\"GBP\",\"CURRENCY\",\"F\",\"F\",\"T\"\n"

    @Test
    fun testImportCsvFromString() {
        val accounts = importService.importAccountsFromCsv(csvString)
        Assert.assertEquals(accounts.size, 1)
        Assert.assertEquals(accounts.get(0).name, "Assets")
        Assert.assertEquals(accounts.get(0).accountType, AccountType.ASSET)
        Assert.assertEquals(accounts.get(0).placeholder, true)
    }
}