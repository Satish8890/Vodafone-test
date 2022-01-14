package com.example.vodafoneTest.service;

import static com.example.vodafoneTest.util.IOTTESTUtil.creatAndGetCSVFile;
import static com.example.vodafoneTest.util.IOTTESTUtil.creatAndGetCorruptCSVFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.vodafoneTest.model.ResponseDetails;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CSVFileUploadServiceTest {

	
	@Autowired
	CSVFileUploadService fileService;
	
	@Test
	void shouldReturn_Data_Refreshed_Msg_OnSuccessfullLoadingOfCSVFile() throws IOException{
		
		final String csvFile = creatAndGetCSVFile();			    
	    
	    Optional<ResponseDetails> resp = fileService.uploadCSVFile(csvFile, ',');
	    
	    ResponseDetails iotResp = resp.get();
	   
	    assertEquals(iotResp.getDescription(), "data refreshed");
        
	}
	
	@Test
	void shouldReturnNotFoundHttpResponse_WhenCSVFileNotFound() throws IOException{
		
		//below file does not exist
		final String csvFile = System.getProperty("user.dir") + "/" + "NotACSV.csv";		
	    
		Optional<ResponseDetails> resp = fileService.uploadCSVFile(csvFile, ',');
	    ResponseDetails iotResp =  (ResponseDetails) resp.get();

	    assertEquals(iotResp.getDescription(), "ERROR: no data file found");
        
	}
	
	@Test
	void shouldReturnBadRequestHttpResponse_WhenCSVFileDoesNotHaveAnyRecordsOrcorrupt() throws IOException{
		
		String csvFile = System.getProperty("user.dir") + "/" + "Empty.csv";
		
		FileWriter fileWriter = new FileWriter(csvFile);
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.println("DateTime,EventId,ProductId,Latitude,Longitude,Battery,Light,AirplaneMode");
	    printWriter.close();
	    
	    Optional<ResponseDetails> resp = fileService.uploadCSVFile(csvFile, ',');
	    ResponseDetails iotResp =  (ResponseDetails) resp.get();
	  
	    assertEquals(iotResp.getDescription(), "ERROR: CSV file is empty or corrupt");
        
	}	
	
	@Test
	void shouldReturn_Technical_Exception_When_Loading_Corrupt_CSVFile() throws IOException{
		
		final String csvFile = creatAndGetCorruptCSVFile();			    
	    
	    Optional<ResponseDetails> resp = fileService.uploadCSVFile(csvFile, ',');
	    
	    ResponseDetails iotResp = resp.get();
	   
	    assertTrue(iotResp.getDescription().contains("ERROR: A technical exception occurred"));
        
	}	


}
