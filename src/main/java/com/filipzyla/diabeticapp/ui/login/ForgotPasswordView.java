package com.filipzyla.diabeticapp.ui.login;

import com.filipzyla.diabeticapp.backend.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Route;
import org.apache.commons.validator.routines.EmailValidator;

@Route("forgotPassword")
public class ForgotPasswordView extends Composite {

    private final UserService userService;

    public ForgotPasswordView(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected Component initContent() {
        VerticalLayout mainLayout = new VerticalLayout();

        EmailField emailField = new EmailField("Email");
        Button sendReminder = new Button("Send email", buttonClickEvent -> {
            if (!emailField.isEmpty() && EmailValidator.getInstance().isValid(emailField.getValue())) {
                userService.forgotPassword(emailField.getValue());
            }
            Notification.show("Email with credentials sent!").setPosition(Notification.Position.MIDDLE);
        });
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.add(new Button("Home", e -> UI.getCurrent().navigate("login")), new H2("Forgot password"), emailField, sendReminder);
        return mainLayout;
    }
}
