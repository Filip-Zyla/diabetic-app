package com.filipzyla.diabeticapp.views;

import com.filipzyla.diabeticapp.insulin.Insulin;
import com.filipzyla.diabeticapp.insulin.InsulinRepository;
import com.filipzyla.diabeticapp.suger.Sugar;
import com.filipzyla.diabeticapp.suger.SugarRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Route("main")
public class MainUserView extends VerticalLayout {

    public MainUserView(SugarRepository sugarRepository, InsulinRepository insulinRepository){
        VerticalLayout layoutLastSugar = new VerticalLayout();
        Optional<Sugar> sugarOpt = Optional.ofNullable(sugarRepository.findFirstByOrderByTimeAsc());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");

        if (sugarOpt.isPresent()) {
            Label labelSugarMain = new Label("Last sugar");
            Label labelSugar = new Label(sugarOpt.get().getSugar().toString() + " " + sugarOpt.get().getUnits().getMsg());
            Label labelTypeSug = new Label(sugarOpt.get().getType().getMsg());
            Label labelTimeSug = new Label(sugarOpt.get().getTime().format(formatter));
            Label labelNoteSug = new Label(sugarOpt.get().getNote());
            layoutLastSugar.add(labelSugarMain, labelSugar, labelTypeSug, labelTimeSug, labelNoteSug);
        }

        VerticalLayout layoutLastInsulin = new VerticalLayout();
        Optional<Insulin> insulinOpt = Optional.ofNullable(insulinRepository.findFirstByOrderByTimeAsc());
        if (insulinOpt.isPresent()) {
            Label labelInsulinMain = new Label("Last sugar");
            Label labelInsulin = new Label(insulinOpt.get().getInsulin().toString()+" units");
            Label labelTypeIns = new Label(insulinOpt.get().getType().getMsg());
            Label labelTimeIns = new Label(insulinOpt.get().getTime().format(formatter));
            Label labelNoteIns = new Label(insulinOpt.get().getNote());
            layoutLastInsulin.add(labelInsulinMain, labelInsulin, labelTypeIns, labelTimeIns, labelNoteIns);
        }

        HorizontalLayout layoutLastMeasurements = new HorizontalLayout();
        layoutLastMeasurements.add(layoutLastSugar, layoutLastInsulin);

        Button buttonAddMeasurement = new Button("Add new", event -> UI.getCurrent().navigate("add"));

        add(layoutLastMeasurements, buttonAddMeasurement);
    }
}
