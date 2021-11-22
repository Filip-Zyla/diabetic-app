package com.filipzyla.diabeticapp.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TopMenuBar {
    private final HorizontalLayout barLayout;
    private final Button homePageButton, settingButton, logoutButton;

    public TopMenuBar() {
        homePageButton = new Button("Home", new Icon(VaadinIcon.HOME), event -> UI.getCurrent().navigate("main"));
        settingButton = new Button("Settings", new Icon(VaadinIcon.OPTIONS), event -> UI.getCurrent().navigate(""));
        logoutButton = new Button("Logout", new Icon(VaadinIcon.EXIT), event -> UI.getCurrent().navigate(""));
        barLayout = new HorizontalLayout();
        barLayout.setHeight(50, Unit.PIXELS);
        barLayout.setWidthFull();
        barLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        barLayout.setAlignSelf(FlexComponent.Alignment.STRETCH);
        barLayout.add(homePageButton, settingButton, logoutButton);
    }

    public HorizontalLayout getBarLayout() {
        return barLayout;
    }
}
