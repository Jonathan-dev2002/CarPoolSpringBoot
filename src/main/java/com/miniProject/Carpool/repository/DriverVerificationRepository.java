package com.miniProject.Carpool.repository;

import com.miniProject.Carpool.model.DriverVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.JpaRepositoryNameSpaceHandler;
import org.springframework.stereotype.Repository;

@Repository
public interface DvRepository extends JpaRepository<DriverVerification , String> , JpaSpecificationExecutor<DriverVerification> {

}
