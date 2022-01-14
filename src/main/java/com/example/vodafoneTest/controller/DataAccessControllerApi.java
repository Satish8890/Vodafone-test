package com.example.vodafoneTest.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.vodafoneTest.constant.CSVFileUploadConstant;
import com.example.vodafoneTest.model.DeviceInfoResponse;
import com.example.vodafoneTest.model.ResponseDetails;
import com.example.vodafoneTest.service.DeviceInfoService;

@Controller
@RequestMapping("/v2")
public class DataAccessControllerApi implements DataAccessController {

	@Autowired
	DeviceInfoService deviceInfoService;

	@Override
	public ResponseEntity<?> getDeviceInfo(String ProductId, Long tstmp) {

		Optional<?> resp = deviceInfoService.getDeviceInfo(ProductId, tstmp);

		if (resp.get() instanceof DeviceInfoResponse) {
			return ResponseEntity.ok(resp.get());
		}

		ResponseDetails iotResponse = (ResponseDetails) resp.get();

		HttpStatus httpstatus = HttpStatus.NOT_FOUND;

		if (iotResponse.getDescription().equals(CSVFileUploadConstant.ERROR_DEVICE_NOT_LOCATED)) {
			httpstatus = HttpStatus.BAD_REQUEST;
		} else if (iotResponse.getDescription().equals(CSVFileUploadConstant.ERROR_DB_EMPTY)) {
			httpstatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return ResponseEntity.status(httpstatus).body(iotResponse);

	}

}
