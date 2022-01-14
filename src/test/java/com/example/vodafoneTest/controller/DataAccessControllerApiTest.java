package com.example.vodafoneTest.controller;

import static com.example.vodafoneTest.util.IOTTESTUtil.creatAndGetCSVFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.vodafoneTest.model.DeviceInfoResponse;
import com.example.vodafoneTest.model.ResponseDetails;
import com.example.vodafoneTest.service.CSVFileUploadService;
import com.example.vodafoneTest.service.DeviceInfoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DataAccessControllerApi.class)
public class DataAccessControllerApiTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DeviceInfoService deviceInfoService;

	@Test
	public void shouldReturnDeviceInformationWithOKStatus() throws Exception {

		DeviceInfoResponse dr = new DeviceInfoResponse();
		dr.setId("WG11155638");
		dr.setName("CyclePlusTracker");
		dr.setBattery("Full");
		dr.setDatetime("25/02/2020 04:31:17");
		dr.setDescription("SUCCESS: Location identified.");
		dr.setLatitude("51.5185");
		dr.setLongitude("-0.1736");
		dr.setStatus("Active");

		Mockito.doReturn(Optional.of(dr)).when(deviceInfoService).getDeviceInfo(any(), any());

		this.mockMvc.perform(get("/v2/event").param("ProductId", "WG11155638")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("id").value("WG11155638"))
				.andExpect(jsonPath("name").value("CyclePlusTracker"))
				.andExpect(jsonPath("datetime").value("25/02/2020 04:31:17"))
				.andExpect(jsonPath("longitude").value("-0.1736")).andExpect(jsonPath("latitude").value("51.5185"))
				.andExpect(jsonPath("status").value("Active")).andExpect(jsonPath("battery").value("Full"))
				.andExpect(jsonPath("description").value("SUCCESS: Location identified."));
	}
}
