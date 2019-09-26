package com.mos.ticket.booking.system.dao.mapper;

import com.mos.ticket.booking.system.constants.columnames.ShowColumn;
import com.mos.ticket.booking.system.dao.pojo.RowAndColumsOfShow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowAndColumsOfShowMapper implements RowMapper<RowAndColumsOfShow> {

    @Override
    public RowAndColumsOfShow mapRow(ResultSet resultSet, int i) throws SQLException {
        RowAndColumsOfShow processingPOJO = new RowAndColumsOfShow();
        processingPOJO.setNoOfRows(resultSet.getInt(ShowColumn.S_ROWS.name()));
        processingPOJO.setNoOfColumns(resultSet.getInt(ShowColumn.S_COLUMNS.name()));
        processingPOJO.setShowId(resultSet.getString(ShowColumn.S_ID.name()));
        return processingPOJO;
    }
}
