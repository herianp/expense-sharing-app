package com.herian.expensesharingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="my_person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique=true)
    private String username;
    @NotEmpty
    private String password;
    @Email
    @NotEmpty
    @Column(unique=true)
    private String email;
    private String role;

    @OneToMany(mappedBy = "person")
    private List<Debt> debtList;

    @OneToMany(mappedBy = "person")
    private List<Expense> expenseList;

    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "person_group",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groupList = new ArrayList<>();
}
