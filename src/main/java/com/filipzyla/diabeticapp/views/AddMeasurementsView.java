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

    ComboBox comboBoxMeasurementType = new ComboBox("What do you want to see?");

    NumberField numFieldSugar = new NumberField("Sugar");
    ComboBox comboBoxSugarType = new ComboBox("Type");
    ComboBox comboBoxSugarUnitsType = new ComboBox("Units");
    DateTimePicker dateTimePicker = new DateTimePicker("Time");
    Button buttonCommitSugar = new Button("Save", save -> saveSugar());

    NumberField numFieldInsulin = new NumberField("Insulin");
    ComboBox comboBoxInsulinType = new ComboBox("Type");
    Button buttonCommitInsulin = new Button("Save", save -> saveInsulin());

    public AddMeasurementsView() {
        comboBoxMeasurementType.setItems(MeasurementType.values());

        comboBoxMeasurementType.addValueChangeListener(event -> {
            remove(numFieldSugar, comboBoxSugarType, comboBoxSugarUnitsType, dateTimePicker,
                    buttonCommitSugar, numFieldInsulin, comboBoxInsulinType, buttonCommitInsulin);
            if (event.getValue() == MeasurementType.SUGAR) {
                numFieldSugar.setStep(1);
                comboBoxSugarType.setItems(SugarType.values());
                comboBoxSugarUnitsType.setItems(SugarUnits.values());
                dateTimePicker.setStep(Duration.ofMinutes(1));
                dateTimePicker.setValue(LocalDateTime.now());

                add(numFieldSugar, comboBoxSugarType, comboBoxSugarUnitsType, dateTimePicker, buttonCommitSugar);
            }
            else if (event.getValue() == MeasurementType.INSULIN) {
                numFieldInsulin.setStep(1);
                comboBoxInsulinType.setItems(InsulinType.values());
                dateTimePicker.setStep(Duration.ofMinutes(1));
                dateTimePicker.setValue(LocalDateTime.now());

                add(numFieldInsulin, comboBoxInsulinType, dateTimePicker, buttonCommitInsulin);
            }
        });

        add(new TopMenuBar().getBarLayout(), comboBoxMeasurementType);


    }

    private void saveSugar() {
        Sugar sugar = new Sugar(numFieldSugar.getValue(), (SugarType) comboBoxSugarType.getValue(),
                (SugarUnits) comboBoxSugarUnitsType.getValue(), dateTimePicker.getValue());
        sugarRepository.save(sugar);
        UI.getCurrent().getPage().reload();
    }

    private void saveInsulin() {
        Insulin insulin = new Insulin(numFieldInsulin.getValue().intValue(), (InsulinType) comboBoxInsulinType.getValue(),
                dateTimePicker.getValue());
        insulinRepository.save(insulin);
        UI.getCurrent().getPage().reload();
    }
}
