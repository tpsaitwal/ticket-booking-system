package com.mos.ticket.booking.system.dao.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Booking {
    private String bookingId;
    private String showId;
    private List<Integer> seatId;
    private double amount;
    private String bookingStatus;
    private Date bookingDate;
    private String theaterId;
}