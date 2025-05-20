package com.cure.bridge.repo;

import com.cure.bridge.entity.Doctor;
import com.cure.bridge.entity.DoctorStatus;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    List<Doctor> findByStatus(DoctorStatus doctorStatus);
    Doctor findByUserId(long id);
}
