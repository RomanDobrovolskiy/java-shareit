package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long booker_id);

    List<Booking> findAllByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(Long booker_id, LocalDateTime end, LocalDateTime start);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long booker_id, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long booker_id, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(Long booker_id, LocalDateTime start, Status status);

    List<Booking> findAllByBookerIdAndStatusIsOrderByStartDesc(Long booker_id, Status status);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "order by b.start desc")
    List<Booking> getAllUsersItemsBookings(Long userId);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and ?2 between b.start and b.end order by b.start desc")
    List<Booking> getCurrentUsersItemsBookings(Long userId, LocalDateTime nowTime);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.end < ?2 order by b.start desc")
    List<Booking> getPastUsersItemsBookings(Long userId, LocalDateTime nowTime);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.start > ?2 order by b.start desc")
    List<Booking> getFutureUsersItemsBookings(Long userId, LocalDateTime nowTime, Status status);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.start > ?2 and b.status = ?3 order by b.start desc")
    List<Booking> getWaitingUsersItemsBookings(Long userId, LocalDateTime nowTime, Status status);

    @Query("select b from Booking b Inner join Item i on b.item.id = i.id where i.ownerId = ?1 " +
            "and b.status = ?2 order by b.start desc")
    List<Booking> getRejectedUsersItemsBookings(Long userId, Status status);


    //    Для сортировки и фильтрации на стороне БД
    @Query(value = "SELECT * FROM bookings b INNER JOIN items i on i.id = b.item_id "
            + "WHERE b.item_id = ?1 AND b.end_time < ?2 ORDER BY b.end_time ASC LIMIT 1",
            nativeQuery = true)
    Optional<Booking> getLastItemBooking(Long itemId, LocalDateTime nowTime);

    @Query(value = "SELECT * FROM bookings b INNER JOIN items i on i.id = b.item_id "
            + "WHERE b.item_id = ?1 and b.start_time > ?2 ORDER BY b.start_time ASC LIMIT 1",
            nativeQuery = true)
    Optional<Booking> getNextItemBooking(Long itemId, LocalDateTime nowTime);

    List<Booking> findAllByItemAndBookerIdAndStatusIsAndEndIsBefore(Item item, Long booker_id, Status status, LocalDateTime end);
}
