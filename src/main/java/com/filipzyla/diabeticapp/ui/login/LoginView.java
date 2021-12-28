package com.filipzyla.diabeticapp.ui.login;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        loginForm.getElement().setAttribute("no-autofocus", "");
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new RouterLink("Register", RegisterView.class),
                new RouterLink("Forgot password", ForgotPasswordView.class));
        layout.setAlignItems(Alignment.START);

        setAlignItems(Alignment.CENTER);
        add(new H2("Welcome to my app"), loginForm, layout);

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