package com.mos.ticket.booking.system.constants.columnames;

import com.mos.ticket.booking.system.dao.pojo.Booking;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookingColumnTest {

    @Test
    public void testEnums(){
        assertEquals("B_ID", BookingColumn.B_ID.name());
        assertEquals("S_ID", BookingColumn.S_ID.name());
        assertEquals("SEAT_IDS", BookingColumn.SEAT_IDS.name());
        assertEquals("B_AMOUNT", BookingColumn.B_AMOUNT.name());
        assertEquals("B_STATUS", BookingColumn.B_STATUS.name());
        assertEquals("B_DATE", BookingColumn.B_DATE.name());
        assertEquals("T_ID", BookingColumn.T_ID.name());
    }
}