package com.mos.ticket.booking.system.dao.impl;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.dao.SeatDao;
import com.mos.ticket.booking.system.dao.mapper.RowAndColumsOfShowMapper;
import com.mos.ticket.booking.system.dao.mapper.SeatStatusForShowMapper;
import com.mos.ticket.booking.system.dao.pojo.RowAndColumsOfShow;
import com.mos.ticket.booking.system.dao.pojo.SeatStatusForShow;
import com.mos.ticket.booking.system.helper.SeatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class SeatDaoImpl implements SeatDao {

    private static final Logger log = LoggerFactory.getLogger(SeatDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${select.rows.and.columns}")
    private String selectRowsAndColumnsSql;

    @Value("${show.date.query.format}")
    private String queryDateFormat;

    @Value("${seat.state.from.showid}")
    private String queryForCurrentBookingStateBasedOnShowId;

    @PostConstruct
    private void init(){
        jdbcTemplate.setFetchSize(1000);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /**
     * This will fetch the total number of seats for a show based on city, theater, movie and show date.
     */
    @Override
    @Transactional
    public RowAndColumsOfShow getNumberOfSeats(String cityName, String theaterName, String movieName, Date showDate) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("cityname", cityName);
        mapSqlParameterSource.addValue("theatername", theaterName);
        mapSqlParameterSource.addValue("moviename", movieName);
        SimpleDateFormat queryFormat = new SimpleDateFormat(queryDateFormat);
        mapSqlParameterSource.addValue("showdate", queryFormat.format(showDate));

        try {
            return this.namedParameterJdbcTemplate.queryForObject(selectRowsAndColumnsSql, mapSqlParameterSource, new RowAndColumsOfShowMapper());
        } catch (EmptyResultDataAccessException em){
            log.info("no data found");
            return null;
        }
    }

    /**
     * This will fetch the status of seats available for particular show based on showId.
     */
    @Override
    @Transactional
    public Map<BookingStatus, List<Integer>> getSeatStatusFromShowId(String showId, List<String> bookingStatuses) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("showid", showId);
        mapSqlParameterSource.addValue("statuses", bookingStatuses);
        List<SeatStatusForShow> statusForShowList =
                this.namedParameterJdbcTemplate.query(queryForCurrentBookingStateBasedOnShowId, mapSqlParameterSource, new SeatStatusForShowMapper());
        return SeatUtils.distinguishSeats(statusForShowList);
    }
}
