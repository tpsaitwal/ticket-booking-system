package com.mos.ticket.booking.system.dao;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.dao.pojo.ShowIdBookingId;

import java.util.Date;
import java.util.List;

public interface BookingDao {
    long doBooking(final String cityName,
                   final String theaterName,
                   final String movieName,
                   final Date showDate,
                   final List<Integer> seats);

    ShowIdBookingId getShowIdTheaterId(final String cityName,
                                       final String theaterName,
                                       final String movieName,
                                       final Date showDate);

    int updateBookingStatus(final long bookingId, final BookingStatus bookingStatus);

    int updateBookingStatusForArchival(final BookingStatus currentStatus, final BookingStatus expectedStatus);
}
