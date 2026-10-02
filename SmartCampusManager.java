import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Smart Campus Resource Manager
 * A Java OOP portfolio project demonstrating:
 * Encapsulation, Inheritance, Polymorphism, Interfaces,
 * Collections, Validation, Exception Handling and
 * Booking Conflict Detection.
 */

interface Bookable {
    boolean isAvailable(LocalDateTime start, LocalDateTime end);
}

abstract class Resource implements Bookable {

    private final int id;
    private final String name;
    private final int capacity;

    protected Resource(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public abstract String getType();

    @Override
    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                "[%d] %-22s | Type: %-10s | Capacity: %d",
                id, name, getType(), capacity
        );
    }
}

class Room extends Resource {

    private final boolean projector;

    public Room(int id, String name, int capacity, boolean projector) {
        super(id, name, capacity);
        this.projector = projector;
    }

    public boolean hasProjector() {
        return projector;
    }

    @Override
    public String getType() {
        return "ROOM";
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" | Projector: %s", projector ? "Yes" : "No");
    }
}

class Lab extends Resource {

    private final int computers;

    public Lab(int id, String name, int capacity, int computers) {
        super(id, name, capacity);
        this.computers = computers;
    }

    @Override
    public String getType() {
        return "LAB";
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" | Computers: %d", computers);
    }
}

class Equipment extends Resource {

    private final String category;

    public Equipment(int id, String name, int capacity, String category) {
        super(id, name, capacity);
        this.category = category;
    }

    @Override
    public String getType() {
        return "EQUIPMENT";
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" | Category: %s", category);
    }
}

class User {

    private final int id;
    private final String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class Booking {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int bookingId;
    private final User user;
    private final Resource resource;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Booking(
            int bookingId,
            User user,
            Resource resource,
            LocalDateTime start,
            LocalDateTime end) {

        this.bookingId = bookingId;
        this.user = user;
        this.resource = resource;
        this.start = start;
        this.end = end;
    }

    public int getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Resource getResource() {
        return resource;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean overlaps(LocalDateTime requestedStart,
                            LocalDateTime requestedEnd) {

        return requestedStart.isBefore(end)
                && requestedEnd.isAfter(start);
    }

    @Override
    public String toString() {
        return String.format(
                "Booking #%d | User: %s | Resource: %s | %s - %s",
                bookingId,
                user.getName(),
                resource.getName(),
                start.format(FORMAT),
                end.format(FORMAT)
        );
    }
}

class BookingConflictException extends Exception {

    public BookingConflictException(String message) {
        super(message);
    }
}

public class SmartCampusManager {

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Resource> resources = new ArrayList<>();
    private static final List<User> users = new ArrayList<>();
    private static final List<Booking> bookings = new ArrayList<>();

    private static int nextBookingId = 1001;

    public static void main(String[] args) {

        seedData();

        boolean running = true;

        printHeader();

        while (running) {

            showMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    displayResources();
                    break;

                case 2:
                    searchResources();
                    break;

                case 3:
                    createBooking();
                    break;

                case 4:
                    displayBookings();
                    break;

                case 5:
                    cancelBooking();
                    break;

                case 6:
                    showStatistics();
                    break;

                case 7:
                    addResource();
                    break;

                case 8:
                    running = false;
                    System.out.println("\nThank you for using Smart Campus Resource Manager.");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please select 1-8.");
            }
        }

        scanner.close();
    }

    private static void printHeader() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("           SMART CAMPUS RESOURCE MANAGER");
        System.out.println("              Java OOP Portfolio Project");
        System.out.println("======================================================");
    }

    private static void showMenu() {

        System.out.println("\n-------------------- MAIN MENU --------------------");
        System.out.println("1. View All Resources");
        System.out.println("2. Search Resources");
        System.out.println("3. Book a Resource");
        System.out.println("4. View All Bookings");
        System.out.println("5. Cancel Booking");
        System.out.println("6. View System Statistics");
        System.out.println("7. Add Resource");
        System.out.println("8. Exit");
        System.out.println("---------------------------------------------------");
    }

    private static void seedData() {

        resources.add(new Room(101, "Seminar Room A", 40, true));
        resources.add(new Room(102, "Conference Room B", 20, true));
        resources.add(new Lab(201, "Programming Lab", 35, 30));
        resources.add(new Lab(202, "Data Science Lab", 25, 22));
        resources.add(new Equipment(301, "Projector P1", 1, "Presentation"));

        users.add(new User(1, "Saima"));
        users.add(new User(2, "Student User"));
    }

    private static void displayResources() {

        System.out.println("\n================ AVAILABLE RESOURCES ================");

        if (resources.isEmpty()) {
            System.out.println("No resources available.");
            return;
        }

        for (Resource resource : resources) {
            System.out.println(resource);
        }
    }

    private static void searchResources() {

        System.out.println("\n================ RESOURCE SEARCH =================");

        String keyword = readText("Search by name/type: ")
                .toLowerCase();

        boolean found = false;

        for (Resource resource : resources) {

            if (resource.getName().toLowerCase().contains(keyword)
                    || resource.getType().toLowerCase().contains(keyword)) {

                System.out.println(resource);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching resources found.");
        }
    }

    private static void createBooking() {

        System.out.println("\n================ CREATE BOOKING =================");

        displayResources();

        int resourceId = readInt("\nEnter Resource ID: ");

        Resource resource = findResource(resourceId);

        if (resource == null) {
            System.out.println("Resource not found.");
            return;
        }

        int userId = readInt("Enter User ID (1-2): ");

        User user = findUser(userId);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        LocalDateTime start = readDateTime(
                "Start time (yyyy-MM-dd HH:mm): ");

        LocalDateTime end = readDateTime(
                "End time   (yyyy-MM-dd HH:mm): ");

        if (!end.isAfter(start)) {
            System.out.println("End time must be after start time.");
            return;
        }

        try {

            validateBooking(resource, start, end);

            Booking booking = new Booking(
                    nextBookingId++,
                    user,
                    resource,
                    start,
                    end
            );

            bookings.add(booking);

            System.out.println("\nBooking created successfully!");
            System.out.println(booking);

        } catch (BookingConflictException e) {

            System.out.println("\nBOOKING REJECTED");
            System.out.println(e.getMessage());
        }
    }

    private static void validateBooking(
            Resource resource,
            LocalDateTime start,
            LocalDateTime end)
            throws BookingConflictException {

        for (Booking booking : bookings) {

            if (booking.getResource().getId() == resource.getId()
                    && booking.overlaps(start, end)) {

                throw new BookingConflictException(
                        "This resource is already booked during the requested time."
                );
            }
        }
    }

    private static void displayBookings() {

        System.out.println("\n================ CURRENT BOOKINGS ================");

        if (bookings.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }

        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }

    private static void cancelBooking() {

        System.out.println("\n================ CANCEL BOOKING ================");

        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        displayBookings();

        int bookingId = readInt("\nEnter Booking ID: ");

        Booking booking = findBooking(bookingId);

        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }

        String confirmation =
                readText("Confirm cancellation? (yes/no): ");

        if (confirmation.equalsIgnoreCase("yes")) {

            bookings.remove(booking);

            System.out.println("Booking cancelled successfully.");

        } else {

            System.out.println("Cancellation aborted.");
        }
    }

    private static void showStatistics() {

        System.out.println("\n================ SYSTEM STATISTICS ================");

        System.out.println("Total Resources : " + resources.size());
        System.out.println("Total Users     : " + users.size());
        System.out.println("Active Bookings : " + bookings.size());

        long rooms = resources.stream()
                .filter(r -> r instanceof Room)
                .count();

        long labs = resources.stream()
                .filter(r -> r instanceof Lab)
                .count();

        long equipment = resources.stream()
                .filter(r -> r instanceof Equipment)
                .count();

        System.out.println("Rooms           : " + rooms);
        System.out.println("Labs            : " + labs);
        System.out.println("Equipment       : " + equipment);
    }

    private static void addResource() {

        System.out.println("\n================ ADD RESOURCE ================");

        int id = readInt("Resource ID: ");

        if (findResource(id) != null) {
            System.out.println("Resource ID already exists.");
            return;
        }

        String name = readText("Resource Name: ");
        int capacity = readInt("Capacity: ");

        if (capacity <= 0) {
            System.out.println("Capacity must be positive.");
            return;
        }

        System.out.println("\nResource Type:");
        System.out.println("1. Room");
        System.out.println("2. Lab");
        System.out.println("3. Equipment");

        int type = readInt("Select type: ");

        switch (type) {

            case 1:

                String projector =
                        readText("Has projector? (yes/no): ");

                boolean hasProjector =
                        projector.equalsIgnoreCase("yes");

                resources.add(
                        new Room(
                                id,
                                name,
                                capacity,
                                hasProjector
                        )
                );

                System.out.println("Room added successfully.");
                break;

            case 2:

                int computers =
                        readInt("Number of computers: ");

                resources.add(
                        new Lab(
                                id,
                                name,
                                capacity,
                                computers
                        )
                );

                System.out.println("Lab added successfully.");
                break;

            case 3:

                String category =
                        readText("Equipment category: ");

                resources.add(
                        new Equipment(
                                id,
                                name,
                                capacity,
                                category
                        )
                );

                System.out.println("Equipment added successfully.");
                break;

            default:
                System.out.println("Invalid resource type.");
        }
    }

    private static Resource findResource(int id) {

        for (Resource resource : resources) {

            if (resource.getId() == id) {
                return resource;
            }
        }

        return null;
    }

    private static User findUser(int id) {

        for (User user : users) {

            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    private static Booking findBooking(int id) {

        for (Booking booking : bookings) {

            if (booking.getBookingId() == id) {
                return booking;
            }
        }

        return null;
    }

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {

                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a number."
                );
            }
        }
    }

    private static String readText(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                    "Input cannot be empty."
            );
        }
    }

    private static LocalDateTime readDateTime(String message) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm"
                );

        while (true) {

            System.out.print(message);

            try {

                return LocalDateTime.parse(
                        scanner.nextLine().trim(),
                        formatter
                );

            } catch (Exception e) {

                System.out.println(
                        "Invalid date/time format. " +
                        "Use yyyy-MM-dd HH:mm"
                );
            }
        }
    }
}
