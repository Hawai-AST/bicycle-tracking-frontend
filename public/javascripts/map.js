window.loadMap = function(currentUserAddress, control) {
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

    // Intitialize total distance of route
    var lengthInKm;

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