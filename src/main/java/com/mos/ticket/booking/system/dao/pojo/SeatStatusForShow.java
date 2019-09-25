package com.mos.ticket.booking.system.dao.pojo;

import com.mos.ticket.booking.system.constants.BookingStatus;
import lombok.Data;

import java.util.List;

@Data
public class SeatStatusForShow {
    private List<Integer> seatId;
    private BookingStatus bookingStatus;
}