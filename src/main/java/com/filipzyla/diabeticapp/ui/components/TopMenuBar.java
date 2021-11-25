package com.filipzyla.diabeticapp.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TopMenuBar extends HorizontalLayout {

    public TopMenuBar() {
        Button buttonHomePage = new Button("Home", new Icon(VaadinIcon.HOME), event -> UI.getCurrent().navigate("home"));
        Button buttonSetting = new Button("Settings", new Icon(VaadinIcon.OPTIONS), event -> UI.getCurrent().navigate(""));
        Button buttonLogout = new Button("Logout", new Icon(VaadinIcon.EXIT), event -> UI.getCurrent().navigate(""));

        setHeight(50, Unit.PIXELS);
        setWidthFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignSelf(FlexComponent.Alignment.STRETCH);

        add(buttonHomePage, buttonSetting, buttonLogout);
    }
}