package com.filipzyla.diabeticapp.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        Button buttonUserMainPage = new Button("User main page", event -> UI.getCurrent().navigate("home"));
        setAlignItems(Alignment.CENTER);
        add(new H2("Welcome to my app"), buttonUserMainPage);
    }
}