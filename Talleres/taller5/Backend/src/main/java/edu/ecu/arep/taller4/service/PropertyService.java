package edu.ecu.arep.taller4.service;

import edu.ecu.arep.taller4.Exception.PropertyNotFound;
import edu.ecu.arep.taller4.model.Property;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PropertyService {
    public void saveProperty(Property property);
    public void UpdateProperty(Long id, Property property) throws PropertyNotFound;
    public void deleteProperty(Long id) throws PropertyNotFound;
    public List<Property> findAllProperty();
    public Property findByIdProperty(Long id) throws PropertyNotFound;
}
