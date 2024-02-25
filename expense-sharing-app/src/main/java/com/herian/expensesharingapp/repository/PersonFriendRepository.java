package com.herian.expensesharingapp.repository;

import com.herian.expensesharingapp.entity.PersonFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonFriendRepository extends JpaRepository<PersonFriend, Long> {

    @Query("SELECT p FROM PersonFriend p WHERE p.person.id = :personId")
    List<PersonFriend> findPersonFriendListByPersonId(Long personId);

    Optional<PersonFriend> findByPerson_Id(Long id);
    Optional<PersonFriend> findById(Long id);
    Optional<PersonFriend> findByFriendEmail(String email);
}
