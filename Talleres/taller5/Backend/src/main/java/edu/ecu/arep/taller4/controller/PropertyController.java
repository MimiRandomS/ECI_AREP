package edu.ecu.arep.taller4.controller;

import edu.ecu.arep.taller4.Exception.PropertyNotFound;
import edu.ecu.arep.taller4.model.Property;
import edu.ecu.arep.taller4.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Property> saveProperty(@RequestBody Property property){
        propertyService.saveProperty(property);
        return new ResponseEntity<>(property, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> UpdateProperty(@PathVariable Long id, @RequestBody Property property){
        try {
            propertyService.UpdateProperty(id, property);
            return new ResponseEntity<>(property, HttpStatus.OK);
        } catch (PropertyNotFound e){
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id){
        try {
            propertyService.deleteProperty(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PropertyNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Property>> findAllProperty(){
        return new ResponseEntity<>(propertyService.findAllProperty(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdProperty(@PathVariable Long id){
        try {
            return new ResponseEntity<>(propertyService.findByIdProperty(id), HttpStatus.OK);
        } catch (PropertyNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
