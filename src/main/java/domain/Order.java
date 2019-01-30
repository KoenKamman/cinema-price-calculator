package domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Order
{
    private int orderNr;
    private boolean isStudentOrder;

    private ArrayList<MovieTicket> tickets;

    public Order(int orderNr, boolean isStudentOrder)
    {
        this.orderNr = orderNr;
        this.isStudentOrder = isStudentOrder;

        tickets = new ArrayList<>();
    }

    public int getOrderNr()
    {
        return orderNr;
    }

    void addSeatReservation(MovieTicket ticket)
    {
        tickets.add(ticket);
    }

    double calculatePrice()
    {
        double orderPrice = 0;

        // Sort array, premium tickets first
        tickets.sort((MovieTicket ticket1, MovieTicket ticket2) -> Boolean.compare(
                !ticket1.isPremiumTicket(),
                !ticket2.isPremiumTicket()
        ));

        // Loop through tickets and calculate total price
        for (int i = 0; i < tickets.size(); i++) {
            double ticketPrice = tickets.get(i).getPrice();
            double discountPercentage = 0;
            boolean isWeekday = isWeekday(tickets.get(i));

            // All second tickets are free for students or on weekdays
            if ((isStudentOrder || isWeekday) && ((i+1) % 2) == 0) continue;

            // Premium tickets cost extra
            if (tickets.get(i).isPremiumTicket()) {
                if (isStudentOrder) {
                    ticketPrice += 2;
                } else {
                    ticketPrice += 3;
                }
            }

            // Group discount applies during weekends
            if (!isWeekday && tickets.size() > 5) {
                discountPercentage += 10;
            }

            // Apply discount and add to orderPrice
            ticketPrice -= ticketPrice * (discountPercentage / 100);
            orderPrice += ticketPrice;
        }

        return orderPrice;
    }

    private boolean isWeekday(MovieTicket ticket) {
        LocalDateTime localDateTime = ticket.getMovieScreening().getDateAndTime();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        return ((dayOfWeek != DayOfWeek.SATURDAY) && (dayOfWeek != DayOfWeek.SUNDAY));
    }

    public void export(TicketExportFormat exportFormat)
    {
        // Bases on the string respresentations of the tickets (toString), write
        // the ticket to a file with naming convention Order_<orderNr>.txt of
        // Order_<orderNr>.json
    }
}
