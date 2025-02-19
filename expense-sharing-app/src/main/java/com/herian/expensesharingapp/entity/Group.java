package com.herian.expensesharingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
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
    @NotNull
    @Column(nullable = false, name = "group_owner_id")
    private Long groupOwnerId;
    private LocalDateTime createdAt;
    //I deleted fetch eager for fields bellow
    @ManyToMany(mappedBy = "groupList")
    @JsonIgnore
    private List<Person> personList;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenseList;

    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Debt> debtList;
}
