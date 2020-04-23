package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	
	private  ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        float diff = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
        
        float duration = diff/(1000*60*60);
        
        
        if(duration > 0.5) {
        	boolean exist = false;
        	double price;
        	//System.out.println("NOUS"+parkingSpotDAO.getRowsCountWithSameVehiculeNumber(ticket.getVehicleRegNumber()));
        	if(parkingSpotDAO.getRowsCountWithSameVehiculeNumber(ticket.getVehicleRegNumber()) >= 1) {   
        		exist = true;
        	}
        		switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                	price=duration * Fare.CAR_RATE_PER_HOUR;
                	if(exist) price = price*0.95;
                    ticket.setPrice(price);
                    break;
                }
                case BIKE: {
                	price=duration * Fare.BIKE_RATE_PER_HOUR;
                	if(exist) price = price*0.95;
                    ticket.setPrice(price);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }	 
        }else {
        	ticket.setPrice(0.0);
        } 
       
    }
    
    public void setParkingDAO(ParkingSpotDAO parkingSpotDAO) {
    	this.parkingSpotDAO = parkingSpotDAO;
    }
    
    
    
    
    
}