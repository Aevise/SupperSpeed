package pl.Aevise.SupperSpeed.util;

import lombok.experimental.UtilityClass;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

@UtilityClass
public class Constants {
    public final static String testPassword = "$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42";
    public final static String WARSZAWA = "Warszawa";
    public final static String LUBLIN = "Lublin";
    public final static String CHELM = "Chelm";
    public final static String POLAND = "Poland";
    public final static String TEST_CLIENT_EMAIL_1 = "test4@gmail.com";
    public final static String TEST_CLIENT_EMAIL_FLYWAY_1 = "user4@user.com";
    public final static String TEST_RESTAURANT_EMAIL_1 = "test1@gmail.com";


    public final static Map<String, String> CUISINES = ImmutableMap.of(
            "Italian", "Italian",
            "Polish", "Polish",
            "Spanish", "Spanish"
    );

    public final static Map<String, String> DISH_CATEGORY = ImmutableMap.of(
            "Meat", "Meat",
            "Vegan", "Vegan"
    );
}
