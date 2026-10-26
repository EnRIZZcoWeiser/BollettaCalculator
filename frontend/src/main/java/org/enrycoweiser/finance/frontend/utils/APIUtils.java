package org.enrycoweiser.finance.frontend.utils;

public class APIUtils {
    public static final String IP = "http://localhost";
    public static final String PORT = "8080";
    public static final String PATH = "/api";

    public static final String BASE_URL = IP + ":" + PORT + PATH;

    public static final String ENTITY = "/entity";
    public static final String LIST = "/list";

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String REFRESH = "/filter";

    public static final String INCOME = "/income";
    public static final String EXPENSE = "/expense";
    public static final String PAYMENT_METHOD = "/paymentMethod";
    public static final String ASSET = "/asset";
    public static final String NET_WORTH = "/netWorth";
    public static final String NET_WORTH_ENTRY = "/netWorthEntry";

    public static final String API_ENTITY_INCOME_SAVE = BASE_URL + ENTITY + INCOME + SAVE;
    public static final String API_ENTITY_INCOME_DELETE = BASE_URL + ENTITY + INCOME + DELETE;
    public static final String API_ENTITY_INCOME_REFRESH = BASE_URL + ENTITY + INCOME + REFRESH;
    public static final String API_ENTITY_EXPENSE_SAVE = BASE_URL + ENTITY + EXPENSE + SAVE;
    public static final String API_ENTITY_EXPENSE_DELETE = BASE_URL + ENTITY + EXPENSE + DELETE;
    public static final String API_ENTITY_EXPENSE_REFRESH = BASE_URL + ENTITY + EXPENSE + REFRESH;
    public static final String API_ENTITY_PAYMENT_METHOD_SAVE = BASE_URL + ENTITY + PAYMENT_METHOD + SAVE;
    public static final String API_ENTITY_PAYMENT_METHOD_DELETE = BASE_URL + ENTITY + PAYMENT_METHOD + DELETE;
    public static final String API_ENTITY_PAYMENT_METHOD_REFRESH = BASE_URL + ENTITY + PAYMENT_METHOD + REFRESH;
    public static final String API_ENTITY_ASSET_SAVE = BASE_URL + ENTITY + ASSET + SAVE;
    public static final String API_ENTITY_ASSET_DELETE = BASE_URL + ENTITY + ASSET + DELETE;
    public static final String API_ENTITY_ASSET_REFRESH = BASE_URL + ENTITY + ASSET + REFRESH;
    public static final String API_ENTITY_NET_WORTH_SAVE = BASE_URL + ENTITY + NET_WORTH + SAVE;
    public static final String API_ENTITY_NET_WORTH_DELETE = BASE_URL + ENTITY + NET_WORTH + DELETE;
    public static final String API_ENTITY_NET_WORTH_REFRESH = BASE_URL + ENTITY + NET_WORTH + REFRESH;
    public static final String API_ENTITY_NET_WORTH_ENTRY_SAVE = BASE_URL + ENTITY + NET_WORTH_ENTRY + SAVE;
    public static final String API_ENTITY_NET_WORTH_ENTRY_DELETE = BASE_URL + ENTITY + NET_WORTH_ENTRY + DELETE;
    public static final String API_ENTITY_NET_WORTH_ENTRY_REFRESH = BASE_URL + ENTITY + NET_WORTH_ENTRY + REFRESH;

    public static final String API_LIST_PAYMENT_METHOD = BASE_URL + LIST + PAYMENT_METHOD;
    public static final String API_LIST_ASSET = BASE_URL + LIST + ASSET;
}
