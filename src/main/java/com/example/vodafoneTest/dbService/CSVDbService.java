package com.example.vodafoneTest.dbService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vodafoneTest.exception.IOTException;
import com.example.vodafoneTest.model.CSVFileDetails;

@Service
public class CSVDbService {

	@Autowired
	IOTDataBase iotDB;

	public int saveOrUpdate(List<CSVFileDetails> csvFileDetailsList) {

		return iotDB.add(csvFileDetailsList);
	}

	public List<CSVFileDetails> getDeviceList(String productId) throws IOTException {

		return iotDB.get(productId);
	}

}
