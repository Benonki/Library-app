package Classes.Coordinator;

import Classes.Coordinator.Util.BookOrder;
import Server.DatabaseConnection;
import Server.Packet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    private int orderID;
    private int orderMaker;
    private Delivery deliveryService;
    private int amount;
    private Date realizationDate;
    private Date creationDate;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    private List<BookOrder> booksToOrder;
    private String status;

    public void setOrderMaker(int orderMaker) {
        this.orderMaker = orderMaker;
    }

    public void setDeliveryService(Delivery deliveryService) {
        this.deliveryService = deliveryService;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setRealizationDate(Date realizationDate) {
        this.realizationDate = realizationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setBooksToOrder(List<BookOrder> booksToOrder) {
        this.booksToOrder = booksToOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order(){

    }

    public Order(int orderMaker, Delivery deliveryService, int amount, Date realizationDate, Date creationDate, List<BookOrder> booksToOrder, String status) {
        this.orderMaker = orderMaker;
        this.deliveryService = deliveryService;
        this.amount = amount;
        this.realizationDate = realizationDate;
        this.creationDate = creationDate;
        this.booksToOrder = new ArrayList<>(booksToOrder);
        this.status = status;
    }

    public Order(Delivery deliveryService, int amount, Date realizationDate, Date creationDate, String status,int orderID) {
        this.deliveryService = deliveryService;
        this.amount = amount;
        this.realizationDate = realizationDate;
        this.creationDate = creationDate;
        this.status = status;
        this.orderID = orderID;
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
            String sqlQuery = "INSERT INTO Zamowienie (Uzytkownik_ID, Dostawca_ID, Ilosc, Termin_Realizacji, Status) VALUES (?, ?, ?, ?, ?)";
            try(PreparedStatement statement = conn.prepareStatement(sqlQuery, new String[]{"Zamowienie_ID"} )){
                statement.setInt(1, orderInfo.getOrderMaker());
                statement.setInt(2, 1);
                statement.setInt(3, orderInfo.getAmount());

                java.sql.Date sqlDate = new java.sql.Date(orderInfo.getRealizationDate().getTime());
                statement.setDate(4, sqlDate);
                statement.setString(5,orderInfo.getStatus());

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

    public static Packet getOrderInfo() {
        List<Order> orders = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlQuery = "SELECT z.zamowienie_id, d.nazwa, z.ilosc, z.data_zamowienia, z.termin_realizacji, z.status FROM Zamowienie z JOIN Dostawca d ON z.dostawca_id = d.dostawca_id";
            try(PreparedStatement statement = conn.prepareStatement(sqlQuery)){
                try(ResultSet rs = statement.executeQuery()){
                    while (rs.next()){
                        int orderID = rs.getInt("zamowienie_id");
                        String deliveryName = rs.getString("nazwa");
                        int amountOfBooks = rs.getInt("ilosc");
                        Date creationDate = rs.getDate("data_zamowienia");
                        Date rezlizationDate = rs.getDate("termin_realizacji");
                        String status = rs.getString("status");

                        orders.add(new Order(new Delivery(deliveryName),amountOfBooks,rezlizationDate,creationDate,status,orderID));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Packet("GetOrderInformation","Error");
        }
        System.out.println("GET ORDER INFO CALLED");
        Packet packet = Packet.withOrdersInfo("GetOrderInformation","Success",orders);
        return packet;
    }

    public static Packet deleteOrder(int zamowienieId) {
        String deleteDostawaSQL = "DELETE FROM Dostawa WHERE Zamowienie_Ksiazka_ID IN (SELECT Zamowienie_Ksiazka_ID FROM Zamowienie_Ksiazka WHERE Zamowienie_ID = ?)";
        String deleteZamowienieKsiazkaSQL = "DELETE FROM Zamowienie_Ksiazka WHERE Zamowienie_ID = ?";
        String deleteZamowienieSQL = "DELETE FROM Zamowienie WHERE Zamowienie_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(deleteDostawaSQL);
                 PreparedStatement ps2 = conn.prepareStatement(deleteZamowienieKsiazkaSQL);
                 PreparedStatement ps3 = conn.prepareStatement(deleteZamowienieSQL)) {

                ps1.setInt(1, zamowienieId);
                ps1.executeUpdate();

                ps2.setInt(1, zamowienieId);
                ps2.executeUpdate();

                ps3.setInt(1, zamowienieId);
                int affectedRows = ps3.executeUpdate();

                conn.commit();

                if (affectedRows > 0) {
                    return new Packet("DeleteOrder", "Success");
                } else {
                    return new Packet("DeleteOrder", "Order was not found");
                }
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return new Packet("DeleteOrder", "Error");
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Packet("DeleteOrder", "Error");
        }
    }
}
