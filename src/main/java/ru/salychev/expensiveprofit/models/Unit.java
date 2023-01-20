package ru.salychev.expensiveprofit.models;

import jakarta.persistence.*;

/*this model describes units of measuring (kg, hour, meter and e.t.c.)*/
@Entity
@Table(name = "unit")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_generator")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Unit() {
    }

    public Unit(String name) {
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
