package com.example.vodafoneTest.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.vodafoneTest.model.CSVFileDetails;

public class CommonUtilTest {

	@Test
	void shouldReturnCorrectDateFormat() {
		Long tstmp = 1582605077000L;
		assertEquals("25/02/2020 10:01:17", CommonUtil.convertToDate(tstmp));
	}

	@Test
	void shouldReturn_Active_Status_When_AirplaneMode_OFF() {

		CSVFileDetails deviceInfo = new CSVFileDetails();
		deviceInfo.setAirplaneMode("OFF");
		assertEquals("Active", CommonUtil.getStatus(deviceInfo));
	}

	@Test
	void shouldReturn_Inactive_Status_When_AirplaneMode_ON() {

		CSVFileDetails deviceInfo = new CSVFileDetails();
		deviceInfo.setAirplaneMode("ON");
		assertEquals("Inactive", CommonUtil.getStatus(deviceInfo));
	}

	@Test
	void shouldReturn_All_Battery_Status_Correct() {
		CSVFileDetails deviceInfo = new CSVFileDetails();
		deviceInfo.setBattery(.98);
		assertEquals("Full", CommonUtil.getBatteryStatus(deviceInfo));

		deviceInfo.setBattery(.60);
		assertEquals("High", CommonUtil.getBatteryStatus(deviceInfo));

		deviceInfo.setBattery(.40);
		assertEquals("Medium", CommonUtil.getBatteryStatus(deviceInfo));

		deviceInfo.setBattery(.10);
		assertEquals("Low", CommonUtil.getBatteryStatus(deviceInfo));

		deviceInfo.setBattery(.05);
		assertEquals("Critical", CommonUtil.getBatteryStatus(deviceInfo));
	}

	@Test
	void shouldReturn_Location_Identified_When_AirplaneMode_OFF() {

		CSVFileDetails deviceInfo = new CSVFileDetails();
		deviceInfo.setAirplaneMode("OFF");
		assertEquals("SUCCESS: Location identified.", CommonUtil.getDescription(deviceInfo));
	}

	@Test
	void shouldReturn_Location_Notavailable_When_AirplaneMode_ON() {

		CSVFileDetails deviceInfo = new CSVFileDetails();
		deviceInfo.setAirplaneMode("ON");
		assertEquals("SUCCESS: Location not available: Please turn off airplane mode",
				CommonUtil.getDescription(deviceInfo));
	}

	@Test
	void shouldReturn_Longitude_Latitude_When_AirplaneMode_OFF() {

		CSVFileDetails deviceInfo = new CSVFileDetails();
		deviceInfo.setAirplaneMode("OFF");
		deviceInfo.setLatitude(51.5185);
		deviceInfo.setLongitude(-0.1736);
		assertEquals("51.5185", CommonUtil.getLatitude(deviceInfo));
		assertEquals("-0.1736", CommonUtil.getLongitude(deviceInfo));
	}

	@Test
	void shouldNotReturn_Longitude_Latitude_When_AirplaneMode_ON() {

		CSVFileDetails deviceInfo = new CSVFileDetails();
		deviceInfo.setAirplaneMode("ON");
		deviceInfo.setLatitude(51.5185);
		deviceInfo.setLongitude(-0.1736);
		assertEquals("", CommonUtil.getLatitude(deviceInfo));
		assertEquals("", CommonUtil.getLongitude(deviceInfo));
	}

	@Test
	void shouldReturnCorrectValueForGPSData() {

		double lat1 = 45.5187;
		double lng1 = -12.52001;
		double lat2 = 45.5187;
		double lng2 = -12.52001;

		assertEquals(CommonUtil.getDistanceBetween(lat1, lng1, lat2, lng2), 0);

		lat2 = 45.5185;
		lng2 = -12.52040;

		assertNotEquals(CommonUtil.getDistanceBetween(lat1, lng1, lat2, lng2), 0);
	}

	@Test
	void shouldReturnInActive_WhenAllGPSDataSame() {
		List<CSVFileDetails> dlist = new LinkedList<CSVFileDetails>();
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		assertEquals(CommonUtil.getStatus(dlist), "Inactive");
	}

	@Test
	void shouldReturnNA_WhenAllGPSDistanceNotSignificant() {
		List<CSVFileDetails> dlist = new LinkedList<CSVFileDetails>();
		dlist.add(getDeviceDetail(45.5185, -12.52010));
		dlist.add(getDeviceDetail(45.5186, -12.52005));
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		assertEquals(CommonUtil.getStatus(dlist), "N/A");
	}

	@Test
	void shouldReturnActive_WhenAllGPS_Are_in_Significant_Distance_Apart() {
		List<CSVFileDetails> dlist = new LinkedList<CSVFileDetails>();
		dlist.add(getDeviceDetail(45.5180, -12.52020));
		dlist.add(getDeviceDetail(45.5184, -12.52011));
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		assertEquals(CommonUtil.getStatus(dlist), "Active");
	}

	@Test
	void shouldReturnInActive_WhenOneGPSDataSame_Not_Enough_Missing() {
		List<CSVFileDetails> dlist = new LinkedList<CSVFileDetails>();
		dlist.add(getDeviceDetail(45.5180, -12.52041));
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		// dlist.add(getDeviceDetail(45.5187,-12.52001));
		assertEquals(CommonUtil.getStatus(dlist), "N/A");
	}

	@Test
	void shouldReturnInActive_WhenOneGPSData_Missing() {
		List<CSVFileDetails> dlist = new LinkedList<CSVFileDetails>();
		dlist.add(getDeviceDetail(45.5180, -12.52041));
		dlist.add(getDeviceDetail(null, -12.520021));
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		assertEquals(CommonUtil.getStatus(dlist), "Inactive");
	}

	@Test
	void shouldReturnInActive_WhenOneGPSData_Missing2() {
		List<CSVFileDetails> dlist = new LinkedList<CSVFileDetails>();
		dlist.add(getDeviceDetail(45.5180, null));
		dlist.add(getDeviceDetail(45.5185, -12.52041));
		dlist.add(getDeviceDetail(45.5187, -12.52001));
		assertEquals(CommonUtil.getStatus(dlist), "Inactive");
	}

	@Test
	void shouldReturnInActive_WhenOneGPSData_Missing3() {
		List<CSVFileDetails> dlist = new LinkedList<CSVFileDetails>();
		dlist.add(getDeviceDetail(45.5183, -12.52001));
		dlist.add(getDeviceDetail(45.5185, -12.52001));
		dlist.add(getDeviceDetail(null, -12.52001));
		assertEquals(CommonUtil.getStatus(dlist), "Inactive");
	}

	@Test
	void shouldValidate_All_Delimiter_Correctly() {

		assertTrue(CommonUtil.isValidDelimiter(','));
		assertTrue(CommonUtil.isValidDelimiter(';'));
		assertTrue(CommonUtil.isValidDelimiter('|'));
		assertTrue(CommonUtil.isValidDelimiter(':'));
		assertTrue(CommonUtil.isValidDelimiter('	'));

		assertFalse(CommonUtil.isValidDelimiter(' '));
		assertFalse(CommonUtil.isValidDelimiter('A'));
		assertFalse(CommonUtil.isValidDelimiter('9'));
		assertFalse(CommonUtil.isValidDelimiter(null));

	}

	private static CSVFileDetails getDeviceDetail(Double lat, Double lon) {
		CSVFileDetails dd = new CSVFileDetails();
		dd.setLatitude(lat);
		dd.setLongitude(lon);
		return dd;
	}

}
