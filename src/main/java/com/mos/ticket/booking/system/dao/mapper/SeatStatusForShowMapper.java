package com.mos.ticket.booking.system.dao.mapper;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.constants.columnames.BookingColumn;
import com.mos.ticket.booking.system.dao.pojo.SeatStatusForShow;
import com.mos.ticket.booking.system.helper.SeatUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatStatusForShowMapper implements RowMapper<SeatStatusForShow> {
    @Override
    public SeatStatusForShow mapRow(ResultSet resultSet, int i) throws SQLException {
        SeatStatusForShow seatStatusForShow = new SeatStatusForShow();
        seatStatusForShow.setBookingStatus(BookingStatus.valueOf(resultSet.getString(BookingColumn.B_STATUS.name())));
        seatStatusForShow.setSeatId(SeatUtils.getSeatIdsFromString(resultSet.getString(BookingColumn.SEAT_IDS.name())));
        return seatStatusForShow;
    }
}