package com.example.vodafoneTest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface DataAccessController {
	@GetMapping(path = "/event", produces = "application/json")
	public ResponseEntity<?> getDeviceInfo(@RequestParam(value = "ProductId", required = true) String ProductId,
			@RequestParam(value = "tstmp", required = false) Long tstmp);
}
