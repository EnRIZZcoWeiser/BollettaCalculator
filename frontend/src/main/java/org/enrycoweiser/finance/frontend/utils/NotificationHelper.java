package org.enrycoweiser.finance.frontend.utils;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NotificationHelper {
    private static final int DURATION = 3000;

    public static void showErrorMessage(String message) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.ERROR);
        notification.setDuration(DURATION);

        showMessage(notification, message);
    }

    public static void showConfirmMessage(String message) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.SUCCESS);
        notification.setDuration(DURATION);

        showMessage(notification, message);

    }

    public static void showMessage(Notification notification, String message) {
        notification.setPosition(Notification.Position.TOP_CENTER);

        HorizontalLayout layout = new HorizontalLayout();

        Text mess = new Text(message);

        Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClickListener(event -> notification.close());

        layout.add(mess, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        notification.add(layout);

        notification.open();
    }
}
