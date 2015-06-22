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

    if (!validateRouteData(listOfErrors)) {
        // TODO make display of errors "more pretty"
        alert(listOfErrors);
        return;
    }

    // Send information to backend
    return $.post("/route", {data: JSON.stringify(route)}).done(function() {
            // TODO (Louisa / Marjan)  wie soll Speicherung signalisiert werden (hübscher)?
            console.log(route);
            // TODO atm it shows "success" when route has been SENT successfully but it doesnt't
            // check if route was really SAVED successfully
            alert( "Die Route wurde erfolgreich gespeichert" );
            //window.location.reload();
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

    if (!validateRouteData(listOfErrors)) {
        // TODO make display of errors "more pretty"
        alert(listOfErrors);
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
        //window.location.reload();
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

// Returns list of errorHandlers (Strings) if route data is invalid, else returns empty list
function getRouteDataErrors(route) {
    var occurredErrors = [];

    // Catch if user didn't type a valid name
    if (!validateRouteName(route.name)) {
        occurredErrors.push(errorHandlerRouteName());
    }

    // Catch if user didn't select a bike
    if (!validateBikeId(route.bikeID)) {
        occurredErrors.push(errorHandlerBikeId());
    }

    // Catch if startAt is empty, invalid or wrong format
    if ((route.startAt === "") || !validateDateFormat(route.startAt) || !validateDate(route.startAt)) {
        occurredErrors.push(errorHandlerStartAt());
    }

    // Catch if finshedAt is empty, invalid or wrong format
    if ((route.finishedAt === "") || !validateDateFormat(route.finishedAt) || !validateDate(route.finishedAt)) {
        occurredErrors.push(errorHandlerFinishedAt());
    }

    var comparisonDates = compareTwoDateStrings(route.startAt, route.finishedAt);

    // Catch if finishedAt is prior to startAt
    if (comparisonDates === 1) {
        occurredErrors.push(errorHandlerComparisonDates());
    }

    return occurredErrors;
}

// Returns -1 if dateOne is smaller, 0 when they are the same and +1 if dateOne is bigger
window.compareTwoDateStrings = function(dateOne, dateTwo) {
    // DateOne is smaller
    if (dateOne  <  dateTwo) return -1;
    // Both dates have same value
    if (dateOne === dateTwo) return  0;
    // (else) DateOne is bigger
    return  1;
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
// Returns boolean: true if entered route name is valid
function validateRouteName(routeName) {
    if (routeName === "") {
        return false;
    }
    return true;
}

// Returns boolean: true if entered bikeID is valid
function validateBikeId(bikeID) {
    if (bikeID === "") {
        return false;
    }
    return true;
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

// Returns true if format is yyyy-mm-dd hh:mm
window.validateDateFormat = function(date) {
    //            yyyy -       MM      -       dd           hh     :   mm
    var regex = /^\d{4}-(0[1-9]|1[0-2])-([0-2]\d|3[01]) ([01]\d|2[0-3]):[0-5]\d$/;
    return regex.test(date);
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

function errorHandlerRouteName() {
    return "Die Route kann ohne Namen nicht gespeichert werden.";
}

function errorHandlerBikeId() {
    return "Bitte w\u00e4hle ein Fahhrad aus.";
}

function errorHandlerStartAt() {
    return "Der Startzeitpunkt ist ung\u00fcltig oder hat das falsche Format, bitte w\u00e4hle ihn aus dem Kalender aus.";
}

function errorHandlerFinishedAt() {
    return "Der Endzeitpunkt ist ung\u00fcltig oder hat das falsche Format, bitte w\u00e4hle ihn aus dem Kalender aus.";
}

function errorHandlerComparisonDates() {
    return "Der Startzeitpunkt muss vor dem Endzeitpunkt der Fahrt liegen.";
}