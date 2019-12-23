package com.weather.controller;

import com.weather.model.WeatherRecord;
import com.weather.services.ApplicationDataService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author		iustin.dumitru
 * @version		1.0
 * 
 * REST Controller class for mapping data to a web service
 */

@RestController
public class ApplicationController {

    private final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    ApplicationDataService dataService;

    @Autowired
    public void setWeatherDataService(ApplicationDataService dataSrv) {
        this.dataService = dataSrv;
    }

    private List<WeatherRecord> fetchData(Date dateStart, Date dateEnd) {
    	List<WeatherRecord> weatherRecords = dataService.getData(dateStart, dateEnd);
    	logger.info(weatherRecords.size() + " records returned by the data service" );
    	return weatherRecords;
    }

    /**
     * @author		iustin.dumitru
     * @param		void
     * @return		ResponseEntity
     * 
     * Public method which outputs the unfiltered data as JSON format
     */
    @GetMapping("/list")
    public ResponseEntity<?> getWeatherData() {
    	List<WeatherRecord> weatherRecords = fetchData(null,null);
    	if(weatherRecords.isEmpty())
    		return ResponseEntity.badRequest().body("No data found");
    	else 
    		return ResponseEntity.ok(weatherRecords);
    }

    
    /**
     * @author		iustin.dumitru
     * @param		String dateStart, String dateEnd
     * @return		ResponseEntity
     * 
     * Public method which outputs the filtered data as JSON format
     */
    @GetMapping("/list/{dateStart}/{dateEnd}")
    public ResponseEntity<?> getWeatherDataFiltered(@PathVariable String dateStart, @PathVariable String dateEnd) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date fmDateStart = null;
    	Date fmDateEnd = null;
    	// Server side validation
    	// Note: a better validation can be implemented using complex regular expression algorithm. For instance, we can check whether day of month is within correct ranges (1-31, 1-30, 1-28, 1-29), etc
    	// 		 However, due to time constraints we can have this covered in the eventuality of a face to face interview
    	try {
    		fmDateStart = dateFormat.parse(dateStart);
    		fmDateEnd =  dateFormat.parse(dateEnd);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body("Invalid date format");
		}
    	
    	List<WeatherRecord> weatherRecords = fetchData(fmDateStart, fmDateEnd);
    	if(weatherRecords.isEmpty())
    		return ResponseEntity.badRequest().body("No data found");
    	else 
    		return ResponseEntity.ok(weatherRecords);
    }


}