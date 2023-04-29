package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        return parkingLotRepository1.save(parkingLot);

    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);

        if(numberOfWheels > 4){
            spot.setSpotType(SpotType.OTHERS);
        }else if(numberOfWheels > 2){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spot.setSpotType(SpotType.TWO_WHEELER);
        }

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spot.setOccupied(false);

        parkingLot.getSpotList().add(spot);

        parkingLotRepository1.save(parkingLot);

        return spot;

    }

    @Override
    public void deleteSpot(int spotId) {

        if(spotRepository1.existsById(spotId)){

            spotRepository1.deleteById(spotId);
        }

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

        if(parkingLotRepository1.findById(parkingLotId).isPresent() && spotRepository1.findById(spotId).isPresent()){

            ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
            Spot spot = spotRepository1.findById(spotId).get();

            spot.setPricePerHour(pricePerHour);

            spotRepository1.save(spot);
            return spot;
        }
        return null;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

        ParkingLot parkingLot = null;
        if(parkingLotRepository1.existsById(parkingLotId)){

            parkingLot = parkingLotRepository1.findById(parkingLotId).get();
            for(Spot spot: parkingLot.getSpotList()){

                spotRepository1.deleteById(spot.getId());

            }

            parkingLotRepository1.deleteById(parkingLotId);

        }

    }
}
