package org.enrycoweiser.finance.shared.utils;

import java.util.List;

public class StaticUtils {
    public static final String EXPENSE_FOOD = "FOOD & BEVERAGE";
    public static final String EXPENSE_TRAVEL = "TRAVEL";
    public static final String EXPENSE_CLOTHES = "CLOTHES & HYGIENE";
    public static final String EXPENSE_ELECTRONIC = "ELECTRONIC";
    public static final String EXPENSE_GIFT = "GIFT";
    public static final String EXPENSE_FUN = "FUN";
    public static final String EXPENSE_SOCIAL = "SOCIAL";
    public static final String EXPENSE_TAX = "TAX & SIMILAR";
    public static final String EXPENSE_CAR = "CAR";
    public static final String EXPENSE_OTHER = "OTHER";
    public static final List<String> EXPENSE_CATEGORIES = List.of(
            EXPENSE_CAR,
            EXPENSE_CLOTHES,
            EXPENSE_ELECTRONIC,
            EXPENSE_FOOD,
            EXPENSE_FUN,
            EXPENSE_GIFT,
            EXPENSE_SOCIAL,
            EXPENSE_TAX,
            EXPENSE_TRAVEL,
            EXPENSE_OTHER
    );

    public static final String INCOME_SALARY = "SALARY";
    public static final String INCOME_GIFTS = "GIFTS";
    public static final String INCOME_REFUNDS = "REFUNDS";
    public static final String INCOME_OTHER = "OTHERS";
    public static final List<String> INCOME_CATEGORIES = List.of(
            INCOME_SALARY,
            INCOME_GIFTS,
            INCOME_REFUNDS,
            INCOME_OTHER
    );

    public static final String NET_WORTH_LIQUIDITY = "LIQUIDITY";
    public static final String NET_WORTH_ETF = "ETF";
    public static final String NET_WORTH_STOCK = "STOCK";
    public static final String NET_WORTH_BOND = "BOND";
    public static final String NET_WORTH_CRYPTO = "CRYPTO";
    public static final List<String> NET_WORTH_CATEGORIES = List.of(
            NET_WORTH_LIQUIDITY,
            NET_WORTH_ETF,
            NET_WORTH_STOCK,
            NET_WORTH_BOND,
            NET_WORTH_CRYPTO
    );

    public static final String INVESTMENT_ETF = "ETF";
    public static final String INVESTMENT_STOCK = "STOCK";
    public static final String INVESTMENT_BOND = "BOND";
    public static final String INVESTMENT_CRYPTO = "CRYPTO";
    public static final List<String> INVESTMENT_CATEGORIES = List.of(
            INVESTMENT_ETF,
            INVESTMENT_STOCK,
            INVESTMENT_BOND,
            INVESTMENT_CRYPTO
    );

    public static final String INVESTMENT_BUY = "BUY";
    public static final String INVESTMENT_SELL = "SELL";
    public static final List<String> INVESTMENT_TYPES = List.of(
            INVESTMENT_BUY,
            INVESTMENT_SELL
    );

    /* category used in the summaries for the subscriptions */
    public static final String SUBSCRIPTION_CATEGORY = "SUBSCRIPTION";

    /* values of the dashboard period combo boxes */
    public static final List<Integer> YEARS = List.of(
            2026,
            2027
    );
    public static final List<Integer> MONTHS = List.of(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
    );
}
