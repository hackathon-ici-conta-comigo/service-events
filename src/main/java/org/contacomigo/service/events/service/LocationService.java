package org.contacomigo.service.events.service;

import org.contacomigo.service.events.domain.Location;
import org.contacomigo.service.events.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);
    
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Save a location.
     *
     * @param location the entity to save
     * @return the persisted entity
     */
    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        Location result = locationRepository.save(location);
        return result;
    }

    /**
     *  Get all the locations.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Location> findAll() {
        log.debug("Request to get all Locations");
        List<Location> result = locationRepository.findAll();

        return result;
    }

    /**
     *  Get one location by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Location findOne(String id) {
        log.debug("Request to get Location : {}", id);
        Location location = locationRepository.findOne(id);
        return location;
    }

    /**
     *  Delete the  location by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.delete(id);
    }
}
