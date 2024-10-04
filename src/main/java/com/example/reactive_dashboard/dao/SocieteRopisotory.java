package com.example.reactive_dashboard.dao;

import com.example.reactive_dashboard.entities.Societe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SocieteRopisotory extends ReactiveMongoRepository<Societe, String> {
}
