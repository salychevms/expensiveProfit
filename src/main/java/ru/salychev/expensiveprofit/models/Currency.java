package ru.salychev.expensiveprofit.models;

import jakarta.persistence.*;
/*
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
*/

/*this model describes currency types (names)*/
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_generator")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    public Currency() {
    }

    public Currency(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
