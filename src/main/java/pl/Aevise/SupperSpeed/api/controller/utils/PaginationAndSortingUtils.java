package pl.Aevise.SupperSpeed.api.controller.utils;

public enum PaginationAndSortingUtils {
    ASC("asc"),
    DESC("desc");
    private final String direction;

    PaginationAndSortingUtils(String direction) {
        this.direction = direction;
    }

    public String getSortingDirection(){
        return direction;
    }
}
