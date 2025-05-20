package com.cure.bridge.repo;



import com.cure.bridge.entity.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TranscriptRepository extends CrudRepository<Transcript, Long> {

    Optional<Transcript> findByAppointmentId(String id);
}