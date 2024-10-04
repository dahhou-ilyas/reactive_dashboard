package com.example.reactive_dashboard;

import com.example.reactive_dashboard.dao.SocieteRopisotory;
import com.example.reactive_dashboard.dao.TransactionRepository;
import com.example.reactive_dashboard.entities.Societe;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

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
                    .map(name -> new Societe(name,name,generateRandomPrice()));
        };
    }

    private double generateRandomPrice() {
        return 100 + RANDOM.nextDouble() * 900;
    }
}
