package uk.divisiblebyzero.ledger.config

import graphql.language.StringValue
import graphql.schema.*
import graphql.schema.idl.RuntimeWiring
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import java.time.LocalDate
import java.time.format.DateTimeParseException


@Configuration
class GraphQLConfiguration {
    private val logger = LoggerFactory.getLogger(javaClass)


    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder -> wiringBuilder.scalar(dateScalar()) }
    }

    fun dateScalar(): GraphQLScalarType {
        logger.info("Setting up scalar type")
        return GraphQLScalarType.newScalar()
            .name("Date")
            .description("Java 8 LocalDate as scalar.")
            .coercing(object : Coercing<LocalDate, String> {
                override fun serialize(dataFetcherResult: Any): String {
                    return (dataFetcherResult as? LocalDate)?.toString()
                        ?: throw CoercingSerializeException("Expected a LocalDate object.")
                }

                override fun parseValue(input: Any): LocalDate {
                    return try {
                        if (input is String) {
                            LocalDate.parse(input as String)
                        } else {
                            throw CoercingParseValueException("Expected a String")
                        }
                    } catch (e: DateTimeParseException) {
                        throw CoercingParseValueException(
                            String.format("Not a valid date: '%s'.", input), e
                        )
                    }
                }

                override fun parseLiteral(input: Any): LocalDate {
                    return if (input is StringValue) {
                        try {
                            LocalDate.parse((input as StringValue).getValue())
                        } catch (e: DateTimeParseException) {
                            throw CoercingParseLiteralException(e)
                        }
                    } else {
                        throw CoercingParseLiteralException("Expected a StringValue.")
                    }
                }
            }).build()
    }
}