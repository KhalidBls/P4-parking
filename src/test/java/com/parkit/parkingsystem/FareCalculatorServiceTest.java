package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {
		
	
	
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    
    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }
    
    
    
    @Test
    @DisplayName("Il y a une réduction de 5% pour les utilisateurs récurrents")
    public void calculateFare_WhenRecurringUsers() {
    	
    	ParkingSpotDAO parkingDAO = Mockito.mock(ParkingSpotDAO.class);
    	  Date inTime = new Date();
          inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
          Date outTime = new Date();
          ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

          ticket.setInTime(inTime);
          ticket.setOutTime(outTime);
          ticket.setParkingSpot(parkingSpot);
          ticket.setVehicleRegNumber("ABCDEF");
          
          
          
      	  when(parkingDAO.getRowsCountWithSameVehiculeNumber(ticket.getVehicleRegNumber())).thenReturn(1); 
          
      	  fareCalculatorService.setParkingDAO(parkingDAO);
      	  
          fareCalculatorService.calculateFare(ticket);
          
          verify(parkingDAO).getRowsCountWithSameVehiculeNumber(ticket.getVehicleRegNumber());
          assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR*0.95);
    } 
    


    @Test
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot); 
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }
    
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
    	//Given 
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //When
        fareCalculatorService.calculateFare(ticket);
        //Then
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    @DisplayName("Prix d'un véhicule qui resterait plus d'une journée")
    public void calculateFareCarWithMoreThanADayParkingTime(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    @DisplayName("Le prix est gratuit pour tout véhicule qui reste moins d'une demi heure")
    public void calculateFare_WhenParkingTimeIsLessThanThirtyMinutes() {
    	 //GIVEN
    	 Date inTime = new Date();
         inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );
         Date outTime = new Date();
         ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

         ticket.setInTime(inTime);
         ticket.setOutTime(outTime);
         ticket.setParkingSpot(parkingSpot);
         
         //WHEN
         fareCalculatorService.calculateFare(ticket);
         
         //THEN
         assertEquals(0.0 , ticket.getPrice() );
    }
    
 

    
    
    
    
    
    
    
    
    
    
    
    
    
}
