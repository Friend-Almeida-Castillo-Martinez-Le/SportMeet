"use strict";

mapboxgl.accessToken = mapboxKey;
const map = new mapboxgl.Map({
    container: 'map', // container ID
    style: 'mapbox://styles/mapbox/streets-v11', // style URL
    center: [-98.4916, 29.4252], // starting position [lng, lat]
    zoom: 12, // starting zoom
    projection: 'globe' // display the map as a 3D globe
});
map.on('style.load', () => {
    map.setFog({}); // Set the default atmosphere style
});

function geocode(search, token) {
    var baseUrl = 'https://api.mapbox.com';
    var endPoint = '/geocoding/v5/mapbox.places/';
    return fetch(baseUrl + endPoint + encodeURIComponent(search) + '.json' + "?" + 'access_token=' + token)
        .then(function (res) {
            return res.json();
            // to get all the data from the request, comment out the following three lines...
        }).then(function (data) {
            return data.features[0].center;
        });
}

function reverseGeocode(coordinates, token) {
    var baseUrl = 'https://api.mapbox.com';
    var endPoint = '/geocoding/v5/mapbox.places/';
    return fetch(baseUrl + endPoint + coordinates.lng + "," + coordinates.lat + '.json' + "?" + 'access_token=' + token)
        .then(function (res) {
            return res.json();
        })
        // to get all the data from the request, comment out the following three lines...
        .then(function (data) {
            console.log(data)
            return data.features[3].place_name;
        });
}

function weatherDisplay(lat, lon) {
    $.get('https://api.openweathermap.org/data/2.5/onecall', {
        APPID: wKey,
        lat: lat,
        lon: lon,
        units: "imperial"
    }).done(function (results) {
        console.log(results);
        let day;
        let date;
        let dateWithSlashes = eventDate.replaceAll('-','/');
        for (let x = 0; x < 8; x ++) {
            let resultsOfDay = results.daily[x];
            day = new Date(results.daily[x].dt * 1000);
            date = new Date(dateWithSlashes);
            if (new Date(resultsOfDay.dt * 1000).toDateString() === date.toDateString()) {
                let minTemp = resultsOfDay.temp.min;
                let maxTemp = resultsOfDay.temp.max
                let weatherIcon = resultsOfDay.weather[0].icon;
                let weatherDescription = resultsOfDay.weather[0].description;
                console.log(minTemp);
                console.log(maxTemp);
                console.log(weatherIcon);
                console.log(weatherDescription);
                $('#day-1').children().first().html(date.toDateString());
                $('#day-1').children().first().css('text-align', 'center');
                $('#day-1').children().children().first().html('<span class="fw-bold">' + maxTemp + '°F / ' + minTemp + '°F </span>' + '<br>' + '<img src="" id="icon-1">')
                $('#icon-1').attr('src', "http://openweathermap.org/img/wn/" + weatherIcon + "@2x.png")
                $('#day-1').children().children().first().css('text-align', 'center')
                $('#day-1').children().children().first().next().html( '<span class="fw-bold">' + weatherDescription + '</span>')
                $('#weather').css('visibility', 'visible');
                break;
            }
        }

    })
}

geocode(geocodeLoc, mapboxKey).then(function (data) {
    mapboxgl.accessToken = mapboxKey;
    const map = new mapboxgl.Map({
        container: 'map', // container ID
        style: 'mapbox://styles/mapbox/streets-v11', // style URL
        center: [data[0], data[1]], // starting position [lng, lat]
        zoom: 12, // starting zoom
        projection: 'globe' // display the map as a 3D globe
    });
    map.on('style.load', () => {
        map.setFog({}); // Set the default atmosphere style
    });
    const NewMARKER = new mapboxgl.Marker()
        .setLngLat([data[0], data[1]])
        .addTo(map);
    weatherDisplay(data[1],data[0]);
})


