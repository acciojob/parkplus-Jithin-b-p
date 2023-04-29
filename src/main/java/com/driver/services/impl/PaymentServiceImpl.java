package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.Reservation;
import com.driver.model.PaymentMode;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Autowired
    SpotRepository spotRepository;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int amountTobePaid = (reservation.getNumberOfHours() * reservation.getSpot().getPricePerHour());

        if(amountSent < amountTobePaid){
            throw new Exception("Insufficient Amount");
        }

        try{

            PaymentMode paymentMode = PaymentMode.valueOf(mode);

            Payment payment = new Payment();
            payment.setPaymentCompleted(true);
            payment.setPaymentMode(paymentMode);
            payment.setReservation(reservation);

            reservation.setPayment(payment);
            reservation.getSpot().setOccupied(true);
            spotRepository.save(reservation.getSpot());

            paymentRepository2.save(payment);
            reservationRepository2.save(reservation);
            return payment;

        }catch (Exception e){
            throw  new Exception("Payment mode not detected");
        }


    }
}
