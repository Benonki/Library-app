package Classes.Reader;

import Classes.User.User;
import Server.Packet;

public class ReaderImpl extends User {
    public ReaderImpl(String username) {
        super(username,"Reader");
    }

    @Override
    public Packet handlePacket(Packet packet) {
    }


}