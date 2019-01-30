package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private double pricePerSeat = 10;
    private double studentPremium = 2;
    private double normalPremium = 3;
    private Movie movie = new Movie("Test Film");
    private MovieScreening movieScreening;
    private Order order;

    private void initOrder(boolean studentOrder, boolean weekend) {
        LocalDateTime localDateTime;
        if (weekend) {
            localDateTime = LocalDateTime.of(2019, 2, 9, 12, 30);
        } else {
            localDateTime = LocalDateTime.of(2019, 2, 6, 12, 30);
        }
        movieScreening = new MovieScreening(movie, localDateTime, pricePerSeat);
        order =  new Order(1, studentOrder);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void FourPeople_AllStudents_Saturday() {
        initOrder(true, true);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*2, price);
    }

    @Test
    void ThreePeople_NoStudents_Wednesday() {
        initOrder(false, false);

        for (int i = 0; i < 3; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*2, price);
    }

    @Test
    void SixPeople_NoStudents_Saturday() {
        initOrder(false, true);

        for (int i = 0; i < 6; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals((pricePerSeat*6)*0.9, price);
    }

    @Test
    void SixPeople_AllStudents_Saturday_TwoPremium() {
        initOrder(true, true);

        MovieTicket ticket = new MovieTicket(movieScreening, false, 1, 1);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, false, 1, 2);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, true, 1, 3);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, false, 1, 4);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, false, 1, 5);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, true, 1, 6);
        order.addSeatReservation(ticket);

        double price = order.calculatePrice();
        assertEquals(32*0.9, price);
    }

    @Test
    void EightPeople_NoStudents_Saturday_FourPremium() {
        initOrder(false, true);

        MovieTicket ticket = new MovieTicket(movieScreening, true, 1, 1);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, false, 1, 2);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, true, 1, 3);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, false, 1, 4);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, false, 1, 5);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, false, 1, 6);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, true, 1, 7);
        order.addSeatReservation(ticket);

        ticket = new MovieTicket(movieScreening, true, 1, 8);
        order.addSeatReservation(ticket);

        double price = order.calculatePrice();

        assertEquals((pricePerSeat * 8 + normalPremium * 4) * 0.9, price);
    }

    @Test
    void GroupDiscount_SixPeopleWeekend() {
        initOrder(false, true);

        for (int i = 0; i < 6; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*6*0.9, price);
    }

    @Test
    void GroupDiscount_SixPeopleWeekday() {
        initOrder(false, false);

        for (int i = 0; i < 6; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*3, price);
    }

    @Test
    void GroupDiscount_FivePeopleWeekend() {
        initOrder(false, true);

        for (int i = 0; i < 5; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*5, price);
    }

    @Test
    void GroupDiscount_FivePeopleWeekday() {
        initOrder(false, false);

        for (int i = 0; i < 5; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*3, price);
    }

    @Test
    void Premium_PremiumStudents() {
        initOrder(true, true);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, true, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals((pricePerSeat+studentPremium)*2, price);
    }

    @Test
    void Premium_NormalStudents() {
        initOrder(true, true);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*2, price);
    }

    @Test
    void Premium_PremiumNoStudents() {
        initOrder(false, true);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, true, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals((pricePerSeat+normalPremium)*4, price);
    }

    @Test
    void Premium_NormalNoStudents() {
        initOrder(false, true);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*4, price);
    }

    @Test
    void SecondTicketsFree_NoStudentWeekend() {
        initOrder(false, true);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*4, price);
    }

    @Test
    void SecondTicketsFree_NoStudentWeekday() {
        initOrder(false, false);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*2, price);
    }

    @Test
    void SecondTicketsFree_StudentWeekend() {
        initOrder(true, true);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*2, price);
    }

    @Test
    void SecondTicketsFree_StudentWeekday() {
        initOrder(true, false);

        for (int i = 0; i < 4; i++) {
            order.addSeatReservation(new MovieTicket(movieScreening, false, 1, i));
        }

        double price = order.calculatePrice();
        assertEquals(pricePerSeat*2, price);
    }
}