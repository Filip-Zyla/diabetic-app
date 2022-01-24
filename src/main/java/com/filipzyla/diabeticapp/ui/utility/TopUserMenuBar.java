package com.filipzyla.diabeticapp.ui.utility;

import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.ui.user.HistoryView;
import com.filipzyla.diabeticapp.ui.user.HomeView;
import com.filipzyla.diabeticapp.ui.user.SettingsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Locale;
import java.util.ResourceBundle;

public class TopUserMenuBar extends HorizontalLayout {

    public TopUserMenuBar(SecurityService securityService) {
        final ResourceBundle langResources = ResourceBundle.getBundle("lang.res");

        Button buttonHomePage = new Button(langResources.getString("home"), new Icon(VaadinIcon.HOME), event -> UI.getCurrent().navigate(HomeView.class));
        Button buttonHistory = new Button(langResources.getString("history"), new Icon(VaadinIcon.ARCHIVE), event -> UI.getCurrent().navigate(HistoryView.class));
        Button buttonSetting = new Button(langResources.getString("settings"), new Icon(VaadinIcon.OPTIONS), event -> UI.getCurrent().navigate(SettingsView.class));
        Button buttonLogout = new Button(langResources.getString("logout"), new Icon(VaadinIcon.EXIT), event -> securityService.logout());
        buttonLogout.addThemeVariants(ButtonVariant.LUMO_ERROR);

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

        add(buttonHomePage, buttonHistory, buttonSetting, buttonEnglish, buttonPolish, buttonLogout);
    }
}