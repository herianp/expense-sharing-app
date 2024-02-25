package com.herian.expensesharingapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person_friends")
public class PersonFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "friend_id")
    private Long friendId;

    @Column(name = "friend_email")
    private String friendEmail;


}
