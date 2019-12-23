package com.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author		iustin.dumitru
 * @version		1.0
 * 
 * Class for implementing URLs to templates mapping
 */

@Controller
public class URLController {

    private final Logger logger = LoggerFactory.getLogger(URLController.class);

    @GetMapping("/")
    public String index() {
    	logger.info("Main page requested");
        return "table";
    }

    @GetMapping("/details")
    public String details() {
    	logger.info("Details page requested");
        return "details";
    }
    
    @GetMapping("/*")
    public String resourceNotFound() {
        return "PageNotFound";
    }
}