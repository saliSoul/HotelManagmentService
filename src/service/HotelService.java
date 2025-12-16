package service;

import model.Room;
import model.User;
import model.Booking;
import model.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;




public class HotelService {

    private static final HotelService INSTANCE = new HotelService();

    private final List<User> users = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    private HotelService() {}

    public static HotelService getInstance() {
        return INSTANCE;
    }

    // ---------- USER ----------
    public void setUser(int userId, int balance) {
        User user = findUser(userId);
        if (user == null) {
            users.add(new User(userId, balance));
        } else {
            user.setBalance(balance);
        }
    }

    // ---------- ROOM ----------
    public void setRoom(int roomNumber, RoomType type, int pricePerNight) {
        Room room = findRoom(roomNumber);
        if (room == null) {
            rooms.add(new Room(roomNumber, type, pricePerNight));
        } else {
        // Past booking remain unchanged
            rooms.remove(room);
            rooms.add(new Room(roomNumber, type, pricePerNight));
        }
    }

    // ---------- BOOK ----------
    public void bookRoom(
            int userId,
            int roomNumber,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        User user = findUser(userId);
        Room room = findRoom(roomNumber);

        if (user == null || room == null) {
            return;
        }

        int nights = (int) java.time.temporal.ChronoUnit.DAYS
                .between(checkIn, checkOut);

        if (nights <= 0) {
            return;
        }

        int totalPrice = nights * room.getPricePerNight();

        if (user.getBalance() < totalPrice) {
            return;
        }

        if (!isRoomAvailable(roomNumber, checkIn, checkOut)) {
            return;
        }

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .dates(checkIn, checkOut)
                .build();

        bookings.add(booking);
        user.setBalance(user.getBalance() - totalPrice);
    }

    // ---------- PRINTING ----------
    public void printAll() {
        System.out.println();
        System.out.println("================================");
        System.out.println("HOTEL STATE");
        System.out.println("================================");

        // ROOMS
        System.out.println();
        System.out.println("Rooms (latest -> oldest)");
        if (rooms.isEmpty()) {
            System.out.println("  No rooms available");
        } else {
            for (int i = rooms.size() - 1; i >= 0; i--) {
                Room r = rooms.get(i);
                System.out.printf(
                        "  Room #%d | %-8s | %d / night%n",
                        r.getRoomNumber(),
                        r.getRoomType(),
                        r.getPricePerNight()
                );
            }
        }

        // BOOKINGS
        System.out.println();
        System.out.println("Bookings (latest -> oldest)");
        if (bookings.isEmpty()) {
            System.out.println("  No bookings");
        } else {
            for (int i = bookings.size() - 1; i >= 0; i--) {
                Booking b = bookings.get(i);
                System.out.printf(
                        "  User %d | Room #%d (%s) | %s -> %s | %d nights | Total: %d%n",
                        b.getUserId(),
                        b.getRoomNumber(),
                        b.getRoomType(),
                        b.getCheckIn(),
                        b.getCheckOut(),
                        b.getNights(),
                        b.getTotalPrice()
                );
            }
        }

        System.out.println("================================");
        System.out.println();
    }
    public void printAllUsers() {
        System.out.println();
        System.out.println("================================");
        System.out.println("USERS (latest -> oldest)");
        System.out.println("================================");

        if (users.isEmpty()) {
            System.out.println("  No users");
        } else {
            for (int i = users.size() - 1; i >= 0; i--) {
                User u = users.get(i);
                System.out.printf(
                        "  User %d | Balance: %d%n",
                        u.getId(),
                        u.getBalance()
                );
            }
        }

        System.out.println("================================");
        System.out.println();
    }






    // ---------- HELPERS ----------
    private User findUser(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Room findRoom(int number) {
        return rooms.stream()
                .filter(r -> r.getRoomNumber() == number)
                .findFirst()
                .orElse(null);
    }

    private boolean isRoomAvailable(int roomNumber, LocalDate in, LocalDate out) {
        return bookings.stream().noneMatch(b ->
                b.getRoomNumber() == roomNumber &&
                        !(out.isBefore(b.getCheckIn()) || in.isAfter(b.getCheckOut()))
        );
    }
}
