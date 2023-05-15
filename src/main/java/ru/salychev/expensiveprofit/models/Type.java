package ru.salychev.expensiveprofit.models;

import jakarta.persistence.*;

/*this model describes types (kinds) of expenses and profits
 * for example: salary, taxes, food store, etc*/
@Entity
@Table(name = "types")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_generator")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Type() {
    }

    public Type(String name) {
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

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
