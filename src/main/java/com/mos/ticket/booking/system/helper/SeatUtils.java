package com.mos.ticket.booking.system.helper;

import com.mos.ticket.booking.system.constants.BookingStatus;
import com.mos.ticket.booking.system.dao.pojo.SeatStatusForShow;

import java.util.*;

public class SeatUtils {

    private SeatUtils(){
        throw new IllegalStateException("This is a Utility class");
    }

    /**
     * This function will distribute seats based on number rows and columns and in a theater and
     * segregate them according to their statuses by removing busy seats from available seats list.
     */
    public static Map<BookingStatus, List<Integer>> getSeatDistribution(final int noOfRows, final int noOfColumns,
                                                                        final Map<BookingStatus, List<Integer>> busySeats){

        List<Integer> bookedSeats = Collections.emptyList();
        List<Integer> bookinInProgressSeats = Collections.emptyList();
        if (busySeats != null && !busySeats.isEmpty()){
            bookedSeats  = busySeats.get(BookingStatus.BOOKED);
            bookinInProgressSeats = busySeats.get(BookingStatus.BOOKING_UNDER_PROGRESS);
        }
        Map<BookingStatus, List<Integer>> map = new HashMap();
        List<Integer> availableSeats = new ArrayList<>();
        for(int i = 1; i <=noOfRows*noOfColumns; i++){
            availableSeats.add(i);
        }
        bookedSeats.forEach(availableSeats::remove);
        bookinInProgressSeats.forEach(availableSeats::remove);
        Collections.sort(availableSeats);
        Collections.sort(bookedSeats);
        Collections.sort(bookinInProgressSeats);
        map.put(BookingStatus.AVAILABLE, availableSeats);
        map.put(BookingStatus.BOOKED, bookedSeats);
        map.put(BookingStatus.BOOKING_UNDER_PROGRESS, bookinInProgressSeats);
        return map;
    }

    /**
     * This will distribute the seats according to their status which are fetched from database.
     */
    public static Map<BookingStatus, List<Integer>> distinguishSeats(final List<SeatStatusForShow> statusForShowList){
        if (statusForShowList == null || statusForShowList.isEmpty())
            return Collections.emptyMap();

        List<Integer> bookedSeats = new ArrayList<>();
        List<Integer> bookingUnderProgressSeats = new ArrayList<>();

        statusForShowList.forEach(seatStatusForShow ->  {
            if (BookingStatus.BOOKED.equals(seatStatusForShow.getBookingStatus())){
                seatStatusForShow.getSeatId().forEach(seatId ->{
                    if (!bookedSeats.contains(seatId))
                        bookedSeats.add(seatId);
                });
            }

            if (BookingStatus.BOOKING_UNDER_PROGRESS.equals(seatStatusForShow.getBookingStatus())){
                seatStatusForShow.getSeatId().forEach(seatId ->{
                    if (!bookingUnderProgressSeats.contains(seatId))
                        bookingUnderProgressSeats.add(seatId);
                });
            }
        });

        Map<BookingStatus, List<Integer>> mapOfBookingStatusAndSeatNumber = new HashMap<>();
        mapOfBookingStatusAndSeatNumber.put(BookingStatus.BOOKED, bookedSeats);
        mapOfBookingStatusAndSeatNumber.put(BookingStatus.BOOKING_UNDER_PROGRESS, bookingUnderProgressSeats);
        return mapOfBookingStatusAndSeatNumber;
    }

    /**
     * This is utility to convert a comma separated integer values to list of integers.
     */
    public static List<Integer> getSeatIdsFromString(final String seats){
        if (seats == null || seats.isEmpty())
            return Collections.emptyList();
        List<Integer> seatIds = new ArrayList<>();
        String [] seatArr = seats.split(",");
        for (String seat: seatArr){
            seatIds.add(Integer.parseInt(seat));
        }
        return seatIds;
    }

    /**
     * This will check if the entered seats are already busy seats or not for booking to progress.
     */
    public static boolean seatValidation(final Map<BookingStatus, List<Integer>> busySeats, final List<Integer> seats){
        if (busySeats == null || busySeats.isEmpty())
            return true;

        List<Integer> bookedSeats  = busySeats.get(BookingStatus.BOOKED);
        List<Integer> bookingInProgressSeats = busySeats.get(BookingStatus.BOOKING_UNDER_PROGRESS);

        for (Integer seat : seats){
            if (bookedSeats.contains(seat) || bookingInProgressSeats.contains(seat)){
                return true;
            }
        }
        return false;
    }
}
