package com.mos.ticket.booking.system.dao.pojo;

import com.mos.ticket.booking.system.constants.BookingStatus;
import lombok.Data;

@Data
public class Seat {
    private String id;
    private int rowNum;
    private int columnNum;
    private BookingStatus bookingStatus;
}
