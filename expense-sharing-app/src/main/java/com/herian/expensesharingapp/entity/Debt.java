package com.herian.expensesharingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private Date createdAt;
    private String description;
    private Date dueDate;
    private Long personIdToPayBack;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
