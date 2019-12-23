package com.weather.services;

import org.apache.commons.csv.CSVFormat;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import com.weather.model.WeatherRecord;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author		iustin.dumitru
 * @version		1.0
 * 
 * Service for reading historical weather information stored in a CSV file
 */

@Service
public class ApplicationDataService {
    private final Logger logger = LoggerFactory.getLogger(ApplicationDataService.class);

    private List<WeatherRecord> weatherRecords;

    /**
     * @author		iustin.dumitru
     * @param		Date dateStart, Date dateEnd
     * @return		List<WeatherRecord>
     * 
     * Method which performs a simple server side validation and returns the matching rows as list of WeatherRecord
     */
	public List<WeatherRecord> getData(Date dateStart, Date dateEnd) {
		if(dateStart == null) {
			logger.info("DateStart is null, return the full dataset");
			return weatherRecords;
		}
		
		// Server side validation
		logger.info("DateStart: " + dateStart + " DateEnd: " + dateEnd);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<WeatherRecord> data = new ArrayList<WeatherRecord>();
		weatherRecords.forEach(weatherRecord -> {
			Date entryDate;
			try {
				entryDate = dateFormat.parse(weatherRecord.getDate());
				if ((dateStart.before(entryDate) || dateStart.equals(entryDate)) && (dateEnd.after(entryDate) || dateEnd.equals(entryDate))) {
					data.add(weatherRecord);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return data;
	}

	
    /**
     * @author		iustin.dumitru
     * @param		
     * @return		void
     * 
     * Method which performs a read and parse of the CSV file before the class is put in service
     */
    @PostConstruct
    public void readCSV() {
    	weatherRecords = new ArrayList<WeatherRecord>();
    	
    	//read CSV
    	Reader csvReader = null;
    	CSVParser csvParser = null;
    	
		try {
			//as the file is small enough, we can use a "parse into memory" approach to speed up the parsing process
			csvReader = Files.newBufferedReader(Paths.get("eng-climate-summary.csv"), StandardCharsets.UTF_8);
	    	csvParser = new CSVParser(csvReader,CSVFormat.DEFAULT.withFirstRecordAsHeader());
			
	    	/* Note: 
	    	   Instead of the classic "loop through", we can also use a functional programming approach: 
	    	   weatherRecords = csvParser.getRecords().stream()
		        	.map(r -> castToWeatherRecord(r)) // this casting function can be defined using the same logic as below (see the for loop below)
		        	.filter(null)
		        	.collect(Collectors.toList());
               I will stick tough with the more OOP approach for now, 
               but I'll be glad to discuss the functional programming approach during an eventual interview at a later stage
	    	 */
	    	
	    	for (CSVRecord csvRecord : csvParser) {
				WeatherRecord weatherRecord = new WeatherRecord(
					csvRecord.get(0),
					csvRecord.get(1),
					csvRecord.get(2),
					csvRecord.get(3),
					csvRecord.get(4),
					csvRecord.get(5));
				weatherRecords.add(weatherRecord);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				csvReader.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			try {
				csvParser.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

    }

}