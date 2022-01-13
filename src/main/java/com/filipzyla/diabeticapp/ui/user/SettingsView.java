package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.backend.service.UserService;
import com.filipzyla.diabeticapp.backend.utility.SugarDefaultSettings;
import com.filipzyla.diabeticapp.backend.utility.Validators;
import com.filipzyla.diabeticapp.ui.utility.TopMenuBar;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.transaction.annotation.Transactional;

@Route("settings")
@Transactional
public class SettingsView extends VerticalLayout {

    private final User user;
    private final VerticalLayout mainLayout;

    public SettingsView(SecurityService securityService, UserService userService) {
        user = userService.findByUsername(securityService.getAuthenticatedUser());

        mainLayout = new VerticalLayout();
        Tab profileTab = new Tab("Profile");
        Tab sugarTab = new Tab("Sugar");
        Tabs tabs = new Tabs(profileTab, sugarTab);

        tabs.addSelectedChangeListener(event -> {
                    mainLayout.removeAll();
                    if (tabs.getSelectedTab().equals(profileTab)) {
                        credentialsLayout(userService, securityService);
                    }
                    else if (tabs.getSelectedTab().equals(sugarTab)) {
                        mainLayout.add(sugarLayout(userService));
                    }
                }
        );

        tabs.setSelectedTab(profileTab);
        credentialsLayout(userService, securityService);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(Alignment.CENTER);

        setAlignItems(Alignment.CENTER);
        add(new TopMenuBar(securityService), tabs, mainLayout);
    }

    private VerticalLayout sugarLayout(UserService userService) {
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
            if (Validators.validateSugar(hypoglycemia.getValue().intValue())
                    && Validators.validateSugar(hyperglycemia.getValue().intValue())
                    && Validators.validateSugar(hyperglycemiaAfterMeal.getValue().intValue())) {
                user.setHypoglycemia(hypoglycemia.getValue());
                user.setHyperglycemia(hyperglycemia.getValue());
                user.setHyperglycemiaAfterMeal(hyperglycemiaAfterMeal.getValue());
                userService.saveUser(user, false);
                Notification.show("Changes saved").setPosition(Notification.Position.MIDDLE);
            }
            else {
                Notification.show(Validators.WRONG_SUGAR_MSG).setPosition(Notification.Position.MIDDLE);
            }
        });

        Button defaultSettings = new Button("Default values", event -> {
            user.setHypoglycemia(SugarDefaultSettings.DEFAULT_HYPOGLYCEMIA);
            user.setHyperglycemia(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA);
            user.setHyperglycemiaAfterMeal(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL);
            hypoglycemia.setValue(SugarDefaultSettings.DEFAULT_HYPOGLYCEMIA);
            hyperglycemia.setValue(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA);
            hyperglycemiaAfterMeal.setValue(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL);
            userService.saveUser(user, false);
            Notification.show("Default values set").setPosition(Notification.Position.MIDDLE);
        });

        VerticalLayout sugarLayout = new VerticalLayout();
        sugarLayout.setAlignItems(Alignment.CENTER);
        sugarLayout.add(hypoglycemia, hyperglycemia, hyperglycemiaAfterMeal, saveSugar, defaultSettings);
        return sugarLayout;
    }

    private void credentialsLayout(UserService userService, SecurityService securityService) {
        VerticalLayout credentialsLayout = new VerticalLayout();
        Tab usernameTab = new Tab("Username");
        Tab passwordTab = new Tab("Password");
        Tab emailTab = new Tab("Email");
        Tab deleteTab = new Tab("Delete");
        Tabs tabs = new Tabs(usernameTab, passwordTab, emailTab, deleteTab);

        tabs.addSelectedChangeListener(event -> {
                    credentialsLayout.removeAll();
                    if (tabs.getSelectedTab().equals(usernameTab)) {
                        credentialsLayout.add(usernameLayout(userService));
                    }
                    else if (tabs.getSelectedTab().equals(passwordTab)) {
                        credentialsLayout.add(passwordLayout(userService));
                    }
                    else if (tabs.getSelectedTab().equals(emailTab)) {
                        credentialsLayout.add(emailLayout(userService));
                    }
                    else if (tabs.getSelectedTab().equals(deleteTab)) {
                        credentialsLayout.add(deleteLayout(userService, securityService));
                    }
                }
        );

        tabs.setSelectedTab(usernameTab);
        credentialsLayout.add(usernameLayout(userService));
        credentialsLayout.setSpacing(false);
        credentialsLayout.setAlignItems(Alignment.CENTER);

        setAlignItems(Alignment.CENTER);
        mainLayout.add(tabs, credentialsLayout);
    }

    private Component usernameLayout(UserService userService) {
        TextField usernameField = new TextField("Username");
        usernameField.setValue(user.getUsername());
        Button changeUsername = new Button("Save changes", event -> {
            if (usernameField.getValue().equals(user.getUsername())) {

            }
            else if (!Validators.validateUsername(usernameField.getValue())) {
                Notification.show(Validators.WRONG_USERNAME_MSG).setPosition(Notification.Position.MIDDLE);
            }
            else if (userService.validateUsername(usernameField.getValue())) {
                Notification.show("Username already exist!").setPosition(Notification.Position.MIDDLE);
            }
            else {
                Dialog popup = new Dialog();
                Button saveButton = new Button("Save", eventSave -> {
                    user.setUsername(usernameField.getValue());
                    userService.saveUser(user, false);
                    popup.close();
                });
                Button closeButton = new Button("Close", eventClose -> popup.close());
                popup.add(new H3("Do you want to save changes?"), saveButton, closeButton);
                popup.open();
            }
        });
        VerticalLayout verticalLayout = new VerticalLayout(usernameField, changeUsername);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }

    private Component passwordLayout(UserService userService) {
        PasswordField password = new PasswordField("Password");
        PasswordField rePassword = new PasswordField("Confirm password");

        Button changePassword = new Button("Save changes", event -> {
            if (!Validators.validatePassword(password.getValue()) && !Validators.validatePassword(rePassword.getValue())) {
                Notification.show(Validators.WRONG_PASSWORD_MSG).setPosition(Notification.Position.MIDDLE);
            }
            else if (!password.getValue().equals(rePassword.getValue())) {
                Notification.show("Passwords don't match").setPosition(Notification.Position.MIDDLE);
            }
            else {
                Dialog popup = new Dialog();
                Button saveButton = new Button("Save", eventSave -> {
                    userService.changePassword(user, password.getValue());
                    popup.close();
                    password.setValue("");
                    rePassword.setValue("");
                    Notification.show("Changes saved, confirmation send to your email").setPosition(Notification.Position.MIDDLE);
                });
                Button closeButton = new Button("Close", eventClose -> popup.close());
                popup.add(new H3("Do you want to save changes?"), saveButton, closeButton);
                popup.open();
            }
        });
        VerticalLayout verticalLayout = new VerticalLayout(password, rePassword, changePassword);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }

    private Component emailLayout(UserService userService) {
        EmailField emailField = new EmailField("Email");
        emailField.setValue(user.getEmail());
        Button changeEmail = new Button("Save changes", event -> {
            if (emailField.getValue().equals(user.getEmail())) {

            }
            else if (!Validators.validateEmail(emailField.getValue())) {
                Notification.show(Validators.WRONG_EMAIL_MSG).setPosition(Notification.Position.MIDDLE);
            }
            else if (userService.validateEmail(emailField.getValue())) {
                Notification.show("Email already exist!").setPosition(Notification.Position.MIDDLE);
            }
            else {
                Dialog popup = new Dialog();
                Button saveButton = new Button("Save", eventSave -> {
                    userService.changeEmail(user, emailField.getValue());
                    popup.close();
                });
                Button closeButton = new Button("Close", eventClose -> popup.close());
                popup.add(new H3("Do you want to save changes?"), saveButton, closeButton);
                popup.open();
            }
        });
        VerticalLayout verticalLayout = new VerticalLayout(emailField, changeEmail);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }

    private Component deleteLayout(UserService userService, SecurityService securityService) {
        Button deleteAccount = new Button("Delete account", event -> {
            Dialog deleteDialog = new Dialog();
            deleteDialog.add(new H3("Do you want to delete account?"),
                    new H3("All your data will be deleted"),
                    new Button("Delete", click -> {
                        userService.deleteUser(user);
                        deleteDialog.close();
                        securityService.logout();
                    }),
                    new Button("Close", click -> deleteDialog.close()));
            deleteDialog.open();
        });
        VerticalLayout verticalLayout = new VerticalLayout(deleteAccount);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }
}