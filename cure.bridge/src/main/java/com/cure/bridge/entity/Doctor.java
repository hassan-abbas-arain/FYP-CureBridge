package com.cure.bridge.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
@Entity
@Table(name = "doctors")
@Getter
@Setter
public class Doctor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String specialization;

    @Column(unique = true, nullable = false)
    private String licenseNumber;

    private int experienceYears;
    private String contactNumber;
    private String clinicAddress;
    private String degreePdfPath;
    private String specializationList;
    @Enumerated(EnumType.STRING)
    private DoctorStatus status = DoctorStatus.PENDING;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    private Timestamp verifiedAt;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
}