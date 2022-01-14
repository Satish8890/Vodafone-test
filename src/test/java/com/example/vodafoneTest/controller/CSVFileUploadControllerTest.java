package com.example.vodafoneTest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.vodafoneTest.model.CSVDetailRequest;
import com.example.vodafoneTest.model.ResponseDetails;
import com.example.vodafoneTest.service.CSVFileUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CSVFileUploadController.class)
public class CSVFileUploadControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CSVFileUploadService fileService;

	@Test
	public void shoulReturnOKStatus_WhenSuccessfullyLoadingCSVFile() throws Exception {

		final String csvFile = creatAndGetCSVFile();

		ResponseDetails resp = new ResponseDetails();
		resp.setDescription("data refreshed");

		CSVDetailRequest CSVDetailRequest = new CSVDetailRequest();
		CSVDetailRequest.setFilepath(csvFile);

		when(fileService.uploadCSVFile(any(), any())).thenReturn(Optional.of(resp));

		this.mockMvc
				.perform(post("/v2/event").contentType(APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(CSVDetailRequest)))
				.andDo(print()).andExpect(jsonPath("description").value("data refreshed")).andExpect(status().isOk());
	}

	@Test
	public void shoulReturnbadStatus_WhenSuccessfullyLoadingCSVFile() throws Exception {

		final String csvFile = creatAndGetCSVFile();

		ResponseDetails resp = new ResponseDetails();
		resp.setDescription("ERROR: CSV file is empty or corrupt");

		CSVDetailRequest CSVDetailRequest = new CSVDetailRequest();
		CSVDetailRequest.setFilepath(csvFile);

		when(fileService.uploadCSVFile(any(), any())).thenReturn(Optional.of(resp));

		this.mockMvc
				.perform(post("/v2/event").contentType(APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(CSVDetailRequest)))
				.andDo(print()).andExpect(jsonPath("description").value("ERROR: CSV file is empty or corrupt")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void shoulReturnInternalServerError_WhenSuccessfullyLoadingCSVFile() throws Exception {

		final String csvFile = creatAndGetCSVFile();

		ResponseDetails resp = new ResponseDetails();
		resp.setDescription("ERROR: A technical exception occurred");

		CSVDetailRequest CSVDetailRequest = new CSVDetailRequest();
		CSVDetailRequest.setFilepath(csvFile);

		when(fileService.uploadCSVFile(any(), any())).thenReturn(Optional.of(resp));

		this.mockMvc
				.perform(post("/v2/event").contentType(APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(CSVDetailRequest)))
				.andDo(print()).andExpect(jsonPath("description").value("ERROR: A technical exception occurred")).andExpect(status().isInternalServerError());
	}

	public static String creatAndGetCSVFile() throws IOException {

		final String csvFile = System.getProperty("user.dir") + "/" + "data.csv";

		FileWriter fileWriter = new FileWriter(csvFile);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println("DateTime,EventId,ProductId,Latitude,Longitude,Battery,Light,AirplaneMode");
		printWriter.println("1582605077000,10001,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
		printWriter.println("1582605137000,10002,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
		printWriter.println("1582605197000,10003,WG11155638,51.5185,-0.1736,0.98,OFF,OFF");
		printWriter.println("1582605257000,10004,WG11155638,51.5185,-0.1736,0.98,OFF,OFF");
		printWriter.println("1582605257000,10005,6900001001,40.73061,-73.935242,0.11,N/A,OFF");
		printWriter.println("1582605258000,10006,6900001001,40.73071,-73.935242,0.1,N/A,OFF");
		printWriter.println("1582605259000,10007,6900001001,40.73081,-73.935242,0.1,N/A,OFF");
		printWriter.println("1582605317000,10008,WG11155800,45.5185,-12.52029,0.11,ON,OFF");
		printWriter.println("1582605377000,10009,WG11155800,45.5186,-12.52027,0.1,ON,OFF");
		printWriter.println("1582605437000,10010,WG11155800,45.5187,-12.52025,0.09,ON,OFF");
		printWriter.println("1582605497000,10011,WG11155638,51.5185,-0.17538,0.95,OFF,OFF");
		printWriter.println("1582605557000,10012,6900001001,40.73081,-73.935242,0.1,N/A,OFF");
		printWriter.println("1582605615000,10013,6900233111,,,0.1,N/A,ON");
		printWriter.println("1582612875000,10014,6900233111,,,0.1,N/A,OFF");

		printWriter.println("1582605317000,10008,WG11155801,45.5185,-12.52035,0.85,ON,OFF");
		printWriter.println("1582605377000,10009,WG11155801,45.5186,-12.52027,0.83,ON,OFF");
		printWriter.println("1582605437000,10010,WG11155801,45.5187,-12.52001,0.82,ON,OFF");

		printWriter.println("1582605317000,10008,WG11155803,45.5185,-12.52035,0.63,ON,OFF");
		printWriter.println("1582605377000,10009,WG11155803,,-12.52027,0.59,ON,OFF");
		printWriter.println("1582605437000,10010,WG11155803,45.5187,-12.52001,0.59,ON,OFF");

		printWriter.println("1582605317000,10008,WG11155804,45.5185,-12.52035,0.63,ON,OFF");
		printWriter.println("1582605377000,10009,WG11155804,45.5186,-12.52027,0.59,ON,OFF");
		printWriter.println("1582605437000,10010,WG11155804,,,0.59,ON,OFF");

		printWriter.println("1582605377000,10009,WG11155805,45.5186,-12.52027,0.35,ON,OFF");
		printWriter.println("1582605437000,10010,WG11155805,45.5187,-12.52001,0.30,ON,OFF");

		printWriter.println("1582605317000,10008,WG11155806,45.5185,,0.85,ON,OFF");
		printWriter.println("1582605377000,10009,WG11155806,45.5186,-12.52027,0.83,ON,OFF");
		printWriter.println("1582605437000,10010,WG11155806,45.5187,-12.52001,0.82,ON,OFF");

		printWriter.println("1582605437000,10010,WG11155841,,,0.82,ON,ON");

		printWriter.println("1582605437000,10010,WG11155842,45.5187,-12.52001,0.82,ON,ON");

		printWriter.print("1582605253000,10015,6900001001,41.73061,-74.935242,0.1,N/A,OFF");
		printWriter.close();

		return csvFile;
	}

	public static String creatAndGetCorruptCSVFile() throws IOException {

		final String csvFile = System.getProperty("user.dir") + "/" + "data.csv";

		FileWriter fileWriter = new FileWriter(csvFile);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println("DateTime,EventId,ProductId,Latitude,Longitude,Battery,Light,AirplaneMode");
		printWriter.println("1582605077000,10001,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");

		printWriter.println("1582605317000,10008,WG11155806,45.5185,,0.85,ON,OFF");
		printWriter.println("1582605377000,,45.5186,-12.52027,0.83,ON,OFF");
		printWriter.println("1582605437000,10010,WG11155806-ON,OFF");

		printWriter.println("1582605437000=10010/WG11155841,0.82,ON,ON");

		printWriter.println("1582605437000\\10010,WG11155842;45.5187:-12.52001,0.82,ON,ON");

		printWriter.print("1582605253000,1,N/A,OFF");
		printWriter.close();

		return csvFile;
	}

	public static String creatAndGetBigCSVFile() throws IOException {

		final String csvFile = System.getProperty("user.dir") + "/" + "data.csv";

		FileWriter fileWriter = new FileWriter(csvFile);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println("DateTime,EventId,ProductId,Latitude,Longitude,Battery,Light,AirplaneMode");
		Long tstmp = 1582605077000L;

		for (int i = 0; i < 10000; i++) {
			printWriter.println(tstmp.toString() + ",10001,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
			tstmp++;
		}

		printWriter.close();

		return csvFile;
	}



	@Test
	public void shoulReturnNotFoundStatus_WhenCSVFile_Not_Found() throws Exception {

		final String csvFile = creatAndGetCSVFile();

		ResponseDetails resp = new ResponseDetails();
		resp.setDescription("ERROR: no data file found");

		CSVDetailRequest CSVDetailRequest = new CSVDetailRequest();
		CSVDetailRequest.setFilepath(csvFile);

		when(fileService.uploadCSVFile(any(), any())).thenReturn(Optional.of(resp));

		this.mockMvc
				.perform(post("/v2/event").contentType(APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(CSVDetailRequest)))
				.andDo(print()).andExpect(jsonPath("description").value("ERROR: no data file found"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void shoulReturnNotFoundStatusWhenCSVFile_Not_Found() throws Exception {

		final String csvFile = creatAndGetBigCSVFile();

		ResponseDetails resp = new ResponseDetails();
		resp.setDescription("ERROR: no data file found");

		CSVDetailRequest CSVDetailRequest = new CSVDetailRequest();
		CSVDetailRequest.setFilepath(csvFile);

		when(fileService.uploadCSVFile(any(), any())).thenReturn(Optional.of(resp));

		this.mockMvc
				.perform(post("/v2/event").contentType(APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(CSVDetailRequest)))
				.andDo(print()).andExpect(jsonPath("description").value("ERROR: no data file found"))
				.andExpect(status().isNotFound());
	}

}
