package com.cure.bridge.repo;

import com.cure.bridge.entity.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends CrudRepository<Appointment,Long> {
    List<Appointment> findByPatientId(Long id);
    List<Appointment> findByDoctorId(Long id);


}
