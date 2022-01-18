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

import java.util.ResourceBundle;

@Route("settings")
@Transactional
public class SettingsView extends VerticalLayout {

    private final User user;
    private final VerticalLayout mainLayout;

    public final ResourceBundle langResources;

    public SettingsView(SecurityService securityService, UserService userService) {
        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");

        mainLayout = new VerticalLayout();
        Tab profileTab = new Tab(langResources.getString("profile"));
        Tab sugarTab = new Tab(langResources.getString("sugar"));
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
        NumberField hypoglycemia = new NumberField(langResources.getString("hypoglycemia"));
        hypoglycemia.setValue(user.getHypoglycemia());
        hypoglycemia.setStep(1);
        NumberField hyperglycemia = new NumberField(langResources.getString("before_meal"));
        hyperglycemia.setValue(user.getHyperglycemia());
        hyperglycemia.setStep(1);
        NumberField hyperglycemiaAfterMeal = new NumberField(langResources.getString("after_meal"));
        hyperglycemiaAfterMeal.setValue(user.getHyperglycemiaAfterMeal());
        hyperglycemiaAfterMeal.setStep(1);

        Button saveSugar = new Button(langResources.getString("save_changes"), event -> {
            if (Validators.validateSugar(hypoglycemia.getValue().intValue())
                    && Validators.validateSugar(hyperglycemia.getValue().intValue())
                    && Validators.validateSugar(hyperglycemiaAfterMeal.getValue().intValue())) {
                user.setHypoglycemia(hypoglycemia.getValue());
                user.setHyperglycemia(hyperglycemia.getValue());
                user.setHyperglycemiaAfterMeal(hyperglycemiaAfterMeal.getValue());
                userService.saveUser(user, false);
                Notification.show(langResources.getString("changes_saved")).setPosition(Notification.Position.MIDDLE);
            }
            else {
                Notification.show(langResources.getString("values_between") + Validators.SUGAR_RANGE).setPosition(Notification.Position.MIDDLE);
            }
        });

        Button defaultSettings = new Button(langResources.getString("def_val"), event -> {
            user.setHypoglycemia(SugarDefaultSettings.DEFAULT_HYPOGLYCEMIA);
            user.setHyperglycemia(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA);
            user.setHyperglycemiaAfterMeal(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL);
            hypoglycemia.setValue(SugarDefaultSettings.DEFAULT_HYPOGLYCEMIA);
            hyperglycemia.setValue(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA);
            hyperglycemiaAfterMeal.setValue(SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL);
            userService.saveUser(user, false);
            Notification.show(langResources.getString("def_val_save")).setPosition(Notification.Position.MIDDLE);
        });

        VerticalLayout sugarLayout = new VerticalLayout();
        sugarLayout.setAlignItems(Alignment.CENTER);
        sugarLayout.add(hypoglycemia, hyperglycemia, hyperglycemiaAfterMeal, saveSugar, defaultSettings);
        return sugarLayout;
    }

    private void credentialsLayout(UserService userService, SecurityService securityService) {
        VerticalLayout credentialsLayout = new VerticalLayout();
        Tab usernameTab = new Tab(langResources.getString("username"));
        Tab passwordTab = new Tab(langResources.getString("password"));
        Tab emailTab = new Tab(langResources.getString("email"));
        Tab deleteTab = new Tab(langResources.getString("delete"));
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
        TextField usernameField = new TextField(langResources.getString("username"));
        usernameField.setValue(user.getUsername());
        Button changeUsername = new Button(langResources.getString("save_changes"), event -> {
            if (usernameField.getValue().equals(user.getUsername())) {

            }
            else if (!Validators.validateUsername(usernameField.getValue())) {
                Notification.show(langResources.getString("wrong_username_msg")).setPosition(Notification.Position.MIDDLE);
            }
            else if (userService.validateUsername(usernameField.getValue())) {
                Notification.show(langResources.getString("username_exist")).setPosition(Notification.Position.MIDDLE);
            }
            else {
                Dialog popup = new Dialog();
                Button saveButton = new Button(langResources.getString("save"), eventSave -> {
                    user.setUsername(usernameField.getValue());
                    userService.saveUser(user, false);
                    popup.close();
                });
                Button closeButton = new Button(langResources.getString("close"), eventClose -> popup.close());
                popup.add(new H3(langResources.getString("save_ask")), saveButton, closeButton);
                popup.open();
            }
        });
        VerticalLayout verticalLayout = new VerticalLayout(usernameField, changeUsername);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }

    private Component passwordLayout(UserService userService) {
        PasswordField password = new PasswordField(langResources.getString("password"));
        PasswordField rePassword = new PasswordField(langResources.getString("confirm_pass"));

        Button changePassword = new Button(langResources.getString("save_changes"), event -> {
            if (!Validators.validatePassword(password.getValue()) && !Validators.validatePassword(rePassword.getValue())) {
                Notification.show(langResources.getString("wrong_password_msg")).setPosition(Notification.Position.MIDDLE);
            }
            else if (!password.getValue().equals(rePassword.getValue())) {
                Notification.show(langResources.getString("pass_no_match")).setPosition(Notification.Position.MIDDLE);
            }
            else {
                Dialog popup = new Dialog();
                Button saveButton = new Button(langResources.getString("save"), eventSave -> {
                    userService.changePassword(user, password.getValue());
                    popup.close();
                    password.setValue("");
                    rePassword.setValue("");
                    Notification.show(langResources.getString("new_cred_mail")).setPosition(Notification.Position.MIDDLE);
                });
                Button closeButton = new Button(langResources.getString("close"), eventClose -> popup.close());
                popup.add(new H3(langResources.getString("save_ask")), saveButton, closeButton);
                popup.open();
            }
        });
        VerticalLayout verticalLayout = new VerticalLayout(password, rePassword, changePassword);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }

    private Component emailLayout(UserService userService) {
        EmailField emailField = new EmailField(langResources.getString("email"));
        emailField.setValue(user.getEmail());
        Button changeEmail = new Button(langResources.getString("save_changes"), event -> {
            if (emailField.getValue().equals(user.getEmail())) {

            }
            else if (!Validators.validateEmail(emailField.getValue())) {
                Notification.show(langResources.getString("wrong_email_msg")).setPosition(Notification.Position.MIDDLE);
            }
            else if (userService.validateEmail(emailField.getValue())) {
                Notification.show(langResources.getString("email_exist")).setPosition(Notification.Position.MIDDLE);
            }
            else {
                Dialog popup = new Dialog();
                Button saveButton = new Button(langResources.getString("save"), eventSave -> {
                    userService.changeEmail(user, emailField.getValue());
                    popup.close();
                });
                Button closeButton = new Button(langResources.getString("close"), eventClose -> popup.close());
                popup.add(new H3(langResources.getString("save_ask")), saveButton, closeButton);
                popup.open();
            }
        });
        VerticalLayout verticalLayout = new VerticalLayout(emailField, changeEmail);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }

    private Component deleteLayout(UserService userService, SecurityService securityService) {
        Button deleteAccount = new Button(langResources.getString("delete_acc"), event -> {
            Dialog deleteDialog = new Dialog();
            deleteDialog.add(new H3(langResources.getString("delete_ask")),
                    new H3(langResources.getString("delete_conf")),
                    new Button(langResources.getString("delete"), click -> {
                        userService.deleteUser(user);
                        deleteDialog.close();
                        securityService.logout();
                    }),
                    new Button(langResources.getString("close"), click -> deleteDialog.close()));
            deleteDialog.open();
        });
        VerticalLayout verticalLayout = new VerticalLayout(deleteAccount);
        verticalLayout.setAlignItems(Alignment.CENTER);
        return verticalLayout;
    }
}