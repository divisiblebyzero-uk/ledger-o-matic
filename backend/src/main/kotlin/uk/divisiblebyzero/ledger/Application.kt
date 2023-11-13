package uk.divisiblebyzero.ledger

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import uk.divisiblebyzero.ledger.service.FakeDataCreator


@SpringBootApplication
open class Application {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    public open fun createDummyData(fakeDataCreator: FakeDataCreator) = CommandLineRunner {
        fakeDataCreator.createFakeData()
        fakeDataCreator.printResults()
    }
}
fun main() {
    runApplication<Application>()
}