package edu.ecu.arep.taller4.repository;

import edu.ecu.arep.taller4.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> { }
