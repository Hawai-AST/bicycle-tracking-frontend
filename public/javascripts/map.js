window.loadMap = function(currentUserAddress, control, lengthInKm) {
    // initial view = Hamburg
    var map = setViewToHamburg();

    // Create pop-up for setting waypoints in map
    function createButton(label, container) {
        var btn = L.DomUtil.create('button', '', container);
        btn.setAttribute('type', 'button');
        btn.innerHTML = label;
        return btn;
    }

    // Add start and destination waypoint from clicking on map
    map.on('click', function(e) {
        var container = L.DomUtil.create('div'),
            startBtn = createButton('Start from this location', container),
            destBtn = createButton('Go to this location', container);

        L.popup()
            .setContent(container)
            .setLatLng(e.latlng)
            .openOn(map);

        L.DomEvent.on(startBtn, 'click', function() {
            control.spliceWaypoints(0, 1, e.latlng);
            map.closePopup();
        });

        L.DomEvent.on(destBtn, 'click', function() {
            control.spliceWaypoints(control.getWaypoints().length - 1, 1, e.latlng);
            map.closePopup();
        });
    });

    // Get map itself
    // http://{s}.tiles.mapbox.com/v3/MapID/{z}/{x}/{y}.png
    L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution:
        'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> ' +
        'contributors, ' + '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery ? <a href="http://mapbox.com">Mapbox</a>',
        maxZoom: 18
    }).addTo(map);

    control.addTo(map);

    // Initialize map with user's home address unless map is initialized with route
    var waypoints = control.getWaypoints();
    if (waypoints[0].latLng == null || waypoints[1].latLng == null) {
        return findUserLocationOnMap(map, currentUserAddress);
    } else {
        return map;
    }
}

window.setViewToHamburg = function() {
    return L.map('map').setView([53.5502099, 9.9993636], 9);
}

window.findUserLocationOnMap = function(map, currentUserAddress) {
    // Find user location and move map initially
    return L.Control.Geocoder.nominatim().geocode(
        currentUserAddress,
        function(results){
            // Only moves to user location if address was found
            if (results.length > 0) {
                map.panTo(results[0].center);
                map.fitBounds(results[0].bbox);
            }
        });
}

window.addControl = function() {
    return L.Routing.control({
        routeWhileDragging: true,
        geocoder: L.Control.Geocoder.nominatim(),
        reverseWaypoints: true,
        fitSelectedRoutes: 'truthy',
        // Set GraphHopper as router -- IMPORTANT: Only 500 queries per day for free...!
        // TODO (Marjan) look up what happens if free queries used up
        router: L.Routing.graphHopper('0ed0b695-73f5-4c79-98ac-81ebf3da1d5f', { "urlParameters": {'vehicle': "bike"}})
    });
}

// Gets called from "Speichern" button in newtracks
window.exportRoute = function(control, lengthInKm) {
    // Set name of the route
    var routeName = document.getElementById("name").value;
    var route = buildRoute(control, lengthInKm, routeName);
    var listOfErrors = getRouteDataErrors(route);
    var alertString = "";

    if (!validateRouteData(listOfErrors)) {
        for (var i = 0, length = listOfErrors.length; i < length; i++) {
            alertString = alertString + (i+1).toString() + ".) " + translate(listOfErrors[i]) + "\n";
        }

        alert(alertString);
        return;
    }

    // Send information to backend
    return $.post("/route", {data: JSON.stringify(route)}).done(function() {
            // TODO (Louisa / Marjan)  wie soll Speicherung signalisiert werden (hübscher)?
            console.log(route);
            // TODO atm it shows "success" when route has been SENT successfully but it doesnt't
            // check if route was really SAVED successfully
            alert( "Die Route wurde erfolgreich gespeichert" );
            window.location.reload();
        }).fail(function() {
            errorHandlerPost();
        });
}

// Gets called from "Änderung speichern" button in tracks
window.updateRoute = function(control, lengthInKm) {
    // Set name of the route
    var routeName = document.getElementById('streckenname').value;
    var route = buildRoute(control, lengthInKm, routeName);
    var listOfErrors = getRouteDataErrors(route);
    var alertString = "";

    if (!validateRouteData(listOfErrors)) {
        for (var i = 0, length = listOfErrors.length; i < length; i++) {
            alertString = alertString + (i+1).toString() + ".) " + translate(listOfErrors[i]) + "\n";
        }

        alert(alertString);
        return;
    }

    var tourID = document.getElementById("name").value;

    // Send information to backend
    return $.post("/route/" + tourID, {data: JSON.stringify(route)}).done(function() {
        // TODO (Louisa / Marjan)  wie soll Speicherung signalisiert werden (hübscher)?
        console.log(route);
        // TODO atm it shows "success" when route has been SENT successfully but it doesnt't
        // check if route was really UPDATED successfully
        alert( "Die Route wurde erfolgreich geupdated" );
        window.location.reload();
    }).fail(function() {
        errorHandlerPost();
    });
}

// Returns route saved as Hash
function buildRoute(control, lengthInKm, routeName) {
    var waypoints = control.getWaypoints();

    // Maps has too much information for backend,
    // therewith we build hashes with relevant information
    // data = array of objects, objects = hashes with lat, lng and name
    var data = $.map(waypoints, function(value, index){
        return {
            latitude: value.latLng.lat,
            longitude: value.latLng.lng,
            name: value.name
        }
    });

    // Build route as per API spec
    // Get information from Form
    var bikeID = document.getElementById("bike").value;
    var startAt = document.getElementById("startAt").value;
    var finishedAt = document.getElementById("finishedAt").value;

    return {
        name: routeName,
        bikeID: bikeID,
        lengthInKm: lengthInKm,
        startAt: startAt,
        finishedAt: finishedAt,
        waypoints: data
    }
}

// Returns list of errorHandlers (keys) if route data is invalid, else returns empty list
function getRouteDataErrors(route) {
    var occurredErrors = [];
    // List of all validation functions which need to get called, here is the place to add new ones if needed
    var validators = [validateRouteName, validateBikeId, validateStartAt, validateFinishedAt, validateStartAtBeforeFinishedAt];

    validators.forEach(function(validator) {
            // Calls each validation function, if data is invalid errorHandler is returned else "undefined"
            var error = validator(route);
            // "if (error)" is short for "if error is defined"
            if (error) {
                occurredErrors.push(error);
            }
        }
    );

    return occurredErrors;
}

// Get distance from map, convert to km
// TODO (Marjan) atm it always picks first route option - should check which route option is the actual used one
window.getLengthInKm = function(control) {
    control.addEventListener("routesfound", function(route){
        return lengthInKm = route.routes[0].summary.totalDistance / 1000;
    });
}


//////////////////////////////////////////////////////////////////////////
// Validation functions
//////////////////////////////////////////////////////////////////////////
// Returns errorHandler if name is invalid else undefined
function validateRouteName(route) {
    if (route.name === "") {
        return 'error_handler_route_name';
    }
}

// Returns errorHandler if bikeId is invalid else undefined
function validateBikeId(route) {
    if (route.bikeID === "") {
        return 'error_handler_bike_id';
    }
}

// Returns errorHandler if startAt is invalid else undefined
function validateStartAt(route) {
    if (!validateDateFormat(route.startAt) || !validateDate(route.startAt)) {
        return 'error_handler_start_at';
    }
}

// Returns errorHandler if finishedAt is invalid else undefined
function validateFinishedAt(route) {
    if (!validateDateFormat(route.finishedAt) || !validateDate(route.finishedAt)) {
        return 'error_handler_finished_at';
    }
}

// Returns true if format is yyyy-mm-dd hh:mm
window.validateDateFormat = function(date) {
    //            yyyy -       MM      -       dd           hh     :   mm
    var regex = /^\d{4}-(0[1-9]|1[0-2])-([0-2]\d|3[01]) ([01]\d|2[0-3]):[0-5]\d$/;
    return regex.test(date);
}

// Returns true if date is valid
// This is not really pretty but "new Date()" automatically casts to a valid one so
// this is the only solution I could come up with
window.validateDate = function(_date) {
    // Get day from input date string and convert to integer
    var dayOfParamDate = +_date.substring(8, 10);
    // Replace all occurances of "-" with "/" else it doesn't work in Firefox
    var castDate = _date.replace(/-/g,'/');
    // Cast to valid date if possible
    var date = new Date(castDate);
    // Get day from casted date
    var dayOfDate = date.getDate();
    // Check if days of input and cast are the same otherwise input date wasn't valid
    if (dayOfDate !== dayOfParamDate) {
        return false;
    }

    return !(date == "Invalid Date");
}

// Returns errorHandler if startAt is before finishedAt else undefined
function validateStartAtBeforeFinishedAt(route) {
    // Also checks if startAt and finishedAt are defined at all
    if (route.startAt && route.finishedAt && route.startAt > route.finishedAt) {
        return 'error_handler_comparison_dates';
    }
}

// Returns boolean: true if no errors occurred
function validateRouteData(listOfErrors) {
    return (listOfErrors.length === 0);
}


//////////////////////////////////////////////////////////////////////////
// Error handler
//////////////////////////////////////////////////////////////////////////
function errorHandlerPost() {
    return alert("Something went wrong, please try again");
}

var locale = 'de';
// Set the error handlers for different languages, atm we only support German but here is the place to add new ones
var i18n = {
    'de': {
        'error_handler_route_name': "Die Route kann ohne Namen nicht gespeichert werden.",
        'error_handler_bike_id': "Bitte w\u00e4hle ein Fahrrad aus.",
        'error_handler_start_at': "Der Startzeitpunkt ist ung\u00fcltig oder hat das falsche Format, bitte w\u00e4hle ihn aus dem Kalender aus.",
        'error_handler_finished_at': "Der Endzeitpunkt ist ung\u00fcltig oder hat das falsche Format, bitte w\u00e4hle ihn aus dem Kalender aus.",
        'error_handler_comparison_dates': "Der Startzeitpunkt muss vor dem Endzeitpunkt der Fahrt liegen."
    }
}
// Translates the error handler (key) to the right String depending on wanted language
var translate = function(key) {
    return i18n[locale][key];
}