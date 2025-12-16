import service.HotelService;
import model.RoomType;
import java.time.LocalDate;


public class Main {

    public static void main(String[] args) {

        HotelService service = HotelService.getInstance();


        service.setUser(1, 5000);
        service.setUser(2, 10000);


        service.setRoom(1, RoomType.STANDARD, 1000);
        service.setRoom(2, RoomType.JUNIOR, 2000);
        service.setRoom(3, RoomType.MASTER, 3000);


        tryBooking(service, 1, 2, LocalDate.of(2026, 6, 30), LocalDate.of(2026, 7, 7));
        tryBooking(service, 1, 2, LocalDate.of(2026, 7, 7), LocalDate.of(2026, 6, 30)); // invalid :)
        tryBooking(service, 1, 1, LocalDate.of(2026, 7, 7), LocalDate.of(2026, 7, 8)); //
        tryBooking(service, 2, 1, LocalDate.of(2026, 7, 7), LocalDate.of(2026, 7, 9)); //
        tryBooking(service, 2, 3, LocalDate.of(2026, 7, 7), LocalDate.of(2026, 7, 8)); //


        service.setRoom(1, RoomType.MASTER, 10000);



        service.printAll();

        System.out.println();


        service.printAllUsers();
    }


    private static void tryBooking(HotelService service, int userId, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        try {
            service.bookRoom(userId, roomNumber, checkIn, checkOut);
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}



