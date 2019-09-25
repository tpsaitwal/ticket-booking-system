Setup
Run attached sql scripts in Database
1) BOOKING.sql
2) CITY.sql
3) MOVIE.sql
4) SHOW.sql
5) THEATER.sql

How to run:
-DCONFIG_LOCATION=file:"<<path-to>>\ticket-booking-system.properties"
-DLOG_PATH=<<path-to-logfile>>
-Dserver.port=<<port>>


URI's exposed
1) To check the status of Booked, Available and In Progress seats
http://localhost:8462/tbs/currstateoftkts?city=CHENNAI&theater=INOX&movie=Mission%20Mangal&showdatetime=29-0-2019-20-00
2) To make the booking
http://localhost:8462/tbs/booktkts?city=CHENNAI&theater=INOX&movie=Mission Mangal&showdatetime=29-09-2019-20-00&seats=101,102