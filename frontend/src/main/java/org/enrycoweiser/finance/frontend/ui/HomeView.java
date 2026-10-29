package org.enrycoweiser.finance.frontend.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.controller.DashboardController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.DashboardResponse;
import org.enrycoweiser.finance.shared.dto.CategoryAmountDto;
import org.enrycoweiser.finance.shared.dto.DashboardDto;
import org.enrycoweiser.finance.shared.dto.LiquidityDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

/*
    Start page: income and expenses of the selected month, grouped by category,
    and the liquidity of every payment method at today's date.
    The charts are plain horizontal bars (Div), no chart library is needed.
 */
@Route(value = "", layout = DashboardView.class)
@PageTitle("Dashboard")
public class HomeView extends VerticalLayout {

    protected static final String INCOME_COLOR = "#2e7d32";
    protected static final String INCOME_BACKGROUND = "#e8f5e9";
    protected static final String EXPENSE_COLOR = "#c62828";
    protected static final String EXPENSE_BACKGROUND = "#ffebee";
    protected static final String BALANCE_COLOR = "#1565c0";
    protected static final String BALANCE_BACKGROUND = "#e3f2fd";
    protected static final String LIQUIDITY_COLOR = "#455a64";
    protected static final String LIQUIDITY_BACKGROUND = "#eceff1";
    protected static final String TEXT_COLOR = "#1f1f1f";
    protected static final String TRACK_COLOR = "rgba(0, 0, 0, 0.08)";

    protected ComboBox<Integer> yearCmb;
    protected ComboBox<Integer> monthCmb;
    protected VerticalLayout content;

    public HomeView() {
        content = new VerticalLayout();
        content.setPadding(false);
        content.setWidthFull();

        add(
                createPeriodBar(),
                content
        );

        setWidthFull();

        refresh();
    }

    protected Component createPeriodBar() {
        LocalDate today = LocalDate.now();

        yearCmb = new ComboBox<>(NameUtils.YEAR);
        yearCmb.setItems(StaticUtils.YEARS);
        yearCmb.setValue(today.getYear());
        yearCmb.setWidth("8rem");
        yearCmb.addValueChangeListener(e -> refresh());

        monthCmb = new ComboBox<>(NameUtils.MONTH);
        monthCmb.setItems(StaticUtils.MONTHS);
        monthCmb.setItemLabelGenerator(m -> Month.of(m).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        monthCmb.setValue(today.getMonthValue());
        monthCmb.setWidth("10rem");
        monthCmb.addValueChangeListener(e -> refresh());

        HorizontalLayout bar = new HorizontalLayout(yearCmb, monthCmb);
        bar.setAlignItems(Alignment.BASELINE);

        return bar;
    }

    protected void refresh() {
        if(yearCmb.getValue() == null || monthCmb.getValue() == null) {
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put(FilterUtils.DASHBOARD_YEAR, yearCmb.getValue());
        data.put(FilterUtils.DASHBOARD_MONTH, monthCmb.getValue());

        DashboardResponse response = DashboardController.callSummaryAPI(data);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.DASHBOARD + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        if(response.getItems() == null || response.getItems().isEmpty()) {
            return;
        }

        DashboardDto dto = response.getItems().get(0);

        content.removeAll();
        content.add(
                createCards(dto),
                createCategories(dto),
                createLiquidity(dto)
        );
    }

    /*
        CARDS: money in, money out, balance
     */
    protected Component createCards(DashboardDto dto) {
        BigDecimal moneyIn = nvl(dto.getMoneyIn());
        BigDecimal moneyOut = nvl(dto.getMoneyOut());

        HorizontalLayout cards = new HorizontalLayout(
                createCard(NameUtils.MONEY_IN, moneyIn, INCOME_COLOR, INCOME_BACKGROUND),
                createCard(NameUtils.MONEY_OUT, moneyOut, EXPENSE_COLOR, EXPENSE_BACKGROUND),
                createCard(NameUtils.BALANCE, moneyIn.subtract(moneyOut), BALANCE_COLOR, BALANCE_BACKGROUND)
        );
        cards.setWidthFull();
        cards.getStyle().set("flex-wrap", "wrap");

        return cards;
    }

    protected Component createCard(String title, BigDecimal amount, String color, String background) {
        Span titleSpan = new Span(title);
        titleSpan.getStyle()
                .set("font-size", "0.9rem")
                .set("font-weight", "600")
                .set("text-transform", "uppercase")
                .set("color", color);

        Span amountSpan = new Span(format(amount));
        amountSpan.getStyle()
                .set("font-size", "1.8rem")
                .set("font-weight", "700")
                .set("color", TEXT_COLOR);

        Div card = new Div(titleSpan, amountSpan);
        card.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.25rem")
                .set("flex", "1 1 14rem")
                .set("padding", "1rem 1.25rem")
                .set("border-radius", "0.75rem")
                .set("border-left", "0.4rem solid " + color)
                .set("background", background);

        return card;
    }

    /*
        CATEGORIES: one box for the incomes and one for the expenses
     */
    protected Component createCategories(DashboardDto dto) {
        HorizontalLayout boxes = new HorizontalLayout(
                createCategoryBox(NameUtils.MONEY_IN, dto.getIncomeCategories(), INCOME_COLOR),
                createCategoryBox(NameUtils.MONEY_OUT, dto.getExpenseCategories(), EXPENSE_COLOR)
        );
        boxes.setWidthFull();
        boxes.setAlignItems(Alignment.START);
        boxes.getStyle().set("flex-wrap", "wrap");

        return boxes;
    }

    protected Component createCategoryBox(String title, List<CategoryAmountDto> categories, String color) {
        H3 header = new H3(title);
        header.getStyle()
                .set("margin", "0 0 0.5rem 0")
                .set("font-size", "1.1rem")
                .set("color", color);

        Div box = new Div(header);
        box.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.6rem")
                .set("flex", "1 1 22rem")
                .set("padding", "1rem 1.25rem")
                .set("border-radius", "0.75rem")
                .set("border", "1px solid " + TRACK_COLOR);

        if(categories == null || categories.isEmpty()) {
            Span empty = new Span(NameUtils.NO_DATA);
            empty.getStyle().set("opacity", "0.6");
            box.add(empty);

            return box;
        }

        // the biggest category fills the whole bar, the others are scaled on it
        BigDecimal max = BigDecimal.ZERO;
        for (CategoryAmountDto c : categories) {
            max = max.max(nvl(c.getAmount()));
        }

        for (CategoryAmountDto c : categories) {
            box.add(createBarRow(c, max, color));
        }

        return box;
    }

    protected Component createBarRow(CategoryAmountDto c, BigDecimal max, String color) {
        Span label = new Span(c.getCategory());
        label.getStyle()
                .set("width", "10rem")
                .set("flex-shrink", "0")
                .set("font-size", "0.85rem");

        BigDecimal width = max.signum() == 0
                ? BigDecimal.ZERO
                : nvl(c.getAmount()).multiply(BigDecimal.valueOf(100)).divide(max, 1, RoundingMode.HALF_UP);

        Div fill = new Div();
        fill.getStyle()
                .set("width", width.toPlainString() + "%")
                .set("height", "100%")
                .set("border-radius", "0.3rem")
                .set("background", color);

        Div track = new Div(fill);
        track.getStyle()
                .set("flex", "1")
                .set("height", "0.8rem")
                .set("border-radius", "0.3rem")
                .set("background", TRACK_COLOR);

        Span amount = new Span(format(c.getAmount()));
        amount.getStyle()
                .set("width", "6.5rem")
                .set("flex-shrink", "0")
                .set("text-align", "right")
                .set("font-weight", "600")
                .set("font-size", "0.85rem");

        Span percentage = new Span(nvl(c.getPercentage()).toPlainString() + "%");
        percentage.getStyle()
                .set("width", "3.5rem")
                .set("flex-shrink", "0")
                .set("text-align", "right")
                .set("font-size", "0.8rem")
                .set("opacity", "0.7");

        HorizontalLayout row = new HorizontalLayout(label, track, amount, percentage);
        row.setWidthFull();
        row.setAlignItems(Alignment.CENTER);

        return row;
    }

    /*
        LIQUIDITY: one card for every payment method, balance at today's date
     */
    protected Component createLiquidity(DashboardDto dto) {
        H3 header = new H3(NameUtils.LIQUIDITY);
        header.getStyle()
                .set("margin", "0")
                .set("font-size", "1.1rem")
                .set("color", LIQUIDITY_COLOR);

        /*
            Grid with 3 slots per row: with 2 payment methods they take 2 slots (2/3 of the width).
            Every slot is at least 12rem wide, so on narrow screens there are fewer columns.
         */
        Div cards = new Div();
        cards.setWidthFull();
        cards.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(max(12rem, calc((100% - 2rem) / 3)), 1fr))")
                .set("gap", "1rem");

        if(dto.getLiquidity() != null) {
            for (LiquidityDto l : dto.getLiquidity()) {
                cards.add(createLiquidityCard(l));
            }
        }

        VerticalLayout section = new VerticalLayout(header, cards);
        section.setPadding(false);
        section.setWidthFull();

        return section;
    }

    protected Component createLiquidityCard(LiquidityDto l) {
        BigDecimal balance = nvl(l.getBalance());

        Span nameSpan = new Span(l.getPaymentMethod());
        nameSpan.getStyle()
                .set("font-size", "0.85rem")
                .set("font-weight", "600")
                .set("text-transform", "uppercase")
                .set("color", LIQUIDITY_COLOR);

        Span balanceSpan = new Span(format(balance));
        balanceSpan.getStyle()
                .set("font-size", "1.4rem")
                .set("font-weight", "700")
                .set("color", balance.signum() < 0 ? EXPENSE_COLOR : TEXT_COLOR);

        Div card = new Div(nameSpan, balanceSpan);
        card.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.25rem")
                .set("padding", "0.8rem 1rem")
                .set("border-radius", "0.75rem")
                .set("border-left", "0.4rem solid " + LIQUIDITY_COLOR)
                .set("background", LIQUIDITY_BACKGROUND);

        return card;
    }

    protected String format(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(Locale.ITALY).format(nvl(amount));
    }

    protected BigDecimal nvl(BigDecimal amount) {
        return amount != null ? amount : BigDecimal.ZERO;
    }
}
