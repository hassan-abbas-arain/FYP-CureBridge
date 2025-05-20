package com.cure.bridge.repo;

import com.cure.bridge.entity.DoctorVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorVerificationRepo extends CrudRepository<DoctorVerification,Long> {
}
