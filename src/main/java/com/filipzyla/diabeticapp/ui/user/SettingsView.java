package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.backend.service.UserService;
import com.filipzyla.diabeticapp.ui.components.TopMenuBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
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

    public SettingsView(SecurityService securityService, UserService userService) {
        user = userService.findByUsername(securityService.getAuthenticatedUser());
        TextField username = new TextField("Username");
        username.setValue(user.getUsername());
        PasswordField password = new PasswordField("Password");
        password.setValue(user.getPassword());
        EmailField email = new EmailField("Email");
        email.setValue(user.getEmail());
        NumberField hypoglycemia = new NumberField("Hypoglycemia level");
        hypoglycemia.setValue(user.getHypoglycemia());
        NumberField hyperglycemia = new NumberField("Hyperglycemia level");
        hyperglycemia.setValue(user.getHyperglycemia());
        NumberField hyperglycemiaAfterMeal = new NumberField("Hyperglycemia after meal level");
        hyperglycemiaAfterMeal.setValue(user.getHyperglycemiaAfterMeal());
        ComboBox<SugarUnits> sugarUnits = new ComboBox<>("Preferred units");
        sugarUnits.setItems(SugarUnits.values());
        sugarUnits.setItemLabelGenerator(SugarUnits::getMsg);
        sugarUnits.setValue(user.getUnits());
        Button saveButton = new Button("Save changes", event -> {
            if (!username.getValue().equals(user.getUsername()) && userService.validateUsername(username.getValue())) {
                Notification.show("Username already exist!").setPosition(Notification.Position.MIDDLE);
            }
            else if (!EmailValidator.getInstance().isValid(email.getValue())) {
                Notification.show("Wrong email!").setPosition(Notification.Position.MIDDLE);
            }
            else if (!email.getValue().equals(user.getEmail()) && userService.validateEmail(email.getValue())) {
                Notification.show("Email already exist!").setPosition(Notification.Position.MIDDLE);
            }
            else {
                user.setUsername(username.getValue());
                user.setPassword(password.getValue());
                user.setEmail(email.getValue());
                user.setHypoglycemia(hypoglycemia.getValue());
                user.setHyperglycemia(hyperglycemia.getValue());
                user.setHyperglycemiaAfterMeal(hyperglycemiaAfterMeal.getValue());
                user.setUnits(sugarUnits.getValue());
                userService.saveUser(user);
                Notification.show("Changes saved!").setPosition(Notification.Position.MIDDLE);
            }
        });

        setAlignItems(Alignment.CENTER);
        add(new TopMenuBar(securityService), new H2("Settings"),
                username, password, email, hypoglycemia, hyperglycemia, hyperglycemiaAfterMeal, sugarUnits, saveButton);
    }
}