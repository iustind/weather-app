var weatherData = null;

$(document).ready(function() {

    load_weather_data();

    $("#weather-form").submit(function(event) {
        event.preventDefault();
        filter();
    });
});

function insertTableRow(currentIndex, item) {
    var weatherTable = document.getElementById("weather-table");
    var row = weatherTable.insertRow(currentIndex);
    var cell1 = row.insertCell(0); // Station Name
    var cell2 = row.insertCell(1); // Date
    var cell3 = row.insertCell(2); // Mean Temperature

    cell1.innerHTML = "<label>" + item.station_name + "</label>";
    cell2.innerHTML = "<label>" + item.date + "</label>";
    cell2.className = "uk-text-center";
    cell3.innerHTML = "<label class=\"uk-badge\" onclick='clickRow(" + (currentIndex - 1) + ")'>" + item.meanTemp + "</label>";
    cell3.className = "uk-text-right";	
}

function populateTable(data) {
    var currentIndex = 1;
    data.forEach(function(item) {
        insertTableRow(currentIndex, item);
        currentIndex++;
    })

}

function load_weather_data() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/list",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
            populateTable(data);
            weatherData = data;
        },
        error: function(e) {
            console.log("ERROR : ", e);
        }
    });
}

function filter() {
    var fromDate = $("#date-start").val();
    var toDate = $("#date-end").val();

	// client side validation:
    if ( checkDateRange(fromDate, toDate) === false )
    	return false;
    
    $("#weather-table:not(:first)").remove();

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/list/" + fromDate + "/" + toDate,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
            populateTable(data);
            weatherData = data;
        },
        error: function(e) {
            alert(e.responseText)
        }
    });
}

function clickRow(index) {
	/* For a single page application approach, we can use a modal dialog for displaying the details, like this:
	var modal = UIkit.modal("#modalDetails");
	$("#blockquote-station-name").html(weatherData[index].station_name);
	$("#blockquote-province").html(weatherData[index].province);
	$("#blockquote-date").html(weatherData[index].date);
	$("#blockquote-meanTemp").html(weatherData[index].meanTemp);
	$("#blockquote-lowestMonthly_Min_Temp").html(weatherData[index].lowestMonthly_Min_Temp);
	$("#blockquote-highest_Monthly_Max_Temp").html(weatherData[index].highest_Monthly_Max_Temp);
	modal.show();
	*/
	
    localStorage.setItem("station", weatherData[index].station_name);
    localStorage.setItem("province", weatherData[index].province);
    localStorage.setItem("date", weatherData[index].date);
    localStorage.setItem("meanTemp", weatherData[index].meanTemp);
    localStorage.setItem("lowestMonthly_Min_Temp", weatherData[index].lowestMonthly_Min_Temp);
    localStorage.setItem("highest_Monthly_Max_Temp", weatherData[index].highest_Monthly_Max_Temp);


    location.href = "/details"
}

function checkDateRange(dtStart, dtEnd) {

	console.log(dtStart);
	var startDate = Date.parse(dtStart);
	var endDate = Date.parse(dtEnd);
	console.log(startDate);
   
	if (isNaN(startDate)) {
		UIkit.modal.alert("The start date provided is not valid, please enter a valid date.");
		return false;
	}
	if (isNaN(endDate)) {
		UIkit.modal.alert("The end date provided is not valid, please enter a valid date.");
		return false;
	}

	var difference = (endDate - startDate) / (86400000 * 7);
	if (difference < 0) {
		UIkit.modal.alert("The start date must come before the end date.");
		return false;
	}
	if (difference <= 1) {
		UIkit.modal.alert("The range must be at least seven days apart.");
		return false;
	}
	return true;
}