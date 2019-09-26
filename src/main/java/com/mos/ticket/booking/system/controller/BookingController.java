package com.mos.ticket.booking.system.controller;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.dao.impl.BookingDaoImpl;
import com.mos.ticket.booking.system.dao.impl.SeatDaoImpl;
import com.mos.ticket.booking.system.dao.pojo.ProcessingPOJO;
import com.mos.ticket.booking.system.helper.SeatUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "${rest.basepath}")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private SeatDaoImpl cityDao;

    @Value("${show.date.rest.format}")
    private String restDateFormat;

    @Value("${show.date.rest.regular.expression}")
    private String restDateRegex;

    @Value("${waiting.msec.booking.retry}")
    private long waitMilliSeconds;

    @Value("${rest.basepath}")
    private String basePath;

    @Value("${rest.show.payment.status}")
    private String paymentPath;

    @Autowired
    private BookingDaoImpl bookingDao;

    @GetMapping(value = "${rest.show.availabilty}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCurrentStateOfTickets(
            @RequestParam(name = "city") String cityName,
            @RequestParam(name = "theater") String theaterName,
            @RequestParam(name = "movie") String movieName,
            @RequestParam(name = "showdatetime") String showDate
    ){
        log.info("Details entered: cityName: {}, theaterName: {}, movieName: {}, showDate: {}",
                cityName, theaterName, movieName, showDate);

        if (!showDate.matches(restDateRegex)){
            return new ResponseEntity("showDate is empty or not in required format - "+ restDateFormat, HttpStatus.NOT_ACCEPTABLE);
        }

        SimpleDateFormat restFormat = new SimpleDateFormat(restDateFormat);
        Date inputDate = null;
        try {
            inputDate = restFormat.parse(showDate);
            if (inputDate.before(Calendar.getInstance().getTime())){
                return new ResponseEntity("Booking of historical date not possible!", HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ParseException e) {
            log.error("Date parsing error {}", ExceptionUtils.getStackTrace(e));
        }

        ProcessingPOJO  processingPOJO = cityDao.getNumberOfSeats(cityName,theaterName,movieName,inputDate);
        Map busySeats = cityDao.getSeatStatusFromShowId(processingPOJO.getShowId(), getStatusListForQuery());
        Map currentState = SeatUtils.getSeatDistribution(processingPOJO.getNoOfRows(), processingPOJO.getNoOfColumns(), busySeats);
        return new ResponseEntity(currentState, HttpStatus.OK);
    }


    @GetMapping(value = "${rest.show.book.tkts}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity bookTickets(
            @RequestParam(name = "city") String cityName,
            @RequestParam(name = "theater") String theaterName,
            @RequestParam(name = "movie") String movieName,
            @RequestParam(name = "showdatetime") String showDate,
            @RequestParam(name = "seats") List<Integer> seats
    ){
        log.info("Details entered: cityName: {}, theaterName: {}, movieName: {}, showDate: {}, seats: {}",
                cityName, theaterName, movieName, showDate, seats);

        if (!showDate.matches(restDateRegex)){
            return new ResponseEntity("showDate is empty or not in required format - "+ restDateFormat, HttpStatus.NOT_ACCEPTABLE);
        }

        SimpleDateFormat restFormat = new SimpleDateFormat(restDateFormat);
        Date inputDate = null;
        try {
            inputDate = restFormat.parse(showDate);
            if (inputDate.before(Calendar.getInstance().getTime())){
                return new ResponseEntity("Booking of historical date not possible!", HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ParseException e) {
            log.error("Date parsing error {}", ExceptionUtils.getStackTrace(e));
        }

        ProcessingPOJO  processingPOJO = cityDao.getNumberOfSeats(cityName,theaterName,movieName,inputDate);
        Map busySeats = cityDao.getSeatStatusFromShowId(processingPOJO.getShowId(),getStatusListForQuery());
        if(SeatUtils.seatValidation(busySeats,seats)){
            return new ResponseEntity( "One or more of seats " + seats +" are booked/blocked.</br> Please select different seats or try after " +
                    (waitMilliSeconds/ (1000*60))+ " minutes.", HttpStatus.NOT_ACCEPTABLE);
        }
        long bookingId = bookingDao.doBooking(cityName,theaterName,movieName,inputDate,seats);
        if (bookingId != 0 )
            return new ResponseEntity(getHtmlResponse(bookingId), HttpStatus.OK);
        else
            return new ResponseEntity("Booking failed", HttpStatus.OK);
    }

    @GetMapping(value = "${rest.show.payment.status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity doPayment(
            @RequestParam(name = "bookingid") long bookingId
    ){
        log.info("Payment done for bookingId {}", bookingId);
        int updatedRecords = bookingDao.updateBookingStatus(bookingId, BookingStatus.BOOKED);
        if (updatedRecords>0)
            return new ResponseEntity("Booking success", HttpStatus.OK);
        else
            return new ResponseEntity("Booking failed", HttpStatus.OK);
    }

    /**
     * This modifies html file for uri and booking id to render it to browser.
     */
    private String getHtmlResponse(final long bookingId){
        File htmlfile = new File("src/main/resources/payment.html");
        String htmlString = null;
        try {
            htmlString = FileUtils.readFileToString(htmlfile);
        } catch (IOException e) {
            log.error("Exception in HTML file reading {}", ExceptionUtils.getStackTrace(e));
        }
        if(htmlString != null) {
            try {
                htmlString = htmlString.replace("$bookingid", Long.toString(bookingId));
                htmlString = htmlString.replace("$host", InetAddress.getLocalHost().getHostName());
                htmlString = htmlString.replace("$port", System.getProperty("server.port"));
                htmlString = htmlString.replace("$path", basePath+paymentPath);
            } catch (UnknownHostException e) {
                log.error("Error during fetching of hostname");
            }
        }
        return htmlString;
    }

    private static List<String> getStatusListForQuery(){
        List<String> statusForQuery = new ArrayList<>();
        statusForQuery.add(BookingStatus.BOOKED.name());
        statusForQuery.add(BookingStatus.BOOKING_UNDER_PROGRESS.name());
        return statusForQuery;
    }
}
