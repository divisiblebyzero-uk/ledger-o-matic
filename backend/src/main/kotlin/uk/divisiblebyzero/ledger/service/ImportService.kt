package uk.divisiblebyzero.ledger.service

import org.apache.commons.csv.CSVFormat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType
import uk.divisiblebyzero.ledger.model.GnuCashAccount
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringReader

@Service
class ImportService(val accountRepository: AccountRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun convertType(accountTypeString: String): AccountType {
        return AccountType.valueOf(accountTypeString)
    }
    fun getParent(g: GnuCashAccount): Account? {
        var path = g.fullAccountName.split(":")
        if (path.size == 1) {
            logger.info("Returning null")
            return null;
        }
        logger.info("Looking for ${path.get(path.size-2)}")
        return accountRepository.findOneByName(path.get(path.size-2))
    }
    fun convertToAccount(g: GnuCashAccount): Account {
        logger.info("Looking up path: ${g.fullAccountName}")
        val parent: Account? = getParent(g)
        val acc: Account = Account(name = g.accountName, accountType = convertType(g.type), placeholder = (g.placeholder=="T"), parentAccount = parent)
        accountRepository.save(acc)
        return acc;
    }
    fun importAccountsFromCsv(csvString: String? = null, inputStream: InputStream? = null): List<Account> {
        logger.info("Importing accounts from CSV")
        val reader = if (csvString != null) StringReader(csvString) else InputStreamReader(inputStream)
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.build().parse(reader).drop(1)
            .map {
                GnuCashAccount(
                    type = it[0],
                    fullAccountName = it[1],
                    accountName = it[2],
                    accountCode = it[3],
                    accountDescription = it[4],
                    accountColour = it[5],
                    notes = it[6],
                    symbol = it[7],
                    namespace = it[8],
                    hidden = it[9],
                    taxInfo = it[10],
                    placeholder = it[11]
                )
            }.map {
                convertToAccount(it)
            }
    }
}