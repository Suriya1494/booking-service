package com.surya.Service;

import com.surya.Dto.BookingRequest;
import com.surya.Dto.SaloonDto;
import com.surya.Dto.ServiceOfferingDto;
import com.surya.Dto.UserDto;
import com.surya.Model.Booking;
import com.surya.Response.ApiResponse;
import com.surya.domain.BookingStatus;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    ResponseEntity<ApiResponse<?>>  createBooking(BookingRequest req,
                                                  UserDto user,
                                                  SaloonDto saloon,
                                                  Set<ServiceOfferingDto> service)throws Exception ;
    ResponseEntity<ApiResponse<?>> getBookingByCustomerId(Long customerId);
//test
    List<Booking> getBookingsBySaloon(Long saloonId);
    ResponseEntity<ApiResponse<?>> updateBokingStatus(Long id, BookingStatus status);

    ResponseEntity<ApiResponse<?>> getBookingById(Long id);

    List<Booking> getBookingsByDate(Long saloonID,LocalDate date);

    ResponseEntity<ApiResponse<?>> getSaloonReport(Long saloonId);



}
