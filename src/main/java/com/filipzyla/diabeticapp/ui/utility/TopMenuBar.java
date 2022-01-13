package com.filipzyla.diabeticapp.ui.utility;

import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TopMenuBar extends HorizontalLayout {

    public TopMenuBar(SecurityService securityService) {
        Button buttonHomePage = new Button("Home", new Icon(VaadinIcon.HOME), event -> UI.getCurrent().navigate("home"));
        Button buttonHistory = new Button("History", new Icon(VaadinIcon.ARCHIVE), event -> UI.getCurrent().navigate("history"));
        Button buttonSetting = new Button("Settings", new Icon(VaadinIcon.OPTIONS), event -> UI.getCurrent().navigate("settings"));
        Button buttonLogout = new Button("Logout", new Icon(VaadinIcon.EXIT), event -> securityService.logout());

        setHeight(50, Unit.PIXELS);
        setWidthFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignSelf(FlexComponent.Alignment.STRETCH);

        add(buttonHomePage, buttonHistory, buttonSetting, buttonLogout);
    }
}