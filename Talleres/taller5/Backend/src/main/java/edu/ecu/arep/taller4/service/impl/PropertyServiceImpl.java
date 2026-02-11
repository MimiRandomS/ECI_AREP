package edu.ecu.arep.taller4.service.impl;

import edu.ecu.arep.taller4.Exception.PropertyNotFound;
import edu.ecu.arep.taller4.model.Property;
import edu.ecu.arep.taller4.repository.PropertyRepository;
import edu.ecu.arep.taller4.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public void saveProperty(Property property) {
        propertyRepository.save(property);
    }

    @Override
    public void UpdateProperty(Long id, Property property) {
        Property existing = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFound("Property with id: " + id + " not found"));

        existing.setAddress(property.getAddress());
        existing.setPrice(property.getPrice());
        existing.setSize(property.getSize());
        existing.setDescription(property.getDescription());

        propertyRepository.save(existing);
    }


    @Override
    public void deleteProperty(Long id) {
        if (propertyRepository.findById(id).isEmpty()) throw new PropertyNotFound("Property with id: " + id + " doesn't found");
        propertyRepository.deleteById(id);
    }

    @Override
    public List<Property> findAllProperty() {
        return propertyRepository.findAll();
    }

    @Override
    public Property findByIdProperty(Long id) throws PropertyNotFound {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFound("Property with id: " + id + " not found"));
    }

}
