package com.surya.Controller;

import com.surya.Dto.BookingRequest;
import com.surya.Dto.SaloonDto;
import com.surya.Dto.ServiceOfferingDto;
import com.surya.Dto.UserDto;
import com.surya.Model.Booking;
import com.surya.Response.ApiResponse;
import com.surya.Service.BookingService;
import com.surya.domain.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService serv;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<?>> save(@RequestBody BookingRequest booking,
                                               @RequestParam Long saloonId) throws Exception {
        UserDto use= new UserDto();
        use.setId(1L);

        SaloonDto sal = new SaloonDto();
        sal.setId(saloonId);
        sal.setOpenTime(LocalTime.of(10,0,1));
        sal.setCloseTime(LocalTime.of(20,0,0));

        Set<ServiceOfferingDto> list = new HashSet<>();
        ServiceOfferingDto servic = new ServiceOfferingDto();
        servic.setId(1L);
        servic.setPrice(400);
        servic.setName("hair cut");
        servic.setDuration(30);
        list.add(servic);


        return serv.createBooking(booking,use,sal,list);
    }


    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<?>> getByCustomerId()
    {
        return serv.getBookingByCustomerId(1L);
    }
    @GetMapping("/saloon")
    public List<Booking> getBySaloonId()
    {
        return serv.getBookingsBySaloon(1L);
    }


    @GetMapping("/booking/id/{id}")
    public ResponseEntity<ApiResponse<?>> getByBookingId(@PathVariable Long id)
    {

        return serv.getBookingById(id);
    }

    @PutMapping("/id/{id}/status")
    public ResponseEntity<ApiResponse<?>> UpdateBooking(@PathVariable Long id, @RequestParam BookingStatus status)
    {
       return serv.updateBokingStatus(id,status);
    }
    @GetMapping("/slots/saloonid/{id}/date/{date}")
    public List<Booking>  getBookingByDate(@PathVariable Long id, @PathVariable LocalDate date)
    {
      return   serv.getBookingsByDate(id,date);
    }


    @GetMapping("/report/saloonId/{id}")
    public ResponseEntity<ApiResponse<?>> saloonReport(@PathVariable Long id)
    {
       return serv.getSaloonReport(id);
    }



}
