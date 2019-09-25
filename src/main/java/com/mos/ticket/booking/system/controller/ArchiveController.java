package com.mos.ticket.booking.system.controller;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.dao.impl.BookingDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ArchiveController {

    private static final Logger log = LoggerFactory.getLogger(ArchiveController.class);

    @Autowired
    private BookingDaoImpl bookingDao;

    @Scheduled(cron = "${update.status.of.bookings.cron}")
    public void updateStatusOfBookings(){
        int numberOfBookings = bookingDao.updateBookingStatusForArchival(BookingStatus.BOOKING_UNDER_PROGRESS, BookingStatus.AVAILABLE);
        log.info("BookingStatus marked {} for {} bookings", BookingStatus.AVAILABLE, numberOfBookings);
    }
}
