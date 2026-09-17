package org.enrycoweiser.finance.frontend.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.standard.StandardDto;

import java.util.Set;

public abstract class StandardListView<E extends StandardDto,
                                ALT extends StandardAlterDlg,
                                FIL extends StandardFilterDlg> extends VerticalLayout {

    protected Grid<E> grid;
    protected ALT alterDlg;
    protected FIL filterDlg;

    public StandardListView(Class<E> entityClass, Class<ALT> alterDlgClass, Class<FIL> filterDlgClass) {
        grid = new Grid<>(entityClass);

        try {
            alterDlg = alterDlgClass.getDeclaredConstructor().newInstance();
            filterDlg = filterDlgClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            alterDlg = null;
            filterDlg = null;
        }

        add(
                createContent()
        );

        setSizeFull();
    }

    protected Component createContent() {
        /*
            TOOLBAR
            GRID
            FOOTER
         */
        VerticalLayout layout = new VerticalLayout();

        layout.add(
                createContentToolbar(),
                createContentData(),
                createContentFooter()
        );

        return layout;
    }

    protected Component createContentToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.START);
        toolbar.setJustifyContentMode(JustifyContentMode.START);

        toolbar.add(
                createToolbarAddButton(),
                createToolbarDeleteButton(),
                createToolbarRefreshButton(),
                createToolbarEditButton(),
                createToolbarFilterButton()
        );

        return toolbar;
    }

    protected Component createToolbarAddButton() {
        Button add = new Button("Add");
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addClickListener(e -> addButtonClicked());

        return add;
    }

    protected void addButtonClicked() {
        addBtn();
    }

    protected abstract void addBtn();

    protected Component createToolbarDeleteButton() {
        Button delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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
        Button refresh = new Button("Refresh");
        refresh.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        refresh.addClickListener(e -> refreshButtonClicked());

        return refresh;
    }

    protected void refreshButtonClicked() {
        refreshBtn();
    }

    protected abstract void refreshBtn();

    protected Component createToolbarEditButton() {
        Button edit = new Button("Edit");
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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
        Button filter = new Button("Filter");
        filter.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
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

        footer.add(
                createFooterText()
        );

        return footer;
    }

    protected Component createFooterText() {
        return new H1("Footer");
    }
}
