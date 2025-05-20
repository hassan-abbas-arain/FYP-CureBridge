package com.cure.bridge.repo;

import com.cure.bridge.entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepo extends CrudRepository<Patient,Long> {
    public Patient findByUserId(long id);
}
