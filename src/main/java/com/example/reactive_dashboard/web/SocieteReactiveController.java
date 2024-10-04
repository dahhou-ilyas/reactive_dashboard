package com.example.reactive_dashboard.web;

import com.example.reactive_dashboard.dao.SocieteRopisotory;
import com.example.reactive_dashboard.entities.Societe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
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

}
