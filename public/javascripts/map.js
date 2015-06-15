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

    // Get distance from map, convert to km
    // TODO (Marjan) atm it always picks first route option - should check which route option is the actual used one
    control.addEventListener("routesfound", function(route){
        lengthInKm = route.routes[0].summary.totalDistance / 1000;
    });

    // Initialize map with user's home address unless map is initialized with route
    // TODO (Marjan) check if the problem with reloading in tracks can be solved here
    //var waypoints = control.getWaypoints();
    //if (waypoints[0].latLng == null || waypoints[1].latLng == null) {
    //    findUserLocationOnMap(map, currentUserAddress);
    //}
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
        // Hides routing but also input
        //show: false,
        // Set GraphHopper as router -- IMPORTANT: Only 500 queries per day for free...!
        // TODO (Marjan) look up what happens if free queries used up
        router: L.Routing.graphHopper('0ed0b695-73f5-4c79-98ac-81ebf3da1d5f', { "urlParameters": {'vehicle': "bike"}})
    });
}


// Gets called from "Speichern" button in newtracks
window.exportRoute = function(control, lengthInKm) {
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
    // Set name of the route
    var routeName = document.getElementById("name").value;

    // Catch if user didn't type a name
    if (routeName === "") {
        window.alert("Die Route kann ohne Namen nicht gespeichert werden.");
        return;
    }

    // Get information from Form
    var bikeID = document.getElementById("bike").value;

    if (bikeID === "") {
        window.alert("Bitte w\u00e4hle ein Fahhrad aus.");
        return;
    }

    var startAt = document.getElementById("startAt").value;

    // Catch if user deleted default date and didn't enter new
    if (startAt === "") {
        window.alert("Bitte w\u00e4hle einen Startzeitpunkt.");
        return;
    }

    var finishedAt = document.getElementById("finishedAt").value;

    // Catch if user deleted default date and didn't enter new
    if (finishedAt === "") {
        window.alert("Bitte w\u00e4hle einen Endzeitpunkt.");
        return;
    }

    // TODO (Marjan / Louisa) Catch if format of date is wrong -> disable to write into field only click?
    // TODO (Marjan / Louisa) Catch if finishedAt is before startAt

    var route = {
        name: routeName,
        bikeID: bikeID,
        lengthInKm: lengthInKm,
        startAt: startAt,
        finishedAt: finishedAt,
        waypoints: data
    }

    console.log(route);

    // Send information to backend
    $.post("/route", {data: JSON.stringify(route)}).done(function() {
        // TODO (Louisa / Marjan)  wie soll Speicherung signalisiert werden (h?bscher)?
        console.log(route);
        console.log(JSON.stringify(route));
        alert( "Die Route wurde erfolgreich gespeichert" );
        // TODO (Louisa/ Marjan) When reloading page date fields should reload again to actual day and time
        window.location.reload();
    }).fail(function() {
        alert( "Something went wrong, pls try again." );
    });

}