package Classes.Coordinator;

import Classes.Coordinator.Util.BookOrder;
import Server.DatabaseConnection;
import Server.Packet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    private int orderMaker;
    private Delivery deliveryService;
    private int amount;
    private Date realizationDate;
    private List<BookOrder> booksToOrder;

    public Order(int orderMaker, Delivery deliveryService, int amount, Date realizationDate, List<BookOrder> booksToOrder) {
        this.orderMaker = orderMaker;
        this.deliveryService = deliveryService;
        this.amount = amount;
        this.realizationDate = realizationDate;
        this.booksToOrder = new ArrayList<>(booksToOrder);;
    }

    public Date getRealizationDate() {
        return realizationDate;
    }

    public List<BookOrder> getBooksToOrder() {
        return booksToOrder;
    }

    public int getOrderMaker() {
        return orderMaker;
    }

    public Delivery getDeliveryService() {
        return deliveryService;
    }

    public int getAmount() {
        return amount;
    }


    private static int insertNewOrderToDB(Order orderInfo){
        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlQuery = "INSERT INTO Zamowienie (Uzytkownik_ID, Dostawca_ID, Ilosc, Termin_Realizacji) VALUES (?, ?, ?, ?)";
            try(PreparedStatement statement = conn.prepareStatement(sqlQuery, new String[]{"Zamowienie_ID"} )){
                statement.setInt(1, orderInfo.getOrderMaker());
                statement.setInt(2, 1);
                statement.setInt(3, orderInfo.getAmount());

                java.sql.Date sqlDate = new java.sql.Date(orderInfo.getRealizationDate().getTime());
                statement.setDate(4, sqlDate);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    try(var generatedKeys = statement.getGeneratedKeys()){
                        if(generatedKeys.next()){
                            System.out.println("MAM KLUCZ GŁÓWNY: " + generatedKeys.getInt(1));
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("NIE MA KLUCZA GŁÓWNEGO");
        return -1;
    }

    private static boolean insertDataToOrder_BookTable(Order orderInfo, int orderID){

        System.out.println("INSERTING INTO ORDER_BOOK");
        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlQuery = "INSERT INTO Zamowienie_Ksiazka (Zamowienie_ID, Ksiazka_ID, Ilosc) VALUES (?, ?, ?)";
            try(PreparedStatement statement = conn.prepareStatement(sqlQuery)){
                int rowsAffected = 0;


                System.out.println("LICZBA RÓŻNYCH EGZEMPLARZY: " + orderInfo.booksToOrder.size());
                for(BookOrder book : orderInfo.booksToOrder){
                    statement.setInt(1, orderID);
                    statement.setInt(2, 1);
                    statement.setInt(3, book.getAmount());

                    rowsAffected = statement.executeUpdate();

                    if (rowsAffected <= 0) {
                        return false;
                    }
                }
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Packet createOrder(Order orderInfo) {
        int orderID = Order.insertNewOrderToDB(orderInfo);

        if (orderID != -1) {

            System.out.println("WYWOŁYWANIE FUNKCJI BOOK_ORDER INSERT");
            boolean orderBookInsertFlag = Order.insertDataToOrder_BookTable(orderInfo, orderID);

            if (orderBookInsertFlag) {
                return new Packet("CreateNewOrder", "Success");
            } else {
                return new Packet("CreateNewOrder", "Failed");
            }
        } else {
            return new Packet("CreateNewOrder", "Failed");
        }
    }
}
