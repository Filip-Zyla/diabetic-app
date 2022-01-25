package com.filipzyla.diabeticapp.ui.utility.defaultViews;

import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@ParentLayout(DefaultView.class)
public class CustomNotFoundRoute extends RouteNotFoundError {
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        getElement().setText("Route not found");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}