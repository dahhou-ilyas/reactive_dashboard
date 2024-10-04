package com.example.reactive_dashboard;

import com.example.reactive_dashboard.dao.SocieteRopisotory;
import com.example.reactive_dashboard.dao.TransactionRepository;
import com.example.reactive_dashboard.entities.Societe;
import com.example.reactive_dashboard.entities.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.stream.Stream;

@SpringBootApplication
public class ReactiveDashboardApplication {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        SpringApplication.run(ReactiveDashboardApplication.class, args);
    }

    @Bean
    CommandLineRunner start(SocieteRopisotory societeRopisotory, TransactionRepository transactionRepository) {
        return args -> {
            String[] societeNames = {"SG", "AWB", "BMCE", "AXA", "ATTIJARI", "BCP", "MAROC TELECOM", "LABEL VIE",
                    "COSUMAR", "MARSA MAROC", "LAFARGEHOLCIM", "TAQA MOROCCO", "MANAGEM", "CIH",
                    "ATLANTA", "LESIEUR CRISTAL", "SONASID", "TOTAL MAROC", "RISMA", "HPS"};

            Flux<Societe> societeFlux = Flux.fromArray(societeNames)
                    .map(name -> new Societe(name,name,generateRandomPrice()))
                    .flatMap(societe -> societeRopisotory.save(societe));

            societeFlux.thenMany(societeRopisotory.findAll())
                    .flatMap(this::generateTransactionsForSociete)
                    .flatMap(transaction -> transactionRepository.save(transaction))
                    .subscribe(
                            transaction -> System.out.println("Saved transaction: " + transaction),
                            error -> System.err.println("Error occurred: " + error),
                            () -> System.out.println("Initialization completed!")
                    );
        };
    }

    private Flux<Transaction> generateTransactionsForSociete(Societe societe) {
        return Flux.range(1, 50 + RANDOM.nextInt(51))  // Génère entre 50 et 100 transactions
                .map(i -> {
                    LocalDateTime transactionTime = LocalDateTime.now().minusDays(RANDOM.nextInt(365))
                            .minusHours(RANDOM.nextInt(24))
                            .minusMinutes(RANDOM.nextInt(60));
                    return new Transaction(
                            null,
                            transactionTime.toInstant(ZoneOffset.UTC),
                            generateTransactionPrice(societe.getPrice()),
                            societe
                    );
                });
    }

    private double generateTransactionPrice(double basePrice) {
        return basePrice * (1 + (RANDOM.nextDouble() - 0.5) / 10);
    }
    private double generateRandomPrice() {
        return 100 + RANDOM.nextDouble() * 900;
    }
}
