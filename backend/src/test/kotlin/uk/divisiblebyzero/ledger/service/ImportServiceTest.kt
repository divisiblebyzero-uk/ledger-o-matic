package uk.divisiblebyzero.ledger.service

import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import uk.divisiblebyzero.ledger.Application
import uk.divisiblebyzero.ledger.model.AccountType

@SpringBootTest(classes = arrayOf(Application::class))
@TestPropertySource("/test.properties")
class ImportServiceTest {
    private val logger = LoggerFactory.getLogger(javaClass)

    //val accountRepository: AccountRepository = mockk()
    //val importService: ImportService = ImportService(accountRepository)

    @Autowired
    lateinit var importService: ImportService

    val singleAccountString = "\"Type\",\"Full Account Name\",\"Account Name\",\"Account Code\",\"Description\",\"Account Colour\",\"Notes\",\"Symbol\",\"Namespace\",\"Hidden\",\"Tax Info\",\"Placeholder\"\n" +
            "\"ASSET\",\"TestAccount\",\"TestAccount\",\"\",\"Assets\",\"\",\"\",\"GBP\",\"CURRENCY\",\"F\",\"F\",\"T\"\n"

    @Test
    fun testImportCsvFromString() {
        val accounts = importService.importAccountsFromCsv(singleAccountString)
        Assert.assertEquals(accounts.size, 1)
        Assert.assertEquals(accounts.get(0).name, "TestAccount")
        Assert.assertEquals(accounts.get(0).accountType, AccountType.ASSET)
        Assert.assertEquals(accounts.get(0).placeholder, true)
    }

    @Test
    fun testImportCsvFromClasspathResource() {
        val accounts = importService.importAccountsFromCsv(inputStream = javaClass.getResource("/accounts-gnucash.csv").openStream())
        logger.info("$accounts")

    }
}