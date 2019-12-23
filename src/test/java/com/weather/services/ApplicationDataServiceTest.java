package com.weather.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Test;

public class ApplicationDataServiceTest {

	@Test
	public void testReadCSV() {
		// check if the file is located in the correct path, or if it exists at all
		assertTrue("The CSV file could not be found in the same path as the ApplicationDataService.java file", 
				Files.exists(Paths.get("eng-climate-summary.csv"), new LinkOption[]{ LinkOption.NOFOLLOW_LINKS}));
		
		// check the header structure
    	Reader csvReader = null;
    	CSVParser csvParser = null;
    	try {
			csvReader = Files.newBufferedReader(Paths.get("eng-climate-summary.csv"), StandardCharsets.UTF_8);
	    	csvParser = new CSVParser(csvReader,CSVFormat.DEFAULT.withFirstRecordAsHeader());
	    	Map<String, Integer> headers = csvParser.getHeaderMap();
	    	assertTrue("Header 'Station_Name' not found in CSV", headers.containsKey("Station_Name"));
	    	assertTrue("Header 'Province' not found in CSV", headers.containsKey("Province"));
	    	assertTrue("Header 'Date' not found in CSV", headers.containsKey("Date"));
	    	assertTrue("Header 'Mean_Temp' not found in CSV", headers.containsKey("Mean_Temp"));
	    	assertTrue("Header 'Highest_Monthly_Maxi_Temp' not found in CSV", headers.containsKey("Highest_Monthly_Maxi_Temp"));
	    	assertTrue("Header 'Lowest_Monthly_Min_Temp' not found in CSV", headers.containsKey("Lowest_Monthly_Min_Temp"));
	    	
	    	assertTrue("No rows found in the CSV file", csvParser.getRecords().size() > 0);
	    	
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
