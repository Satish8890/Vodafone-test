package com.example.vodafoneTest.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vodafoneTest.constant.CSVFileUploadConstant;
import com.example.vodafoneTest.dbService.CSVDbService;
import com.example.vodafoneTest.exception.IOTException;
import com.example.vodafoneTest.model.CSVFileDetails;
import com.example.vodafoneTest.model.DeviceInfoResponse;
import com.example.vodafoneTest.model.ProductList;
import com.example.vodafoneTest.model.ResponseDetails;
import com.example.vodafoneTest.util.CommonUtil;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {
	
	@Autowired
	CSVDbService iotRepository;

	@Override
	public Optional<?> getDeviceInfo(String productId, Long tstmp) {

		if (tstmp == null) {
			Instant instant = Instant.now();
			tstmp = instant.toEpochMilli();
		}

		List<CSVFileDetails> deviceDetailList = null;

		try {
			deviceDetailList = iotRepository.getDeviceList(productId);
		} catch (IOTException e) {
			ResponseDetails resp = new ResponseDetails();
			resp.setDescription(CSVFileUploadConstant.ERROR_DB_EMPTY);
			return Optional.of(resp);
		}

		if (deviceDetailList == null) {
			ResponseDetails resp = new ResponseDetails();
			resp.setDescription("ERROR: Id <" + productId + "> not found");
			return Optional.of(resp);
		}

		if (ProductList.getProductType(productId).equals("CyclePlusTracker")) {

			List<CSVFileDetails> devList = getDeviceDetailList(deviceDetailList, tstmp);

			return generateIOTResponse(devList);

		}

		Optional<CSVFileDetails> deviceDetails = getDeviceDetails(deviceDetailList, tstmp);

		return generateIOTResponse(deviceDetails);

	}

	public Optional<CSVFileDetails> getDeviceDetails(List<CSVFileDetails> deviceDetailList, Long t_stmp) {

		return Optional.ofNullable(deviceDetailList.parallelStream().filter(dd -> dd.getDateTime() <= t_stmp)
				.min(Comparator.comparingLong(dd -> (Math.abs(dd.getDateTime() - t_stmp)))).orElse(null));
	}

	public List<CSVFileDetails> getDeviceDetailList(List<CSVFileDetails> deviceDetailList, Long tstmp) {
		List<CSVFileDetails> dList = new LinkedList<CSVFileDetails>();
		Optional<CSVFileDetails> deviceDetails = null;

		for (int i = 0; i < 3; i++) {
			deviceDetails = getDeviceDetails(deviceDetailList, tstmp);
			if (deviceDetails.isPresent()) {
				dList.add(deviceDetails.get());
				tstmp = deviceDetails.get().getDateTime() - (i + 1);// avoid duplicate
			} else {
				break;
			}
		}

		if (dList.isEmpty()) {
			dList = null;
		}

		return dList;
	}

	private Optional<?> generateIOTResponse(Optional<CSVFileDetails> deviceDetails) {

		if (!deviceDetails.isPresent()) {
			ResponseDetails resp = new ResponseDetails();
			resp.setDescription(CSVFileUploadConstant.ERROR_NO_DEVICE_IN_PAST);
			return Optional.of(resp);
		}

		CSVFileDetails deviceInfo = deviceDetails.get();

		if (deviceInfo.getAirplaneMode().equals("OFF")
				&& (deviceInfo.getLatitude() == null || deviceInfo.getLongitude() == null)) {
			ResponseDetails resp = new ResponseDetails();
			resp.setDescription(CSVFileUploadConstant.ERROR_DEVICE_NOT_LOCATED);
			return Optional.of(resp);

		}

		DeviceInfoResponse deviceInfoResponse = uploadAndGetDeviceInfo(deviceInfo);

		return Optional.of(deviceInfoResponse);

	}

	private Optional<?> generateIOTResponse(List<CSVFileDetails> devList) {

		Optional<CSVFileDetails> deviceDetails = Optional.empty();

		if (devList == null) {
			ResponseDetails resp = new ResponseDetails();
			resp.setDescription(CSVFileUploadConstant.ERROR_NO_DEVICE_IN_PAST);
			return Optional.of(resp);
		}

		deviceDetails = Optional.of(devList.get(0));

		CSVFileDetails deviceInfo = deviceDetails.get();

		if (deviceInfo.getAirplaneMode().equals("OFF")
				&& (deviceInfo.getLatitude() == null || deviceInfo.getLongitude() == null)) {
			ResponseDetails resp = new ResponseDetails();
			resp.setDescription(CSVFileUploadConstant.ERROR_DEVICE_NOT_LOCATED);
			return Optional.of(resp);
		}

		DeviceInfoResponse deviceInfoResponse = uploadAndGetDeviceInfo(deviceInfo);

		if (deviceInfo.getAirplaneMode().equals("OFF")) {
			String status = CommonUtil.getStatus(devList);
			deviceInfoResponse.setStatus(status);
		}

		return Optional.of(deviceInfoResponse);

	}

	private DeviceInfoResponse uploadAndGetDeviceInfo(CSVFileDetails deviceInfo) {

		DeviceInfoResponse deviceInfoResponse = new DeviceInfoResponse();

		deviceInfoResponse.setId(deviceInfo.getProductId());
		deviceInfoResponse.setName(ProductList.getProductType(deviceInfo.getProductId()));
		deviceInfoResponse.setDatetime(CommonUtil.convertToDate(deviceInfo.getDateTime()));
		deviceInfoResponse.setLongitude(CommonUtil.getLongitude(deviceInfo));
		deviceInfoResponse.setLatitude(CommonUtil.getLatitude(deviceInfo));
		deviceInfoResponse.setStatus(CommonUtil.getStatus(deviceInfo));
		deviceInfoResponse.setBattery(CommonUtil.getBatteryStatus(deviceInfo));
		deviceInfoResponse.setDescription(CommonUtil.getDescription(deviceInfo));

		return deviceInfoResponse;
	}

}
