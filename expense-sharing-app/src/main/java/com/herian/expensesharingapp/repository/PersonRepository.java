package com.herian.expensesharingapp.repository;

import com.herian.expensesharingapp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByEmail(String email);

    Person findOneByEmail(String email);
}
