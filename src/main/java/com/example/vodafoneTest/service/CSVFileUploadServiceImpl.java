package com.example.vodafoneTest.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vodafoneTest.constant.CSVFileUploadConstant;
import com.example.vodafoneTest.dbService.CSVDbService;
import com.example.vodafoneTest.model.CSVFileDetails;
import com.example.vodafoneTest.model.ResponseDetails;
import com.example.vodafoneTest.util.CommonUtil;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

@Service
public class CSVFileUploadServiceImpl implements CSVFileUploadService {

	@Autowired
	CSVDbService iotRepository;

	@Override
	public Optional<ResponseDetails> uploadCSVFile(String filePath, Character delimiter) {

		List<CSVFileDetails> deviceDetailList = null;

		if (CommonUtil.isValidDelimiter(delimiter) == false) {
			delimiter = ',';
		}

		ResponseDetails resp = new ResponseDetails();

		try (Reader reader = new FileReader(filePath)) {

			deviceDetailList = readAll(reader, delimiter);

			if (deviceDetailList == null || deviceDetailList.isEmpty()) {
				resp.setDescription(CSVFileUploadConstant.ERROR_EMPTY_FILE);
				return Optional.of(resp);
			}

		} catch (FileNotFoundException e) {
			resp.setDescription(CSVFileUploadConstant.ERROR_FILE_NOT_FOUND);
			System.out.println("File - " + filePath + " not found - " + e.getMessage());
			return Optional.of(resp);
		} catch (Exception e) {
			deviceDetailList = null;
			resp.setDescription(CSVFileUploadConstant.ERROR_TECHNICAL_EXCEP + " - " + e.getMessage());
			System.out.println("Exception occured while processing file - " + filePath + " reason - " + e.getMessage());
			return Optional.of(resp);
		}

		if (iotRepository.saveOrUpdate(deviceDetailList) == 1) {
			resp.setDescription(CSVFileUploadConstant.DATA_REFRESHED);
		} else {
			deviceDetailList = null;
			resp.setDescription(
					"ERROR: A technical exception occurred - Failed to load DB" + " - Check csv File and Delimiter");
			System.out.println("Exception occured while Loading records in DB - ");
			return Optional.of(resp);
		}

		return Optional.of(resp);

	}

	private List<CSVFileDetails> readAll(Reader reader, char separator) {

		HeaderColumnNameMappingStrategy<CSVFileDetails> cpms = new HeaderColumnNameMappingStrategy<CSVFileDetails>();
		cpms.setType(CSVFileDetails.class);
		CsvToBean<CSVFileDetails> csvToBean = new CsvToBeanBuilder<CSVFileDetails>(reader).withSeparator(separator)
				.withType(CSVFileDetails.class).withMappingStrategy(cpms).build();
		return csvToBean.parse();
	}

}
