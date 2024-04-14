package pl.Aevise.SupperSpeed.api.controller.utils;

public enum OrderStatus {
    NEW(1),
    PAID(2),
    ACCEPTED(3),
    DELIVERED(4),
    REALIZED(5),
    CANCELED(6);

    private final int statusId;

    OrderStatus(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }
}
