package com.filipzyla.diabeticapp.ui.login;

import com.filipzyla.diabeticapp.ui.utility.TopLoginBar;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.ResourceBundle;

@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();
    private final ResourceBundle langResources;

    public LoginView() {
        langResources = ResourceBundle.getBundle("lang.res");

        configLoginFrom();

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new RouterLink(langResources.getString("register"), RegistrationView.class),
                new RouterLink(langResources.getString("forgot_pass"), PasswordReminderView.class));
        layout.setAlignItems(Alignment.START);

        setAlignItems(Alignment.CENTER);
        add(new TopLoginBar(), new H2(langResources.getString("welcome")), loginForm, layout);
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

    private void configLoginFrom() {
        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle(langResources.getString("log_in"));
        i18nForm.setUsername(langResources.getString("username"));
        i18nForm.setPassword(langResources.getString("password"));
        i18nForm.setSubmit(langResources.getString("log_in"));
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle(langResources.getString("bad_login"));
        i18nErrorMessage.setMessage(langResources.getString("bad_login_msg"));
        i18n.setErrorMessage(i18nErrorMessage);

        loginForm.setI18n(i18n);
        loginForm.getElement().setAttribute("no-autofocus", "");
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);
    }
}