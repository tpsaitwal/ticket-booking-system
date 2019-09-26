package com.mos.ticket.booking.system.constants;

import com.mos.ticket.booking.system.dao.pojo.Booking;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookingStatusTest {

    @Test
    public void testEnums(){
        assertEquals("AVAILABLE", BookingStatus.AVAILABLE.name());
        assertEquals("BOOKING_UNDER_PROGRESS", BookingStatus.BOOKING_UNDER_PROGRESS.name());
        assertEquals("BOOKED", BookingStatus.BOOKED.name());
    }
}