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
}
