package com.filipzyla.diabeticapp.ui.utility;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Locale;
import java.util.ResourceBundle;

public class TopLoginBar extends HorizontalLayout {

    public TopLoginBar() {
        final ResourceBundle langResources = ResourceBundle.getBundle("lang.res");

        Button buttonHomePage = new Button(langResources.getString("home"), new Icon(VaadinIcon.HOME), event -> UI.getCurrent().navigate("login"));
        Button buttonEnglish = new Button(langResources.getString("switch_en"), event -> {
            Locale.setDefault(new Locale("EN"));
            UI.getCurrent().getPage().reload();
        });
        Button buttonPolish = new Button(langResources.getString("switch_pl"), event -> {
            Locale.setDefault(new Locale("PL"));
            UI.getCurrent().getPage().reload();
        });

        setHeight(50, Unit.PIXELS);
        setWidthFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignSelf(FlexComponent.Alignment.STRETCH);

        add(buttonHomePage, buttonEnglish, buttonPolish);
    }
}