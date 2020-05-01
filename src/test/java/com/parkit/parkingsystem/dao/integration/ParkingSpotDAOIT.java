package com.parkit.parkingsystem.dao.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class ParkingSpotDAOIT {

	ParkingSpotDAO parkingDAO;
	TicketDAO ticketDAO;
	Ticket ticket;
	
	@BeforeEach
	public void setUpPerTest() {
		parkingDAO = new ParkingSpotDAO();
		ticketDAO = new TicketDAO();
		ticket = new Ticket();
	}
	
	@Test
	public void getNextAvailableSlotWhenAvailableTest() {
		int ourNum = parkingDAO.getNextAvailableSlot(ParkingType.CAR);
		assertThat(ourNum).isNotEqualTo(-1);
	}
	
	@Test
	public void updateParkingTestWithGoodValueOfParkingSpotShouldReturnTrue() {
		
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,true);
		
		ticket.setParkingSpot(parkingSpot);
		ticketDAO.saveTicket(ticket);
		parkingSpot = new ParkingSpot(4, ParkingType.CAR,true);
		
		
		assertThat(parkingDAO.updateParking(parkingSpot)).isEqualTo(true);
	}
	
	@Test
	public void updateParkingTestWithNullValueOfParkingSpotShouldReturnFalse() {
		
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,true);
		
		ticket.setParkingSpot(parkingSpot);
		ticketDAO.saveTicket(ticket);
		
		
		
		assertThat(parkingDAO.updateParking(null)).isEqualTo(false);
	}
	
	
}
