package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=null;
        List<Spot> spotList=parkingLot.getSpotList();
        for(Spot spot1:spotList){
            if(spot1.getId()==spotId)
                spot=spot1;
        }


        assert spot != null;
        spot.setPricePerHour(pricePerHour);



        spotList.add(spot);
        for (Spot spots : spotList) {
            if (spots.getId() == spotId) {
                spots.setPricePerHour(pricePerHour);
            }
        }
        spot.setParkingLot(parkingLot);

        spotRepository1.save(spot);


        return spot;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

        ParkingLot parkingLot = null;
        if( parkingLotRepository1.existsById(parkingLotId)) {
            parkingLot = parkingLotRepository1.findById(parkingLotId).get();
            List<Spot> spotList = parkingLot.getSpotList();
            for (Spot spot : spotList) {
                if( spotRepository1.existsById(spot.getId())) {
                    deleteSpot(spot.getId());
                }
            }
        }
        parkingLotRepository1.deleteById(parkingLotId);

    }
}
