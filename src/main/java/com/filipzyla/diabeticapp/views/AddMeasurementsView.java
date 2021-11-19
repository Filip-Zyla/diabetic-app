package com.filipzyla.diabeticapp.views;

import com.filipzyla.diabeticapp.Enums.InsulinType;
import com.filipzyla.diabeticapp.Enums.MeasurementType;
import com.filipzyla.diabeticapp.Enums.SugarType;
import com.filipzyla.diabeticapp.Enums.SugarUnits;
import com.filipzyla.diabeticapp.insulin.Insulin;
import com.filipzyla.diabeticapp.insulin.InsulinRepository;
import com.filipzyla.diabeticapp.suger.Sugar;
import com.filipzyla.diabeticapp.suger.SugarRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

@Route("add")
public class AddMeasurementsView extends VerticalLayout {

    @Autowired
    private SugarRepository sugarRepository;
    @Autowired
    private InsulinRepository insulinRepository;

    private final ComboBox<MeasurementType> comboBoxMeasurementType;
    private final NumberField numFieldSugar;
    private final ComboBox<SugarType> comboBoxSugarType;
    private final ComboBox<SugarUnits> comboBoxSugarUnitsType;
    private final DateTimePicker dateTimePicker;
    private final Button buttonCommitSugar;
    private final NumberField numFieldInsulin;
    private final ComboBox<InsulinType> comboBoxInsulinType;
    private final Button buttonCommitInsulin;
    private final TopMenuBar topMenuBar = new TopMenuBar();

    public AddMeasurementsView() {
        comboBoxMeasurementType = new ComboBox("What do you want to add?");
        comboBoxMeasurementType.setItems(MeasurementType.values());

        dateTimePicker = new DateTimePicker("Time");

        numFieldSugar = new NumberField("Sugar");
        comboBoxSugarType = new ComboBox("Type");
        comboBoxSugarUnitsType = new ComboBox("Units");
        buttonCommitSugar = new Button("Save", save -> saveSugar());

        numFieldInsulin = new NumberField("Insulin");
        comboBoxInsulinType = new ComboBox("Type");
        buttonCommitInsulin = new Button("Save", save -> saveInsulin());

        comboBoxMeasurementType.addValueChangeListener(event -> {
            remove(numFieldSugar, comboBoxSugarType, comboBoxSugarUnitsType, dateTimePicker,
                    buttonCommitSugar, numFieldInsulin, comboBoxInsulinType, buttonCommitInsulin);
            if (event.getValue() == MeasurementType.SUGAR) {
                numFieldSugar.setStep(1);
                comboBoxSugarType.setItems(SugarType.values());
                comboBoxSugarType.setItemLabelGenerator(SugarType::getMsg);
                comboBoxSugarUnitsType.setItems(SugarUnits.values());
                comboBoxSugarUnitsType.setItemLabelGenerator(SugarUnits::getMsg);
                dateTimePicker.setStep(Duration.ofMinutes(1));
                dateTimePicker.setValue(LocalDateTime.now());

                add(numFieldSugar, comboBoxSugarType, comboBoxSugarUnitsType, dateTimePicker, buttonCommitSugar);
            }
            else if (event.getValue() == MeasurementType.INSULIN) {
                numFieldInsulin.setStep(1);
                comboBoxInsulinType.setItems(InsulinType.values());
                comboBoxInsulinType.setItemLabelGenerator(InsulinType::getMsg);
                dateTimePicker.setStep(Duration.ofMinutes(1));
                dateTimePicker.setValue(LocalDateTime.now());

                add(numFieldInsulin, comboBoxInsulinType, dateTimePicker, buttonCommitInsulin);
            }
        });

        setAlignItems(Alignment.CENTER);
        add(topMenuBar.getBarLayout(), comboBoxMeasurementType);


    }

    private void saveSugar() {
        Sugar sugar = new Sugar(numFieldSugar.getValue(), comboBoxSugarType.getValue(), comboBoxSugarUnitsType.getValue(), dateTimePicker.getValue());
        sugarRepository.save(sugar);
        UI.getCurrent().getPage().reload();
    }

    private void saveInsulin() {
        Insulin insulin = new Insulin(numFieldInsulin.getValue().intValue(), comboBoxInsulinType.getValue(), dateTimePicker.getValue());
        insulinRepository.save(insulin);
        UI.getCurrent().getPage().reload();
    }
}