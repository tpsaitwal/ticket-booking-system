package com.mos.ticket.booking.system.dao;

import java.util.List;

public interface TheaterDao {
    List<String> getTheatersInACity(String cityId);
}
