package com.ehr.controller;


import com.ehr.model.Ride;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestControllerTest {

    @Test(timeout = 8000)
    public void testCreateRide() {
        RestTemplate restTemplate = new RestTemplate();

        Ride ride = new Ride();
        ride.setName("Green Valley Ride");
        ride.setDuration(38);

        restTemplate.put("http://localhost:8080/ride", ride);
    }

    @Test(timeout = 8000)
    public void testCreateRideByPost() {
        RestTemplate restTemplate = new RestTemplate();

        Ride ride = new Ride();
        ride.setName("Yellow Fort Ride");
        ride.setDuration(12);

        ride = restTemplate.postForObject("http://localhost:8080/ride", ride, Ride.class);
        System.out.println("Ride: " + ride);
    }

    @Test(timeout = 8000)
    public void testGetRides() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Ride>> ridersResponse = restTemplate.exchange(
                "http://localhost:8080/rides", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Ride>>() {}
        );

        List<Ride> rides = ridersResponse.getBody();

        for (Ride ride : rides) {
            System.out.println("Ride name: " + ride.getName());
        }
    }

    @Test(timeout = 8000)
    public void testGetRide() {
        RestTemplate restTemplate = new RestTemplate();

        Ride ride = restTemplate.getForObject(
                "http://localhost:8080/ride/1",
                Ride.class
        );

        System.out.println("Ride name: " + ride.getName());
    }

    @Test(timeout = 8000)
    public void testUpdateRide() {
        RestTemplate restTemplate = new RestTemplate();

        Ride ride = restTemplate.getForObject(
                "http://localhost:8080/ride/2",
                Ride.class
        );
        System.out.println("Ride name: " + ride.getName());
        System.out.println(ride);

        ride.setName("LeMans Ride no." + String.valueOf(1 + (int) (Math.random()*99)));
        ride.setDuration((int) (1 + Math.random()*999));

        restTemplate.put("http://localhost:8080/ride/update/", ride);

        System.out.println("Ride name: " + ride.getName());
        System.out.println(ride);
    }

    @Test(timeout = 8000)
    public void testBatchUpdate() {
        RestTemplate restTemplate = new RestTemplate();


        restTemplate.getForObject(
                "http://localhost:8080/batch/",
                Object.class
        );

        //System.out.println("Ride name: " + ride.getName());
    }

    @Test(timeout = 8000)
    public void testDelete() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.delete("http://localhost:8080/delete/8");
    }

    @Test(timeout = 8000)
    public void testDeleteByParam() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.delete("http://localhost:8080/deleteByParam/7");
    }

    @Test(timeout = 8000)
    public void testException() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getForObject("http://localhost:8080/test/", Ride.class);
    }
}
