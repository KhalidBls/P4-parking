package com.parkit.parkingsystem.dao.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;

public class TicketDAOIT {

	static TicketDAO ticketDAO;
	static Ticket ticket;
	static ParkingSpot parkingSpot;
	
	ParkingService parkingService;
	
	@BeforeAll
	public static void setUp() {
		ticketDAO = new TicketDAO();
		ticket = new Ticket();
		parkingSpot = new ParkingSpot(3, ParkingType.CAR,false);
	}
	
	
	
	
	@Test
	public   void savingTicketTest() {
		//GIVEN
		Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
         
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("REGNUMBER");
        ticket.setPrice(15.0);
        
        //WHEN
        boolean isSaved = ticketDAO.saveTicket(ticket);
        
       //THEN
       assertThat(isSaved).isEqualTo(false);
	}
	
	@Test
	public void getTicketTestWithExistingTicket() {
		
		savingTicketTest();
		Ticket ourTicket = ticketDAO.getTicket("REGNUMBER");
		
		assertThat(ticket.getParkingSpot()).isEqualTo(ourTicket.getParkingSpot());
	}
	
	@Test
	public void updateTicketTest() {
		
		savingTicketTest();
		ticket.setPrice(1);
		assertThat(ticketDAO.updateTicket(ticket)).isEqualTo(true);
	}
	
}
