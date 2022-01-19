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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ResourceBundle;

@Route("registration")
public class RegistrationView extends Composite {

    private final UserService userService;
    private final ResourceBundle langResources;


    public RegistrationView(UserService userService) {
        this.userService = userService;
        langResources = ResourceBundle.getBundle("lang.res");
    }

    @Override
    protected Component initContent() {
        EmailField email = new EmailField(langResources.getString("email"));
        TextField username = new TextField(langResources.getString("username"));
        PasswordField password1 = new PasswordField(langResources.getString("password"));
        PasswordField password2 = new PasswordField(langResources.getString("confirm_pass"));
        VerticalLayout layout = new VerticalLayout();
        layout.add(new TopLoginBar(),
                new H2(langResources.getString("register")), email, username, password1, password2,
                new Button(langResources.getString("create_user"), event -> {
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
        if (!Validators.validateEmail(email)) {
            Notification.show(langResources.getString("wrong_email_msg")).setPosition(Notification.Position.MIDDLE);
        }
        else if (!Validators.validateUsername(username)) {
            Notification.show(langResources.getString("wrong_username_msg")).setPosition(Notification.Position.MIDDLE);
        }
        else if (!Validators.validatePassword(pass1) || !Validators.validatePassword(pass2)) {
            Notification.show(langResources.getString("wrong_password_msg")).setPosition(Notification.Position.MIDDLE);
        }
        else if (!pass1.equals(pass2)) {
            Notification.show(langResources.getString("pass_no_match")).setPosition(Notification.Position.MIDDLE);
        }
        else {
            if (userService.validateEmail(email)) {
                Notification.show(langResources.getString("other_email")).setPosition(Notification.Position.MIDDLE);
            }
            else if (userService.validateUsername(username)) {
                Notification.show(langResources.getString("other_username")).setPosition(Notification.Position.MIDDLE);
            }
            else {
                if (userService.registerUser(email, username, pass1)) {
                    Notification.show(langResources.getString("check_mail")).setPosition(Notification.Position.MIDDLE);
                }
                else {
                    Notification.show(langResources.getString("error")).setPosition(Notification.Position.MIDDLE);
                }
            }
        }
    }
}