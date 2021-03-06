package com.filipzyla.diabeticapp.ui.utility.defaultViews;

import com.filipzyla.diabeticapp.ui.user.HomeView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@Route()
public class DefaultView extends Div implements BeforeEnterObserver, RouterLayout {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.rerouteTo(HomeView.class);
    }
}