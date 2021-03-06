package com.filipzyla.diabeticapp.ui.utility;

import com.filipzyla.diabeticapp.ui.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Locale;
import java.util.ResourceBundle;

public class TopLoginBar extends HorizontalLayout {

    public TopLoginBar() {
        final ResourceBundle langResources = ResourceBundle.getBundle("lang.res");

        Button buttonHomePage = new Button(langResources.getString("home"), new Icon(VaadinIcon.HOME), event -> UI.getCurrent().navigate(LoginView.class));
        Button buttonEnglish = new Button(langResources.getString("switch_en"), event -> {
            Locale.setDefault(new Locale("EN"));
            UI.getCurrent().getPage().reload();
        });
        buttonEnglish.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        Button buttonPolish = new Button(langResources.getString("switch_pl"), event -> {
            Locale.setDefault(new Locale("PL"));
            UI.getCurrent().getPage().reload();
        });
        buttonPolish.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);

        setHeight(50, Unit.PIXELS);
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignSelf(Alignment.STRETCH);

        add(buttonHomePage, buttonEnglish, buttonPolish);
    }
}