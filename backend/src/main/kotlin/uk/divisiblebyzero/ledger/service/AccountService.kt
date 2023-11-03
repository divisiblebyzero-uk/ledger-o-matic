package uk.divisiblebyzero.ledger.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.divisiblebyzero.ledger.model.Account
import uk.divisiblebyzero.ledger.model.AccountType

@Service
class AccountService(val accountRepository: AccountRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)
    fun accountHierarchyDepth(depth:Int, account:Account?=null, accountType: AccountType?=null ) {
        val children:List<Account> = if (account == null) if (accountType == null) accountRepository.findByParentAccountIsNull() else accountRepository.findByParentAccountIsNullAndAccountType(accountType) else accountRepository.findByParentAccount(account)
        children.forEach {
            val indent:String = "\t".repeat(depth);
            logger.info("$indent : ${it.name}")
            accountHierarchyDepth(depth = depth+1, account = it)
        }
    }
    fun accountHierarchy() {
        AccountType.entries.forEach {
            logger.info("Type: ${it.name}")
            accountHierarchyDepth(1, accountType = it)
        }
    }



}