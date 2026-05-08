package com.example.PRISM.repository;

import com.example.PRISM.entity.MachineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MachineRepository extends JpaRepository<MachineEntity,Long> {
    Page<MachineEntity> findAll(Pageable pageable);

    Optional<MachineEntity> findByCode(String code);
}