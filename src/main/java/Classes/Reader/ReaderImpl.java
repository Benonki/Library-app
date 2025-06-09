package Classes.Reader;

import Classes.User.User;
import Server.Packet;
import java.util.Map;

public class ReaderImpl extends User {
    public ReaderImpl(String username) {
        super(username, "Reader");
    }

    @Override
    public Packet handlePacket(Packet packet) {
        if (!(packet.data instanceof Map)) {
            return new Packet(packet.type, "Błąd formatu danych");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) packet.data;

        int userId = (int) data.get("uzytkownik_id");
        int ksiazkaId = (int) data.get("ksiazka_id");

        return switch (packet.type) {
            case "REZERWUJ_KSIAZKE" ->
                    new Packet("REZERWUJ_KSIAZKE_ODP", RezerwacjaDAO.zarezerwujKsiazke(userId, ksiazkaId) ? "OK" : "FAIL");
            case "ANULUJ_REZERWACJE" ->
                    new Packet("ANULUJ_REZERWACJE_ODP", RezerwacjaDAO.anulujRezerwacje(userId, ksiazkaId) ? "OK" : "FAIL");
            default -> new Packet(packet.type, "Nieobsługiwany typ pakietu");
        };
    }
}