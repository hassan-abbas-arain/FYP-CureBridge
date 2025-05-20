package com.cure.bridge.repo;

import com.cure.bridge.entity.MedicalReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalReportRepo extends CrudRepository<MedicalReport,Long> {
}
