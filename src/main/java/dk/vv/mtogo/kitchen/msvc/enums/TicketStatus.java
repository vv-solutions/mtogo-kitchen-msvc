package dk.vv.mtogo.kitchen.msvc.enums;

public enum TicketStatus {

    PENDING(1),
    IN_PROGRESS(2),
    DONE(3),
    DENIED(4);

    private int _value;

    TicketStatus(int Value) {
        this._value = Value;
    }

    public int value() {
        return _value;
    }

    public static TicketStatus fromInt(int i) {
        for (TicketStatus b : TicketStatus.values()) {
            if (b.value() == i) { return b; }
        }
        return null;
    }
}
