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
        TextField email = new TextField("Email");
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H2("Register"), username, email, password1, password2,
                new Button("Create user", event -> {
                    register(
                            username.getValue(),
                            email.getValue(),
                            password1.getValue(),
                            password2.getValue());
                    UI.getCurrent().navigate("login");
                })
        );
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private void register(String username, String email, String pass1, String pass2) {
        if (username.trim().isEmpty())
            Notification.show("Enter a username");
        else if (email.trim().isEmpty())
            Notification.show("Enter an email");
        else if (pass1.isEmpty())
            Notification.show("Enter a password");
        else if (!pass1.equals(pass2))
            Notification.show("Passwords don't match");
        else {
            if (!userService.validateEmail(email)) {
                Notification.show("Use other email.");
            }
            else if (!userService.validateUsername(username)) {
                Notification.show("User other username.");
            }
            else {
                userService.registerUser(email, username, pass1);
                Notification.show("Check your email.");
            }
        }
    }
}