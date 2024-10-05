package com.example.reactive_dashboard.web;

import com.example.reactive_dashboard.dao.SocieteRopisotory;
import com.example.reactive_dashboard.dao.TransactionRepository;
import com.example.reactive_dashboard.entities.Societe;
import com.example.reactive_dashboard.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class TransactionReactiveController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SocieteRopisotory societeRepository;


    @GetMapping(value = "/transactions")
    public Flux<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    @GetMapping(value = "/transactions/{id}")
    public Mono<Transaction> getOne(@PathVariable String id){
        return transactionRepository.findById(id);
    }

    @PostMapping("/transactions")
    public Mono<Transaction> save(@RequestBody Transaction transaction){

        Mono<Transaction> transactionMono = societeRepository.findById(transaction.getSociete().getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Société non trouvée !")))
                .flatMap(societe -> {
                    transaction.setSociete(societe);
                    return transactionRepository.save(transaction);
                });
        return transactionMono;
    }

    @DeleteMapping(value = "/transactions/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return transactionRepository.deleteById(id);
    }

    @PutMapping(value = "/transactions/{id}")
    public Mono<Transaction> update(@PathVariable String id, @RequestBody Transaction transaction){
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }
}
