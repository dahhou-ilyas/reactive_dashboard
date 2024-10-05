package com.example.reactive_dashboard.dao;

import com.example.reactive_dashboard.entities.Societe;
import com.example.reactive_dashboard.entities.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findBySociete(Societe societe);
}
