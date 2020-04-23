package com.parkit.parkingsystem.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputReaderUtilTest {
	
	private InputReaderUtil inputReaderUtil;
	
	@BeforeEach 
	private void setUpPerTest() {
		inputReaderUtil = new InputReaderUtil();
	}
	
	@Test
	public void readSelectionTestWithGoodValueEntered() throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream("1\n".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		
		assertThat(inputReaderUtil.readSelection()).isEqualTo(1);
	}

	
	
	@Test
	public void readSelectionTestWithBadValueEntered() throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream("Bonjour\n".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		
		assertThat(inputReaderUtil.readSelection()).isEqualTo(-1);
	}
	
	@Test
	public void readVehiculeRegistrationNumberWithGoodVehiculeRegNumber() throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream("ABCDEF\n".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		
		assertThat(inputReaderUtil.readVehiculeRegistrationNumber()).isEqualTo("ABCDEF");
	}
	
	@Test
	public void readVehiculeRegistrationNumberWithBadVehiculeRegNumber() throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream("\n".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		
		 assertThrows(IllegalArgumentException.class , () -> inputReaderUtil.readVehiculeRegistrationNumber(),"Invalid input provided" );

		    
	}
	
}

	
	
	
	
	
	
	
	