package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.model.User;
import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        if(!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()){
            throw new Exception("Cannot make reservation");
        }

        User user = userRepository3.findById(userId).get();
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        Spot spot = null;
        int amount = Integer.MAX_VALUE;
        for(Spot spotInParkingLot: parkingLot.getSpotList()){

            if(!spotInParkingLot.isOccupied()){

                int amountOfCurrentSpot = spotInParkingLot.getPricePerHour() * timeInHours;

                if(spotInParkingLot.getSpotType().equals(SpotType.OTHERS) && !spotInParkingLot.isOccupied()){

                    if(amountOfCurrentSpot < amount){
                        amount = amountOfCurrentSpot;
                        spot = spotInParkingLot;

                    }
                }else if(spotInParkingLot.getSpotType().equals(SpotType.FOUR_WHEELER) && !spotInParkingLot.isOccupied()){

                    if(numberOfWheels == 2 || numberOfWheels == 4){

                        if(amountOfCurrentSpot < amount){
                            amount = amountOfCurrentSpot;
                            spot = spotInParkingLot;

                        }
                    }
                }else{

                    if(numberOfWheels == 2 && !spotInParkingLot.isOccupied()){

                        if(amountOfCurrentSpot < amount){
                            amount = amountOfCurrentSpot;
                            spot = spotInParkingLot;

                        }
                    }
                }
            }
        }

        if(spot == null){
            throw new Exception("Cannot make reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);

        reservation.setUser(user);
        reservation.setSpot(spot);

        user.getReservationList().add(reservation);
        spot.getReservationList().add(reservation);
        spot.setOccupied(true);

        spotRepository3.save(spot);
        userRepository3.save(user);
        return reservationRepository3.save(reservation);

    }
}
