package com.herian.expensesharingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="my_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String description;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "groupList", cascade = { CascadeType.ALL })
    @JsonIgnore
    private List<Person> personList;

    @OneToMany(mappedBy = "group")
    private List<Expense> expenseList;
}
