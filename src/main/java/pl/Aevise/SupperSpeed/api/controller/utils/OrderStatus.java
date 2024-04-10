package pl.Aevise.SupperSpeed.api.controller.utils;

public enum OrderStatus {
    NEW(1),
    PAID(2),
    ACCEPTED(3),
    DELIVERED(4),
    REALIZED(5);

    OrderStatus(Integer status_id){

    }

}
