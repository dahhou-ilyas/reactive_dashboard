package com.example.reactive_dashboard.web;

import com.example.reactive_dashboard.dao.SocieteRopisotory;
import com.example.reactive_dashboard.entities.Societe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@CrossOrigin("*")
public class SocieteReactiveController {
    @Autowired
    private SocieteRopisotory societeRopisotory;


    @GetMapping(value = "/societs")
    public Flux<Societe> findAll(){
        return societeRopisotory.findAll();
    }

    @GetMapping(value = "/societs/{id}")
    public Mono<Societe> getOne(@PathVariable String id){
        return societeRopisotory.findById(id);
    }

    @PostMapping("/societs")
    public Mono<Societe> save(@RequestBody Societe societe){
        societe.setId(societe.getName());
        return societeRopisotory.save(societe);
    }

    @DeleteMapping(value = "/societs/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return societeRopisotory.deleteById(id);
    }

    @PutMapping(value = "/societs/{id}")
    public Mono<Societe> update(@PathVariable String id, @RequestBody Societe societe){
        societe.setId(id);
        return societeRopisotory.save(societe);
    }
}
