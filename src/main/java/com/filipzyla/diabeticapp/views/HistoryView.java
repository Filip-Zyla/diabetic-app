package com.filipzyla.diabeticapp.views;

import com.filipzyla.diabeticapp.CustomDateTimeFormatter;
import com.filipzyla.diabeticapp.enums.InsulinType;
import com.filipzyla.diabeticapp.enums.SugarType;
import com.filipzyla.diabeticapp.enums.SugarUnits;
import com.filipzyla.diabeticapp.insulin.Insulin;
import com.filipzyla.diabeticapp.insulin.InsulinRepository;
import com.filipzyla.diabeticapp.suger.Sugar;
import com.filipzyla.diabeticapp.suger.SugarRepository;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Route("history")
public class HistoryView extends VerticalLayout {

    @Autowired
    private SugarRepository sugarRepository;
    @Autowired
    private InsulinRepository insulinRepository;

    private final HorizontalLayout layoutTabs = new HorizontalLayout(), layoutDatePeriod = new HorizontalLayout();
    private final VerticalLayout layoutSugar = new VerticalLayout(), layoutInsulin = new VerticalLayout();
    private final TopMenuBar topMenuBar = new TopMenuBar();
    private DatePicker fromDate, toDate;
    private Grid<Sugar> gridSugar = new Grid(Sugar.class);
    private Grid<Insulin> gridInsulin = new Grid(Insulin.class);
    private H2 labelHistory;
    private H4 labelInsulin, labelSugar;
    private Dialog dialog;


    public HistoryView(SugarRepository sugarRepository, InsulinRepository insulinRepository) {
        this.insulinRepository = insulinRepository;
        this.sugarRepository = sugarRepository;

        labelHistory = new H2("History");
        datePeriodSelector();
        Button buttonShowHistory = new Button("Show", event -> refreshHistoryGrid());
        sugarGrid();
        insulinGrid();
        setAlignItems(Alignment.CENTER);
        add(topMenuBar.getBarLayout(), labelHistory, layoutDatePeriod, buttonShowHistory, layoutTabs);
    }

    private void refreshHistoryGrid() {
        layoutSugar.removeAll();
        layoutInsulin.removeAll();
        layoutTabs.removeAll();

        sugarGrid();
        insulinGrid();
    }

    private void datePeriodSelector() {
        fromDate = new DatePicker("From");
        fromDate.setValue(LocalDate.now().minusDays(14));
        toDate = new DatePicker("To");
        toDate.setValue(LocalDate.now());
        layoutDatePeriod.add(fromDate, toDate);
    }

    private void sugarGrid() {
        labelSugar = new H4("Sugar");

        gridSugar = new Grid(Sugar.class, false);
        gridSugar.addColumn(sugar -> sugar.getSugar() + " " + sugar.getUnits().getMsg()).setHeader("Sugar");
        gridSugar.addColumn(sugar -> sugar.getType().getMsg()).setHeader("Type");
        gridSugar.addColumn(sugar -> sugar.getTime().format(CustomDateTimeFormatter.formatter)).setHeader("Time");
        gridSugar.addColumn(
                new ComponentRenderer<>(Button::new, (button, sugar) -> {
                    button.addClickListener(e -> modifySugar(sugar));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader("Modify");


        List<Sugar> sugar = sugarRepository.findAllOrderByTimeBetweenDates(fromDate.getValue(), toDate.getValue().plusDays(1));
        gridSugar.setItems(sugar);
        gridSugar.setWidth(700, Unit.PIXELS);
        gridSugar.setHeight(500, Unit.PIXELS);
        layoutSugar.setAlignItems(Alignment.CENTER);
        layoutSugar.add(labelSugar, gridSugar);
        layoutTabs.add(layoutSugar);
    }

    private void modifySugar(Sugar sugar) {
        dialog = new Dialog();
        dialog.open();
        NumberField numField = new NumberField("Sugar");
        numField.setValue(sugar.getSugar());
        numField.setStep(1);
        ComboBox<SugarUnits> comboBoxUnits = new ComboBox("Units");
        comboBoxUnits.setItems(SugarUnits.values());
        comboBoxUnits.setItemLabelGenerator(SugarUnits::getMsg);
        comboBoxUnits.setValue(sugar.getUnits());
        ComboBox<SugarType> comboBoxType = new ComboBox("Type");
        comboBoxType.setItems(SugarType.values());
        comboBoxType.setItemLabelGenerator(SugarType::getMsg);
        comboBoxType.setValue(sugar.getType());
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setStep(Duration.ofMinutes(1));
        dateTimePicker.setValue(sugar.getTime());

        Button buttonCommit = new Button("Save", save -> {
            sugar.setSugar(numField.getValue());
            sugar.setUnits(comboBoxUnits.getValue());
            sugar.setType(comboBoxType.getValue());
            sugar.setTime(dateTimePicker.getValue());
            sugarRepository.save(sugar);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonDelete = new Button("Delete", save -> {
            sugarRepository.delete(sugar);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonClose = new Button("Close", save -> {
            dialog.close();
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(buttonCommit, buttonDelete, buttonClose);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(numField, comboBoxType, dateTimePicker);
        dialog.add(dialogLayout, buttonLayout);
        add(dialog);
    }

    private void insulinGrid() {
        labelInsulin = new H4("Insulin");

        gridInsulin = new Grid(Insulin.class, false);
        gridInsulin.addColumn(Insulin::getInsulin).setHeader("Units");
        gridInsulin.addColumn(insulin -> insulin.getType().getMsg()).setHeader("Type");
        gridInsulin.addColumn(insulin -> insulin.getTime().format(CustomDateTimeFormatter.formatter)).setHeader("Time");
        gridInsulin.addColumn(
                new ComponentRenderer<>(Button::new, (button, insulin) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON);
                    button.addClickListener(e -> modifyInsulin(insulin));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader("Modify");

        List<Insulin> insulins = insulinRepository.findAllOrderByTimeBetweenDates(fromDate.getValue(), toDate.getValue().plusDays(1));
        gridInsulin.setItems(insulins);
        gridInsulin.setWidth(700, Unit.PIXELS);
        gridInsulin.setHeight(500, Unit.PIXELS);
        layoutInsulin.setAlignItems(Alignment.CENTER);
        layoutInsulin.add(labelInsulin, gridInsulin);
        layoutTabs.add(layoutInsulin);
    }

    private void modifyInsulin(Insulin insulin) {
        dialog = new Dialog();
        dialog.open();
        NumberField numField = new NumberField("Insulin");
        numField.setValue(Double.valueOf(insulin.getInsulin()));
        numField.setStep(1);
        ComboBox<InsulinType> comboBox = new ComboBox("Type");
        comboBox.setItems(InsulinType.values());
        comboBox.setItemLabelGenerator(InsulinType::getMsg);
        comboBox.setValue(insulin.getType());
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setStep(Duration.ofMinutes(1));
        dateTimePicker.setValue(insulin.getTime());

        Button buttonCommit = new Button("Save", save -> {
            insulin.setInsulin(numField.getValue().intValue());
            insulin.setType(comboBox.getValue());
            insulin.setTime(dateTimePicker.getValue());
            insulinRepository.save(insulin);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonDelete = new Button("Delete", save -> {
            insulinRepository.delete(insulin);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonClose = new Button("Close", save -> {
            dialog.close();
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(buttonCommit, buttonDelete, buttonClose);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(numField, comboBox, dateTimePicker);
        dialog.add(dialogLayout, buttonLayout);
        add(dialog);
    }

    private void charts() {
    }
}