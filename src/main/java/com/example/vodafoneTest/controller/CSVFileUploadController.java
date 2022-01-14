package com.example.vodafoneTest.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.vodafoneTest.constant.CSVFileUploadConstant;
import com.example.vodafoneTest.model.CSVDetailRequest;
import com.example.vodafoneTest.model.ResponseDetails;
import com.example.vodafoneTest.service.CSVFileUploadService;

@Controller
@RequestMapping("/v2")
public class CSVFileUploadController implements CSVFileUploadControllerApi {

	@Autowired
	CSVFileUploadService csvFileUploadService;

	@Override
	public ResponseEntity<ResponseDetails> loadCSVFile(CSVDetailRequest csvDetailRequest) {
		Optional<ResponseDetails> resp = csvFileUploadService.uploadCSVFile(csvDetailRequest.getFilepath(),
				csvDetailRequest.getDelimiter());
		ResponseDetails iotResponse = resp.get();

		if (iotResponse.getDescription().equals(CSVFileUploadConstant.DATA_REFRESHED)) {
			return ResponseEntity.ok(iotResponse);
		} else if (iotResponse.getDescription().equals(CSVFileUploadConstant.ERROR_EMPTY_FILE)) {
			return ResponseEntity.badRequest().body(iotResponse);
		} else if (iotResponse.getDescription().contains(CSVFileUploadConstant.ERROR_TECHNICAL_EXCEP)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(iotResponse);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(iotResponse);
	}

}
