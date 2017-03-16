package com.tiqwab.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by nm on 3/15/17.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
