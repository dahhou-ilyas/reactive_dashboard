package com.example.reactive_dashboard.web;

import com.example.reactive_dashboard.dao.SocieteRopisotory;
import com.example.reactive_dashboard.dao.TransactionRepository;
import com.example.reactive_dashboard.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;


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

        //dans cette traitemetn on peut mette seulement le id de la societer sans besoin de faire tous les info
        //c'est à dire dans le body de requet on passe le id de la societer et on stocker dans transaction selement le un objet societer qui contienne le id de la societe et les autre attribue null
        //car si on trouve seulement le id on peut connue le societe deouit societer repository
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

    @GetMapping(value = "/stream/transactions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamTransactions(){
        return transactionRepository.findAll();
    }

    @GetMapping(value = "/transactions/societe/{societeId}")
    public Flux<Transaction> transactionsSociete(@PathVariable String societeId) {
        return societeRepository.findById(societeId)
                .flatMapMany(soc -> {
                    Flux<Transaction> bySociete = transactionRepository.findBySociete(soc);
                    return bySociete;
                })
                .switchIfEmpty(Flux.empty()) // Si aucun societe n'est trouvé
                .doOnComplete(() -> System.out.println("Nous avons terminé")); // Callback pour indiquer la fin
    }

    @GetMapping(value = "/stream/transactions/societe/{societeId}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamTransactionsSociete(@PathVariable String societeId) {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        return societeRepository.findById(societeId)
                .flatMapMany(soc -> Flux.zip(interval, transactionRepository.findBySociete(soc)).map(data->data.getT2()))
                .switchIfEmpty(Flux.empty())
                .doOnComplete(() -> System.out.println("Nous avons terminé"));
    }

    @GetMapping(value = "/events/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> events(@PathVariable String id) {

    }
}

class Event {
    private Instant instant;
    private double value;
    private String societeId;

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSocieteId() {
        return societeId;
    }

    public void setSocieteId(String societeId) {
        this.societeId = societeId;
    }
}

