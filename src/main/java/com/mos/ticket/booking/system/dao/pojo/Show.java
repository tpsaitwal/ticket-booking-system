package com.mos.ticket.booking.system.dao.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Show {
    private String id;
    private Date showTime;
    private String movieId;
    private double ticketPrice;
    private String theaterId;
    private int rows;
    private int columns;
}
