package com.mos.ticket.booking.system.constants.columnames;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShowColumnTest {

    @Test
    public void testEnums(){
        assertEquals("S_ID", ShowColumn.S_ID.name());
        assertEquals("S_DATETIME", ShowColumn.S_DATETIME.name());
        assertEquals("M_ID",ShowColumn.M_ID.name());
        assertEquals("S_PRICE",ShowColumn.S_PRICE.name());
        assertEquals("T_ID",ShowColumn.T_ID.name());
        assertEquals("S_ROWS",ShowColumn.S_ROWS.name());
        assertEquals("S_COLUMNS",ShowColumn.S_COLUMNS.name());
    }
}