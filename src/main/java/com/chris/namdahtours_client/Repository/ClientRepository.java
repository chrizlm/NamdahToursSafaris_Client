package com.chris.namdahtours_client.Repository;

import com.chris.namdahtours_client.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Boolean existsByEmail(String email);
}
