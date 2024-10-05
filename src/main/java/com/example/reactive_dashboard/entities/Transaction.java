package com.example.reactive_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Document
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Transaction {
    @Id
    private String id;
    private Instant instant;
    private Double price;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Societe societe;
}
