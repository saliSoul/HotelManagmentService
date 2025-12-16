package model;
import java.time.LocalDate;
import model.RoomType;
import model.Room;
import model.User;
public class Booking {

    // Snapshots :
    // values stored to keep booking history unchanged even if room or user data changes later
    // which defintely will
    private final int roomNumber;
    private final RoomType roomType;
    private final int pricePerNight;
    private final int userId;

    private final LocalDate checkIn;
    private final LocalDate checkOut;

    private final int nights;
    private final int totalPrice;

    public int getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public int getPricePerNight() { return pricePerNight; }
    public int getUserId() { return userId; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public int getNights() { return nights; }


    private Booking(Builder builder) {
        this.roomNumber = builder.room.getRoomNumber();
        this.roomType = builder.room.getRoomType();
        this.pricePerNight = builder.room.getPricePerNight();
        this.userId = builder.user.getId();

        this.checkIn = builder.checkIn;
        this.checkOut = builder.checkOut;
        this.nights = builder.nights;
        this.totalPrice = builder.nights * this.pricePerNight;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    // BUILDER
    public static class Builder {
        private Room room;
        private User user;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private int nights;

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder room(Room room) {
            this.room = room;
            return this;
        }

        public Builder dates(LocalDate checkIn, LocalDate checkOut) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.nights = (int) java.time.temporal.ChronoUnit.DAYS
                    .between(checkIn, checkOut);
            return this;
        }

        public Booking build() {
            if (room == null || user == null || checkIn == null || checkOut == null) {
                throw new IllegalStateException("Missing booking information");
            }
            if (nights <= 0) {
                throw new IllegalArgumentException("Invalid booking dates");
            }
            return new Booking(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
    @Override
    public String toString() {
        return "Booking{" +
                "userId=" + userId +
                ", roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", pricePerNight=" + pricePerNight +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", nights=" + nights +
                ", totalPrice=" + totalPrice +
                '}';
    }

}
