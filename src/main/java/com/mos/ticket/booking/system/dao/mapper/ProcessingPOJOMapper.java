package com.mos.ticket.booking.system.dao.mapper;

import com.mos.ticket.booking.system.constants.columnames.ShowColumn;
import com.mos.ticket.booking.system.dao.pojo.ProcessingPOJO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessingPOJOMapper implements RowMapper<ProcessingPOJO> {

    @Override
    public ProcessingPOJO mapRow(ResultSet resultSet, int i) throws SQLException {
        ProcessingPOJO processingPOJO = new ProcessingPOJO();
        processingPOJO.setNoOfRows(resultSet.getInt(ShowColumn.S_ROWS.name()));
        processingPOJO.setNoOfColumns(resultSet.getInt(ShowColumn.S_COLUMNS.name()));
        processingPOJO.setShowId(resultSet.getString(ShowColumn.S_ID.name()));
        return processingPOJO;
    }
}
