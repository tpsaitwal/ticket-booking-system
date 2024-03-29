package com.mos.ticket.booking.system.dao;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.dao.pojo.RowAndColumsOfShow;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeatDao {
    RowAndColumsOfShow getNumberOfSeats(final String cityName,
                                        final String theaterName,
                                        final String movieName,
                                        final Date showDate);
    Map<BookingStatus, List<Integer>> getSeatStatusFromShowId(final String showId, final List<String> bookingStatuses);
}
