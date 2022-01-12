package com.filipzyla.diabeticapp.backend.security;

import com.filipzyla.diabeticapp.ui.login.ForgotPasswordView;
import com.filipzyla.diabeticapp.ui.login.LoginView;
import com.filipzyla.diabeticapp.ui.login.RegisterView;
import com.filipzyla.diabeticapp.ui.user.MainUserView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    private void beforeEnter(BeforeEnterEvent event) {
        if (RegisterView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(RegisterView.class);
        }
        else if (ForgotPasswordView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(ForgotPasswordView.class);
        }
        else if (!LoginView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
        else if (SecurityUtils.isUserLoggedIn() &&
                (RegisterView.class.equals(event.getNavigationTarget())
                        || ForgotPasswordView.class.equals(event.getNavigationTarget())
                        || LoginView.class.equals(event.getNavigationTarget()))
        ) {
            event.rerouteTo(MainUserView.class);
        }
    }
}