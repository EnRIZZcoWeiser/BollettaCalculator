package org.enrycoweiser.finance.frontend.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.standard.StandardDto;
import org.enrycoweiser.finance.shared.utils.NameUtils;

import java.util.Set;

public abstract class StandardListView<E extends StandardDto> extends VerticalLayout {

    protected Grid<E> grid;

    public StandardListView() {
        grid = createGrid();

        add(
                createContent()
        );

        setSizeFull();
    }

    protected Grid<E> createGrid() {
        return new Grid<>();
    }

    protected Component createContent() {
        /*
            TOOLBAR
            GRID
            FOOTER
         */
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        Component toolbar = createContentToolbar();
        Component data = createContentData();
        Component footer = createContentFooter();

        layout.add(
                toolbar,
                data,
                footer
        );

        layout.expand(data);

        return layout;
    }

    protected Component createContentToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.START);
        toolbar.setJustifyContentMode(JustifyContentMode.START);

        toolbar.add(
                createToolbarAddButton(),
                createToolbarEditButton(),
                createToolbarRefreshButton(),
                createToolbarFilterButton(),
                createToolbarDeleteButton()
        );

        return toolbar;
    }

    protected Component createToolbarAddButton() {
        Button add = new Button(VaadinIcon.PLUS.create());
        add.setTooltipText("Add");
        add.setAriaLabel("Add");
        add.addThemeVariants(ButtonVariant.PRIMARY);
        add.addClickListener(e -> addButtonClicked());

        return add;
    }

    protected void addButtonClicked() {
        addBtn();
    }

    protected abstract void addBtn();

    protected Component createToolbarDeleteButton() {
        Button delete = new Button(VaadinIcon.TRASH.create());
        delete.setTooltipText("Delete");
        delete.setAriaLabel("Delete");
        delete.addThemeVariants(ButtonVariant.ERROR);
        delete.addClickListener(e -> deleteButtonClicked());

        return delete;
    }

    protected void deleteButtonClicked() {
        Integer rows = checkItems();

        if (rows == 0) {
            NotificationHelper.showErrorMessage("No rows selected");
            return;
        }

        if (rows == 2) {
            NotificationHelper.showErrorMessage("Too many rows selected");
            return;
        }

        deleteBtn();
    }

    protected abstract void deleteBtn();

    protected Component createToolbarRefreshButton() {
        Button refresh = new Button(VaadinIcon.REFRESH.create());
        refresh.setTooltipText("Refresh");
        refresh.setAriaLabel("Refresh");
        refresh.addThemeVariants(ButtonVariant.PRIMARY);
        refresh.addClickListener(e -> refreshButtonClicked());

        return refresh;
    }

    protected void refreshButtonClicked() {
        refreshBtn();
    }

    protected abstract void refreshBtn();

    protected Component createToolbarEditButton() {
        Button edit = new Button(VaadinIcon.EDIT.create());
        edit.setTooltipText("Edit");
        edit.setAriaLabel("Edit");
        edit.addThemeVariants(ButtonVariant.PRIMARY);
        edit.addClickListener(e -> editButtonClicked());

        return edit;
    }

    protected void editButtonClicked() {
        Integer rows = checkItems();

        if (rows == 0) {
            NotificationHelper.showErrorMessage("No rows selected");
            return;
        }

        if (rows == 2) {
            NotificationHelper.showErrorMessage("Too many rows selected");
            return;
        }

        editBtn();
    }

    protected abstract void editBtn();

    protected Component createToolbarFilterButton() {
        Button filter = new Button(VaadinIcon.FILTER.create());
        filter.setTooltipText("Filter");
        filter.setAriaLabel("Filter");
        filter.addThemeVariants(ButtonVariant.PRIMARY);
        filter.addClickListener(e -> filterButtonClicked());

        return filter;
    }

    protected void filterButtonClicked() {
        filterBtn();
    }

    protected abstract void filterBtn();

    protected Integer checkItems() {
        Set<E> items = grid.getSelectedItems();

        if (items == null || items.isEmpty()) {
            return 0;
        } else if (items.size() == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    protected Component createContentData() {
        HorizontalLayout content = new HorizontalLayout();

        content.setSizeFull();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);

        content.add(
                createContentGrid()
        );

        content.expand(grid);

        return content;
    }

    protected Component createContentGrid() {
        configureColumns();

        grid.setSizeFull();
        grid.getColumns().forEach(
                column -> {
                    column.setAutoWidth(true);
                });

        return grid;
    }

    protected abstract void configureColumns();

    protected Component createContentFooter() {
        HorizontalLayout footer = new HorizontalLayout();

        footer.setWidthFull();
        footer.setAlignItems(Alignment.CENTER);
        footer.setJustifyContentMode(JustifyContentMode.CENTER);

        footer.getStyle()
                .set("font-size", "0.8rem")
                .set("opacity", "0.7");

        footer.add(
                createFooterLink(VaadinIcon.ENVELOPE, NameUtils.AUTHOR_MAIL, "mailto:" + NameUtils.AUTHOR_MAIL),
                createFooterLink(VaadinIcon.TWITTER, NameUtils.AUTHOR_X, "https://x.com/" + NameUtils.AUTHOR_X),
                createFooterLink(VaadinIcon.CODE, NameUtils.AUTHOR_GITHUB, "https://github.com/" + NameUtils.AUTHOR_GITHUB)
        );

        return footer;
    }

    protected Component createFooterLink(VaadinIcon icon, String text, String href) {
        Icon i = icon.create();
        i.setSize("0.9rem");

        Anchor link = new Anchor(href, text);
        link.setTarget(AnchorTarget.BLANK);

        HorizontalLayout layout = new HorizontalLayout(i, link);
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        layout.getStyle().set("gap", "0.3rem");

        return layout;
    }
}
