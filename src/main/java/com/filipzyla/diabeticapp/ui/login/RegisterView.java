package com.filipzyla.diabeticapp.ui.login;

import com.filipzyla.diabeticapp.backend.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegisterView extends Composite {

    private final UserService userService;

    public RegisterView(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected Component initContent() {
        EmailField email = new EmailField("Email");
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H2("Register"), email, username, password1, password2,
                new Button("Create user", event -> {
                    register(
                            username.getValue(),
                            email.getValue(),
                            password1.getValue(),
                            password2.getValue());
                })
        );
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private void register(String username, String email, String pass1, String pass2) {
        if (username.trim().isEmpty())
            Notification.show("Enter a username").setPosition(Notification.Position.MIDDLE);
        else if (pass1.isEmpty() || pass2.isEmpty())
            Notification.show("Enter a password").setPosition(Notification.Position.MIDDLE);
        else if (!pass1.equals(pass2))
            Notification.show("Passwords don't match").setPosition(Notification.Position.MIDDLE);
        else {
            if (userService.validateEmail(email))
                Notification.show("Use other email.").setPosition(Notification.Position.MIDDLE);
            else if (userService.validateUsername(username))
                Notification.show("User other username.").setPosition(Notification.Position.MIDDLE);
            else {
                if (userService.registerUser(email, username, pass1)) {
                    Notification.show("Check your email.").setPosition(Notification.Position.MIDDLE);
                }
                else {
                    Notification.show("Something gone wrong, try again.").setPosition(Notification.Position.MIDDLE);
                }
            }
        }
    }
}