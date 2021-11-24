package com.filipzyla.diabeticapp.views;

import com.filipzyla.diabeticapp.CustomDateTimeFormatter;
import com.filipzyla.diabeticapp.charts.SugarLineChart;
import com.filipzyla.diabeticapp.insulin.Insulin;
import com.filipzyla.diabeticapp.insulin.InsulinRepository;
import com.filipzyla.diabeticapp.suger.Sugar;
import com.filipzyla.diabeticapp.suger.SugarRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("main")
public class MainUserView extends VerticalLayout {

    @Autowired
    private SugarRepository sugarRepository;
    @Autowired
    private InsulinRepository insulinRepository;

    private final VerticalLayout layoutLastSugar = new VerticalLayout(), layoutLastInsulin = new VerticalLayout();
    private final HorizontalLayout layoutButtons = new HorizontalLayout(), layoutLastMeasurements = new HorizontalLayout();
    private final TopMenuBar topMenuBar = new TopMenuBar();
    private Button buttonAddMeasurement;
    private Button buttonShowHistory;
    private H3 labelSugarMain, labelInsulinMain;
    private H5 labelSugar, labelTypeSug, labelTimeSug, labelInsulin, labelTypeIns, labelTimeIns;

    public MainUserView(SugarRepository sugarRepository, InsulinRepository insulinRepository) {
        this.sugarRepository = sugarRepository;
        this.insulinRepository = insulinRepository;

        addLastSugar();
        addLastInsulin();
        lowerButtons();

        layoutLastMeasurements.add(layoutLastSugar, layoutLastInsulin);
        layoutLastMeasurements.setSizeFull();

        SugarLineChart sugarLineChart = new SugarLineChart(sugarRepository);

        add(topMenuBar.getBarLayout(), layoutLastMeasurements, layoutButtons, sugarLineChart);
    }

    private void addLastSugar() {
        layoutLastSugar.setWidth("50%");
        layoutLastSugar.setAlignItems(Alignment.CENTER);
        Optional<Sugar> sugarOpt = Optional.ofNullable(sugarRepository.findFirstByOrderByTimeAsc());
        if (sugarOpt.isPresent()) {
            labelSugarMain = new H3("Last sugar");
            labelSugar = new H5(sugarOpt.get().getSugar().toString() + " " + sugarOpt.get().getUnits().getMsg());
            labelTypeSug = new H5(sugarOpt.get().getType().getMsg());
            labelTimeSug = new H5(sugarOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            layoutLastSugar.add(labelSugarMain, labelSugar, labelTypeSug, labelTimeSug);
        }
    }

    private void addLastInsulin() {
        layoutLastInsulin.setWidth("50%");
        layoutLastInsulin.setAlignItems(Alignment.CENTER);
        Optional<Insulin> insulinOpt = Optional.ofNullable(insulinRepository.findFirstByOrderByTimeAsc());
        if (insulinOpt.isPresent()) {
            labelInsulinMain = new H3("Last insulin");
            labelInsulin = new H5(insulinOpt.get().getInsulin().toString() + " units");
            labelTypeIns = new H5(insulinOpt.get().getType().getMsg());
            labelTimeIns = new H5(insulinOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            layoutLastInsulin.add(labelInsulinMain, labelInsulin, labelTypeIns, labelTimeIns);
        }
    }

    private void lowerButtons() {
        layoutButtons.setWidthFull();
        layoutButtons.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutButtons.setAlignSelf(Alignment.STRETCH);
        buttonAddMeasurement = new Button("Add new", new Icon(VaadinIcon.PLUS), event -> UI.getCurrent().navigate("add"));
        buttonShowHistory = new Button("History", new Icon(VaadinIcon.ARCHIVE), event -> UI.getCurrent().navigate("history"));
        layoutButtons.add(buttonAddMeasurement, buttonShowHistory);
    }
}
