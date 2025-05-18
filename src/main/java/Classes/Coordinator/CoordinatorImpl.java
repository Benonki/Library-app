package Classes.Coordinator;

import Classes.User.User;
import Server.Packet;

public class CoordinatorImpl extends User {

    public CoordinatorImpl(String username){
        super(username,"Coordinator");
    }

    @Override
    public Packet handlePacket(Packet packet) {
        switch (packet.type){
            case "GetInventoryStatus":
                return Warehouse.getInventoryStatus();
            case"GetDeliveryInformation":
                return Delivery.getDeliveryInfo();
            case "CreateNewOrder":
                return Order.createOrder(packet.orderInfo);
            case "GetOrderInformation":
                return Order.getOrderInfo();
            case "DeleteOrder":
                return Order.deleteOrder(packet.orderInfo.getOrderID());
            default:
                return new Packet(packet.type,"Unsupported in Coordinator");
        }
    }


}


