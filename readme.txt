This is a simple web application to visualize DJI flight logs in CSV format. The app is built using Spring Boot and Thymeleaf.

Find your .txt flight log file on your phone under /FlightRecord and convert it to CSV.

Use one of the  converters available online for this:
https://www.phantomhelp.com/logviewer/upload/  (recommended)
https://app.airdata.com/dji-log-converter
https://phantompilots.com/threads/tool-win-offline-txt-flightrecord-to-csv-converter.70428/
https://github.com/dji-sdk/FlightRecordParsingLib

For testing and demonstration purposes, use the
/FlightRecord/Converted to CSV/demo flight one.csv
/FlightRecord/Converted to CSV/demo flight two.csv

Start the app from the IDE, go to localhost:8080 and upload the CSV log file to view flight data.

The interface looks like this:

![Map + Altitude Replay](./docs/ExampleScreenshot.jpg)