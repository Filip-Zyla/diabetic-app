package com.filipzyla.diabeticapp.backend.security;

import com.filipzyla.diabeticapp.ui.login.LoginView;
import com.filipzyla.diabeticapp.ui.login.PasswordReminderView;
import com.filipzyla.diabeticapp.ui.login.RegistrationView;
import com.filipzyla.diabeticapp.ui.user.HomeView;
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
        if (RegistrationView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(RegistrationView.class);
        }
        else if (PasswordReminderView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(PasswordReminderView.class);
        }
        else if (!LoginView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
        else if (SecurityUtils.isUserLoggedIn()
                && (RegistrationView.class.equals(event.getNavigationTarget())
                || PasswordReminderView.class.equals(event.getNavigationTarget())
                || LoginView.class.equals(event.getNavigationTarget()))
        ) {
            event.rerouteTo(HomeView.class);
        }
    }
}