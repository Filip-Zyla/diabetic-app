package com.filipzyla.diabeticapp.ui.login;

import com.filipzyla.diabeticapp.backend.service.UserService;
import com.filipzyla.diabeticapp.backend.utility.Validators;
import com.filipzyla.diabeticapp.ui.utility.TopLoginBar;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Route;

import java.util.ResourceBundle;

@Route("forgotPassword")
public class ForgotPasswordView extends Composite {

    private final UserService userService;

    public ForgotPasswordView(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected Component initContent() {
        final ResourceBundle langResources = ResourceBundle.getBundle("lang.res");

        VerticalLayout mainLayout = new VerticalLayout();
        EmailField emailField = new EmailField(langResources.getString("email"));
        Button sendReminder = new Button(langResources.getString("send_email"), buttonClickEvent -> {
            if (Validators.validateEmail(emailField.getValue())) {
                userService.forgotPassword(emailField.getValue());
                Notification.show(langResources.getString("check_mail")).setPosition(Notification.Position.MIDDLE);
            }
            else {
                Notification.show(langResources.getString("wrong_email_msg")).setPosition(Notification.Position.MIDDLE);
            }
        });
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.add(new TopLoginBar(), new H2(langResources.getString("forgot_pass")), emailField, sendReminder);
        return mainLayout;
    }
}
