window.addEventListener('DOMContentLoaded', () => {
    // ---- Map (always render) ----
    if (typeof L === 'undefined') {
        console.error('Leaflet not loaded'); return;
    }
    const map = L.map('map').setView([48.137, 11.576], 5); // default view
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution:'&copy; OpenStreetMap'
    }).addTo(map);

    // ---- Fetch samples ----
    fetch(`/api/flights/${flightId}/samples`)
        .then(r => r.json())
        .then(raw => {
            console.log('Raw samples:', raw.length, raw.slice(0,3));
            // Drop leading 0/0 coords
            const firstValid = raw.findIndex(s => Number(s.latitude)!==0 && Number(s.longitude)!==0);
            const data = (firstValid > 0 ? raw.slice(firstValid) : raw).map(s => ({
                ts: Number(s.ts),
                lat: Number(s.latitude),
                lon: Number(s.longitude),
                alt: Number(s.altitude),
                spd: Number(s.speed),
                bat: Number(s.battery)
            }));
            if (data.length === 0) { console.warn('No valid GPS points.'); return; }

            // ---- Draw track & marker ----
            const latlngs = data.map(p => [p.lat, p.lon]);
            const poly = L.polyline(latlngs).addTo(map);
            map.fitBounds(poly.getBounds());
            const marker = L.marker(latlngs[0]).addTo(map);

            // ---- Altitude chart (index-based x axis, guaranteed to draw) ----
            const ctx = document.getElementById('altChart').getContext('2d');

            // Build {x: index, y: altitude}
            const pts = data.map((p, i) => ({ x: i, y: p.alt }));
            console.log('Chart points:', pts.length, pts.slice(0, 3));

            const altChart = new Chart(ctx, {
                type: 'line',
                data: {
                    datasets: [
                        {
                            label: 'Altitude (m)',
                            data: pts,
                            parsing: false,
                            borderColor: '#3b82f6',       // explicit blue
                            backgroundColor: 'rgba(59,130,246,0.15)',
                            pointRadius: 0,
                            borderWidth: 2,
                            tension: 0.2
                        },
                        {
                            label: 'Current',
                            data: [],
                            parsing: false,
                            showLine: false,
                            pointRadius: 4,
                            borderColor: '#ef4444',
                            backgroundColor: '#ef4444'
                        }
                    ]
                },
                options: {
                    responsive: false,      // we set fixed canvas size in HTML
                    animation: false,
                    scales: {
                        x: {
                            type: 'linear',
                            title: { display: true, text: 'Sample' }
                        },
                        y: {
                            title: { display: true, text: 'Meters' },
                            // Optional: suggest a sensible range around your altitudes
                            suggestedMin: Math.min(...pts.map(p => p.y)) - 5,
                            suggestedMax: Math.max(...pts.map(p => p.y)) + 5
                        }
                    },
                    plugins: { legend: { display: true } }
                }
            });

            // ---- Replay (kept in sync with index-based x) ----
            const btn = document.getElementById('replayBtn');
            let timer = null, i = 0;
            btn.onclick = () => {
                if (timer || data.length === 0) return;
                const speed = 10; // 10×
                i = 0;
                timer = setInterval(() => {
                    if (i >= data.length) { clearInterval(timer); timer = null; return; }
                    const p = data[i++];
                    marker.setLatLng([p.lat, p.lon]);

                    altChart.data.datasets[1].data = [{ x: i - 1, y: p.alt }];
                    altChart.update('none');
                }, 1000 / speed);
            };
        })
        .catch(err => console.error('Failed to load samples:', err));
});
