package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.backend.service.MailService;
import com.filipzyla.diabeticapp.backend.service.UserService;
import com.filipzyla.diabeticapp.backend.utility.SugarDefaultSettings;
import com.filipzyla.diabeticapp.backend.utility.Validators;
import com.filipzyla.diabeticapp.ui.components.TopMenuBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.transaction.annotation.Transactional;

@Route("settings")
@Transactional
public class SettingsView extends VerticalLayout {

    private final User user;

    public SettingsView(SecurityService securityService, UserService userService, MailService mailService) {
        user = userService.findByUsername(securityService.getAuthenticatedUser());

        HorizontalLayout mainSettingsLayout = new HorizontalLayout();
        mainSettingsLayout.add(credentialsLayout(userService, securityService, mailService), settingsLayout(userService));

        setAlignItems(Alignment.CENTER);
        add(new TopMenuBar(securityService), new H2("Settings"), mainSettingsLayout);
    }

    private VerticalLayout credentialsLayout(UserService userService, SecurityService securityService, MailService mailService) {
        TextField username = new TextField("Username");
        username.setValue(user.getUsername());
        PasswordField password = new PasswordField("Password");
        password.setValue(user.getPassword());
        PasswordField rePassword = new PasswordField("Confirm password");
        EmailField email = new EmailField("Email");
        email.setValue(user.getEmail());
        EmailField reEmail = new EmailField("Confirm email");
        Button saveCredentials = new Button("Save changes", event -> {
            if (!username.getValue().equals(user.getUsername()) && userService.validateUsername(username.getValue())) {
                Notification.show("Username already exist!").setPosition(Notification.Position.MIDDLE);
            }
            else if (!EmailValidator.getInstance().isValid(email.getValue())) {
                Notification.show("Wrong email!").setPosition(Notification.Position.MIDDLE);
            }
            else if (!email.getValue().equals(user.getEmail()) && userService.validateEmail(email.getValue())) {
                Notification.show("Email already exist!").setPosition(Notification.Position.MIDDLE);
            }
            else if (!email.getValue().equals(user.getEmail()) && !email.getValue().equals(reEmail.getValue())) {
                Notification.show("Emails don't match!").setPosition(Notification.Position.MIDDLE);
            }
            else if (!password.getValue().equals(user.getPassword()) && !password.getValue().equals(rePassword.getValue())) {
                Notification.show("Passwords don't match!").setPosition(Notification.Position.MIDDLE);
            }
            else {
                user.setUsername(username.getValue());
                user.setPassword(password.getValue());
                user.setEmail(email.getValue());
                userService.saveUser(user);
                rePassword.setValue("");
                reEmail.setValue("");
                mailService.changeCredentials(user);
                Notification.show("Changes saved, confirmation send to your email!").setPosition(Notification.Position.MIDDLE);
            }
        });

        Button deleteAccount = new Button("Delete account", event -> {
            Dialog deleteDialog = new Dialog();
            deleteDialog.add(new H5("Do you want to delete account?"),
                    new H5("All your data will be deleted also."),
                    new Button("Delete", click -> {
                        userService.deleteUser(user);
                        deleteDialog.close();
                        securityService.logout();
                    }),
                    new Button("Close", click -> {
                        deleteDialog.close();
                    }));
            deleteDialog.open();
            add(deleteDialog);
        });

        VerticalLayout credentialsLayout = new VerticalLayout();
        credentialsLayout.add(username, password, rePassword, email, reEmail, saveCredentials, deleteAccount);

        return credentialsLayout;
    }

    private VerticalLayout settingsLayout(UserService userService) {
        NumberField hypoglycemia = new NumberField("Hypoglycemia");
        hypoglycemia.setValue(user.getHypoglycemia());
        hypoglycemia.setStep(1);
        NumberField hyperglycemia = new NumberField("Before Meal");
        hyperglycemia.setValue(user.getHyperglycemia());
        hyperglycemia.setStep(1);
        NumberField hyperglycemiaAfterMeal = new NumberField("After meal");
        hyperglycemiaAfterMeal.setValue(user.getHyperglycemiaAfterMeal());
        hyperglycemiaAfterMeal.setStep(1);

        Button saveSugar = new Button("Save changes", event -> {
            if (!Validators.validateSugar(hypoglycemia.getValue().intValue())
                    || !Validators.validateSugar(hyperglycemia.getValue().intValue())
                    || !Validators.validateSugar(hyperglycemiaAfterMeal.getValue().intValue())) {
                Notification.show("Values must be between " + Validators.MIN_SUGAR + " - " + Validators.MAX_SUGAR + "!")
                        .setPosition(Notification.Position.MIDDLE);
            }
            else {
                user.setHypoglycemia(hypoglycemia.getValue());
                user.setHyperglycemia(hyperglycemia.getValue());
                user.setHyperglycemiaAfterMeal(hyperglycemiaAfterMeal.getValue());
                userService.saveUser(user);
                Notification.show("Changes saved").setPosition(Notification.Position.MIDDLE);
            }
        });

        Button defaultSettings = new Button("Default values", event -> {
            user.setHypoglycemia(SugarDefaultSettings.DEFAULT_HYPOGLYCEMIA);
            user.setHyperglycemia(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA);
            user.setHyperglycemiaAfterMeal(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL);
            hypoglycemia.setValue(SugarDefaultSettings.DEFAULT_HYPOGLYCEMIA);
            hyperglycemia.setValue(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA);
            hyperglycemiaAfterMeal.setValue(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL);
            userService.saveUser(user);
            Notification.show("Default values set").setPosition(Notification.Position.MIDDLE);
        });

        VerticalLayout settingsLayout = new VerticalLayout();
        settingsLayout.add(hypoglycemia, hyperglycemia, hyperglycemiaAfterMeal, saveSugar, defaultSettings);
        return settingsLayout;
    }
}