package com.mos.ticket.booking.system.dao.mapper;

import com.mos.ticket.booking.system.constants.columnames.ShowColumn;
import com.mos.ticket.booking.system.constants.columnames.TheaterColumn;
import com.mos.ticket.booking.system.dao.pojo.ShowIdBookingId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowIdBookingIdMapper implements RowMapper<ShowIdBookingId> {

    @Override
    public ShowIdBookingId mapRow(ResultSet resultSet, int i) throws SQLException {
        ShowIdBookingId showIdBookingId = new ShowIdBookingId();
        showIdBookingId.setShowId(resultSet.getString(ShowColumn.S_ID.name()));
        showIdBookingId.setTicketPrice(resultSet.getDouble(ShowColumn.S_PRICE.name()));
        showIdBookingId.setTheaterId(resultSet.getString(TheaterColumn.T_ID.name()));
        return showIdBookingId;
    }
}
