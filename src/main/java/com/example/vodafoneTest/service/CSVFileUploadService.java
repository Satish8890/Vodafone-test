package com.example.vodafoneTest.service;

import java.util.Optional;

import com.example.vodafoneTest.model.ResponseDetails;

public interface CSVFileUploadService {

	Optional<ResponseDetails> uploadCSVFile(String filepath, Character delimiter);

}
