package com.cure.bridge.repo;

import com.cure.bridge.entity.Prescription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepo extends CrudRepository<Prescription,Long> {
    Optional<Prescription> findByAppointmentId(Long appointmentId);

}
