package com.example.reactive_dashboard.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Societe {
    @Id
    private String id;
    private String name;
    private double price;
}
