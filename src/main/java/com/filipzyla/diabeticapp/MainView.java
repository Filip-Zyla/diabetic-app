package com.filipzyla.diabeticapp;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
    public MainView(){
        Label labelMain = new Label("There should be login page");
        Button buttonUserMainPage = new Button("User main page", event -> UI.getCurrent().navigate("home"));

        add(labelMain, buttonUserMainPage);
    }

}
