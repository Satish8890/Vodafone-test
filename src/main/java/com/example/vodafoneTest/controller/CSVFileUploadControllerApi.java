package com.example.vodafoneTest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.vodafoneTest.model.CSVDetailRequest;
import com.example.vodafoneTest.model.ResponseDetails;

public interface CSVFileUploadControllerApi {

	@PostMapping(path = "/event", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseDetails> loadCSVFile(@RequestBody CSVDetailRequest csvDetailRequest);
}
