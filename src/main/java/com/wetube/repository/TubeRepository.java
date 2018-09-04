package com.wetube.repository;

import com.wetube.domain.entities.Tube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TubeRepository extends JpaRepository<Tube, Integer> {
}
