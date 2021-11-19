package com.filipzyla.diabeticapp.views;

import com.filipzyla.diabeticapp.insulin.Insulin;
import com.filipzyla.diabeticapp.insulin.InsulinRepository;
import com.filipzyla.diabeticapp.suger.Sugar;
import com.filipzyla.diabeticapp.suger.SugarRepository;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("history")
public class HistoryView extends VerticalLayout {
    public HistoryView(SugarRepository sugarRepository, InsulinRepository insulinRepository) {
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("What do you want to see?");
        radioGroup.setItems("All", "Sugar", "Insulin");
        radioGroup.setValue("All");
        radioGroup.setReadOnly(true);
        add(radioGroup);

        radioGroup.addValueChangeListener(event -> {
            if (radioGroup.getValue().equals("All")) {
                final List<Sugar> allSugar = sugarRepository.findAll();
                final List<Insulin> allInsulin = insulinRepository.findAll();

            }
            else if (radioGroup.getValue().equals("Sugar")) {
                final List<Sugar> allSugar = sugarRepository.findAll();

            }
            else if (radioGroup.getValue().equals("Insulin")) {
                final List<Insulin> allInsulin = insulinRepository.findAll();

            }
        });

//        remove(grid);
//        grid.addColumn(Sugar::getSugar).setHeader("Value");
//        grid.addColumn(Sugar::getType).setHeader("Type");
//        grid.addColumn(Sugar::getTime).setHeader("Time");
//        grid.setItems(allSugar);
//        add(grid);

        add(new TopMenuBar().getBarLayout(), radioGroup);
    }
}
