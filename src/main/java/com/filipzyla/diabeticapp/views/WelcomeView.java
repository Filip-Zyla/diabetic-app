package com.filipzyla.diabeticapp.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class WelcomeView extends VerticalLayout {
    public WelcomeView() {
        H2 labelWelcome = new H2("Welcome to my app");
        Button buttonUserMainPage = new Button("User main page", event -> UI.getCurrent().navigate("main"));
        setAlignItems(Alignment.CENTER);

        add(labelWelcome, buttonUserMainPage);
    }
}