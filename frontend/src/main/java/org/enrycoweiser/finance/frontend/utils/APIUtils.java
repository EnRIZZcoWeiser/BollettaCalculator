package org.enrycoweiser.finance.frontend.utils;

public class APIUtils {
    public static final String IP = "http://localhost";
    public static final String PORT = "8081";
    public static final String PATH = "/api";

    public static final String BASE_URL = IP + ":" + PORT + PATH;

    public static final String ENTITY = "/entity";

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String REFRESH = "/filter";

    public static final String INCOME = "/event";
    public static final String EXPENSE = "/tournament";

    public static final String API_ENTITY_INCOME_SAVE = BASE_URL + ENTITY + INCOME + SAVE;
    public static final String API_ENTITY_INCOME_DELETE = BASE_URL + ENTITY + INCOME + DELETE;
    public static final String API_ENTITY_INCOME_REFRESH = BASE_URL + ENTITY + INCOME + REFRESH;
    public static final String API_ENTITY_EXPENSE_SAVE = BASE_URL + ENTITY + EXPENSE + SAVE;
    public static final String API_ENTITY_EXPENSE_DELETE = BASE_URL + ENTITY + EXPENSE + DELETE;
    public static final String API_ENTITY_EXPENSE_REFRESH = BASE_URL + ENTITY + EXPENSE + REFRESH;
}
