package com.bookacab.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<String> locations = new ArrayList<>();

    public void UserService() {
    }

    public List<String> fetchDriverLocation() {
        int size = locations.size();
        if (size > 5) {
            locations = locations.subList(locations.size() - 5, locations.size());
        }
        return locations;
    }

    @KafkaListener(topics = "location", groupId = "user-group-1")
    public void fetchDriverLocation(String location) {
        locations.add(location);
    }

}
