// Array to store flight samples fetched from the API
let samples = [];

// Initialize the Leaflet map and set the default view to coordinates [0, 0] with zoom level 13
let map = L.map('map').setView([0, 0], 13);

// Add OpenStreetMap tile layer to the map with attribution
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap'
}).addTo(map);

// Fetch flight samples from the API and render them on the map
fetch(`/api/flights/${flightId}/samples`)
    .then(r => r.json()) // Parse the response as JSON
    .then(data => {
        samples = data; // Store the fetched samples
        const latlngs = samples.map(s => [s.latitude, s.longitude]); // Extract latitude and longitude for each sample
        const poly = L.polyline(latlngs).addTo(map); // Draw a polyline connecting the sample points
        map.fitBounds(poly.getBounds()); // Adjust the map view to fit the polyline bounds
        marker = L.marker(latlngs[0]).addTo(map); // Add a marker at the starting point
    });

// Variables for the marker, replay timer, and current sample index
let marker, timer, idx = 0;

/**
 * Starts the replay of the flight path by moving the marker along the sample points.
 * The replay runs at 10x real-time speed.
 */
function startReplay() {
    if (timer) return; // Prevent multiple timers from being started
    const speed = 10; // Replay speed multiplier (10x real-time)
    timer = setInterval(() => {
        if (idx >= samples.length) {
            clearInterval(timer); // Stop the timer when all samples are replayed
            return;
        }
        const s = samples[idx++]; // Get the current sample and increment the index
        marker.setLatLng([s.latitude, s.longitude]); // Update the marker position
    }, 1000 / speed); // Set the interval duration based on the replay speed
}