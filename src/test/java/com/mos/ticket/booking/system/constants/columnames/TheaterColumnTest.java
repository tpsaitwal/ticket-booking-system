package com.mos.ticket.booking.system.constants.columnames;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TheaterColumnTest {
    @Test
    public void testEnums(){
        assertEquals("T_ID",TheaterColumn.T_ID.name());
        assertEquals("T_NAME",TheaterColumn.T_NAME.name());
        assertEquals("C_ID", TheaterColumn.C_ID.name());
    }
}