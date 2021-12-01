package com.filipzyla.diabeticapp.ui.login;

import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;
    private final LoginForm loginForm = new LoginForm();

    public LoginView(SecurityService securityService) {
        this.securityService = securityService;

        loginForm.getElement().setAttribute("no-autofocus", "");
        loginForm.setAction("login");
        setAlignItems(Alignment.CENTER);
        add(new H2("Welcome to my app"), loginForm, new RouterLink("Register", RegisterView.class));

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}