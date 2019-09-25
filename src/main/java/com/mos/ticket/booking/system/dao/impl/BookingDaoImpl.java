package com.mos.ticket.booking.system.dao.impl;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.dao.BookingDao;
import com.mos.ticket.booking.system.dao.mapper.ShowIdBookingIdMapper;
import com.mos.ticket.booking.system.dao.pojo.ShowIdBookingId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class BookingDaoImpl implements BookingDao {

    private static final Logger log = LoggerFactory.getLogger(BookingDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${show.date.query.format}")
    private String queryDateFormat;

    @Value("${select.showid.theaterid.for.booking}")
    private String queryForShowIdTheaterId;

    @Value("${insert.booking.sql}")
    private String insertBookingSql;

    @Value("${update.booking.status}")
    private String updateBookingStatus;

    @Value("${seat.booking.update.status.query}")
    private String updateBookingStatusForArchivalSql;

    @PostConstruct
    private void init(){
        jdbcTemplate.setFetchSize(1000);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /**
     * This function will initiate the booking
     */
    @Override
    @Transactional
    public long doBooking(final String cityName,
                          final String theaterName,
                          final String movieName,
                          final Date showDate,
                          final List<Integer> seats) {
        ShowIdBookingId showIdBookingId = getShowIdTheaterId(cityName, theaterName, movieName, showDate);

        Calendar calendar = Calendar.getInstance();
        long bookingId = calendar.getTimeInMillis();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("bookingid", bookingId);
        mapSqlParameterSource.addValue("showid", showIdBookingId.getShowId());
        StringBuilder stringBuilder= new StringBuilder();
        seats.forEach(seat -> stringBuilder.append(seat + ","));
        stringBuilder.replace( stringBuilder.lastIndexOf(","), stringBuilder.length(), "");
        mapSqlParameterSource.addValue("seatids", stringBuilder.toString());
        mapSqlParameterSource.addValue("amount", Math.round(showIdBookingId.getTicketPrice() * seats.size()));
        mapSqlParameterSource.addValue("bookingstatus", BookingStatus.BOOKING_UNDER_PROGRESS.name());
        mapSqlParameterSource.addValue("theaterid", showIdBookingId.getTheaterId());
        int noOfrows = this.namedParameterJdbcTemplate.update(insertBookingSql, mapSqlParameterSource);

        if( noOfrows > 0){
            return bookingId;
        }
        return 0;
    }

    /**
     * This function will return showId and theaterId for booking.
     */
    @Override
    @Transactional
    public ShowIdBookingId getShowIdTheaterId(final String cityName,
                                              final String theaterName,
                                              final String movieName,
                                              final Date showDate) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("cityname", cityName);
        mapSqlParameterSource.addValue("theatername", theaterName);
        mapSqlParameterSource.addValue("moviename", movieName);
        SimpleDateFormat queryFormat = new SimpleDateFormat(queryDateFormat);
        mapSqlParameterSource.addValue("showdate", queryFormat.format(showDate));

        try {
            return this.namedParameterJdbcTemplate.queryForObject(queryForShowIdTheaterId, mapSqlParameterSource, new ShowIdBookingIdMapper());
        } catch (EmptyResultDataAccessException em){
            log.info("no data found");
            return null;
        }
    }

    /**
     * This function will update the booking status as given input for booking id.
     */
    @Override
    @Transactional
    public int updateBookingStatus(long bookingId, final BookingStatus bookingStatus) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("bookingid", bookingId);
        mapSqlParameterSource.addValue("bookingstatus", bookingStatus.name());
        return this.namedParameterJdbcTemplate.update(updateBookingStatus, mapSqlParameterSource);
    }

    @Override
    @Transactional
    public int updateBookingStatusForArchival(BookingStatus currentStatus, BookingStatus expectedStatus) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("currstatus", currentStatus.name());
        mapSqlParameterSource.addValue("expectedstatus", expectedStatus.name());
        return this.namedParameterJdbcTemplate.update(updateBookingStatusForArchivalSql, mapSqlParameterSource);
    }
}
