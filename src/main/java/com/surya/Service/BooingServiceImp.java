package com.surya.Service;

import com.surya.Dto.BookingRequest;
import com.surya.Dto.SaloonDto;
import com.surya.Dto.ServiceOfferingDto;
import com.surya.Dto.UserDto;
import com.surya.Model.Booking;
import com.surya.Model.SaloonReport;
import com.surya.Reository.BookingRepository;
import com.surya.Response.ApiResponse;
import com.surya.domain.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooingServiceImp implements BookingService{

    private final ModelMapper mapper;
    private final BookingRepository repo;
    @Override
    public ResponseEntity<ApiResponse<?>> createBooking(BookingRequest booking,
                                                        UserDto user,
                                                        SaloonDto saloon,
                                                        Set<ServiceOfferingDto> service) throws Exception {
        int totalDuration = service.stream()
                .mapToInt(ServiceOfferingDto::getDuration).sum();

        LocalDateTime bookingStartTime=booking.getStartTime();
        LocalDateTime bookingEndTime=bookingStartTime.plusMinutes(totalDuration);

        Boolean isAvaliable=isTimeSlotAvaliable(saloon,bookingStartTime,bookingEndTime);

        int totalPrice=service.stream().mapToInt(ServiceOfferingDto::getPrice).sum();

        Set<Long> idLists=service.stream().map(ServiceOfferingDto::getId)
                .collect(Collectors.toSet());

        Booking book=new Booking();
        book.setCustomerId(user.getId());
        book.setSaloonId(saloon.getId());
        book.setServiceIds(idLists);
        book.setStatus(BookingStatus.PENDING);
        book.setStartTime(bookingStartTime);
        book.setEndTime(bookingEndTime);
        book.setTotalPrice(totalPrice);

        Booking res=repo.save(book);

        return ResponseEntity.ok(new ApiResponse<>("Successfully booking created ",res));
    }
    public Boolean isTimeSlotAvaliable(SaloonDto saloon,
                                       LocalDateTime bookingStartTime,
                                       LocalDateTime bookingEndTime) throws Exception {
        List<Booking> existingBookings=  getBookingsBySaloon(saloon.getId());
        LocalDateTime saloonOpenTime=saloon.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime saloonCloseTime=saloon.getCloseTime().atDate(bookingEndTime.toLocalDate());

        if(bookingStartTime.isBefore(saloonOpenTime)||bookingEndTime.isAfter(saloonCloseTime))
        {
         throw  new Exception("Booking time must in working hours");
        }
        if(!bookingStartTime.toLocalDate().isEqual(LocalDate.now()))
        {
            throw new Exception("no time Slots");
        }
        for(Booking existingBooking:existingBookings)
        {
            LocalDateTime existingStartTime=existingBooking.getStartTime();
            LocalDateTime existingEndTime=existingBooking.getEndTime();
            if(bookingStartTime.isBefore(existingEndTime)
                    &&bookingEndTime.isAfter(existingStartTime))
            {
                throw new Exception("no time Slots");
            }
            if (bookingStartTime.equals(existingStartTime)||bookingEndTime.equals(existingEndTime))
            {
                throw new Exception("no time Slots");
            }
        }
        return true;


    }

    @Override
    public ResponseEntity<ApiResponse<?>> getBookingByCustomerId(Long customerId) {

        List<Booking> book=repo.findByCustomerId(customerId);
        book.stream().forEach(System.out::println);
        if(book!=null&&!book.isEmpty())
        {
            return ResponseEntity.ok(new ApiResponse<>("Successfully fetched ",book));

        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Not found",null));

    }

    @Override
    public List<Booking> getBookingsBySaloon(Long saloonId) {
        List<Booking> bookings = repo.findBySaloonId(saloonId);
        return bookings;

    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateBokingStatus(Long id, BookingStatus status) {
        Booking book=repo.findById(id).orElse(null);
        if(book!=null)
        {
            book.setStatus(status);
            return ResponseEntity.ok(new ApiResponse<>("Successfully updated ",book));

        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Not found",null));


    }

    @Override
    public ResponseEntity<ApiResponse<?>> getBookingById(Long id) {
        {

            Booking book=repo.findById(id).orElse(null);
            if(book!=null)
            {
                return ResponseEntity.ok(new ApiResponse<>("Successfully fetched ",book));

            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Not found",null));

        }
    }

    @Override
    public List<Booking> getBookingsByDate(Long saloonID,LocalDate date) {

        List<Booking> bookings = repo.findBySaloonId(saloonID);
        if(date==null)
        {

            return bookings;
        }
       return bookings.stream().filter(booking ->
                booking.getStartTime().toLocalDate().isEqual(date)
                        || booking.getEndTime().toLocalDate().isEqual(date))
                        .collect(Collectors.toList());

    }

    @Override
    public ResponseEntity<ApiResponse<?>> getSaloonReport(Long saloonId) {

        List<Booking> bookings = repo.findBySaloonId(saloonId);

       int totalEarnings=bookings.stream()
                .mapToInt(Booking::getTotalPrice).sum();
       Integer totalBookings= bookings.size();

       List<Booking> cancledBookings=bookings.stream().filter(
               booking -> booking.getStatus()
               .equals(BookingStatus.CANCELED))
               .collect(Collectors.toList());
       Double totalRefund=cancledBookings.stream()
               .mapToDouble(Booking::getTotalPrice).sum();

        SaloonReport report=new SaloonReport();
        report.setSaloonId(saloonId);
        report.setCancledBookings(cancledBookings.size());
        report.setTotalRefund(totalRefund);
        report.setTotalEarnings(totalEarnings);
        report.setTotalBookings(totalBookings);


        return ResponseEntity.ok(new ApiResponse<>("Saloon Report", report));
    }
}
