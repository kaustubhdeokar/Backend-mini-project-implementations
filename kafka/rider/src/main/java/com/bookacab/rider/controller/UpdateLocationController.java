package com.bookacab.rider.controller;

import com.bookacab.rider.service.GeoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rider/update")
public class UpdateLocationController {

    private final GeoDataService geoDataService;

    @Autowired
    public UpdateLocationController(GeoDataService geoDataService) {
        this.geoDataService = geoDataService;
    }


    @PostMapping("/fetch")
    public ResponseEntity<String> sendLocationUpdateOfRider() throws Exception {
        geoDataService.getRandomGeoData();
        return new ResponseEntity<>("Sent", HttpStatus.OK);
    }



}
