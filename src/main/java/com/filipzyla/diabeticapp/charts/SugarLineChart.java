package com.filipzyla.diabeticapp.charts;

import com.filipzyla.diabeticapp.suger.Sugar;
import com.filipzyla.diabeticapp.suger.SugarRepository;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class SugarLineChart extends Div {
    @Autowired
    private SugarRepository sugarRepository;

    public SugarLineChart(SugarRepository sugarRepository) {
        this.sugarRepository = sugarRepository;
        ApexCharts sugarChart =
                ApexChartsBuilder.get()
                        .withChart(
                                ChartBuilder.get()
                                        .withType(Type.line)
                                        .withZoom(
                                                ZoomBuilder.get()
                                                        .withEnabled(false)
                                                        .build()
                                        )
                                        .build()
                        )
                        .withDataLabels(
                                DataLabelsBuilder.get()
                                        .withEnabled(false)
                                        .build()
                        )
                        .withStroke(
                                StrokeBuilder.get()
                                        .withCurve(Curve.straight)
                                        .build()
                        )
                        .withSeries(getSugars(sugarRepository))
                        .withLabels(getDates(sugarRepository))
                        .withXaxis(
                                XAxisBuilder.get()
                                        .withType(XAxisType.datetime)
                                        .build()
                        )
                        .withYaxis(
                                YAxisBuilder.get()
                                        .withMin(0)
                                        .withMax(500)
                                        .withOpposite(true)
                                        .build()
                        )
                        .withLegend(
                                LegendBuilder.get()
                                        .withHorizontalAlign(HorizontalAlign.left)
                                        .build()
                        )
                        .build();
        setWidth("80%");
        add(sugarChart);

    }

    private Series getSugars(SugarRepository sugarRepository) {
        final List<Sugar> sugars = sugarRepository.findAllOrderByTimeBetweenDates(LocalDate.now().minusDays(14), LocalDate.now());
        Series series = new Series(sugars.stream().map(sugar -> sugar.getSugar()).toArray());
        return series;
    }

    private String[] getDates(SugarRepository sugarRepository) {
        final List<Sugar> sugars = sugarRepository.findAllOrderByTimeBetweenDates(LocalDate.now().minusDays(14), LocalDate.now());
        return sugars.stream().map(sugar -> sugar.getTime().toString()).toArray(String[]::new);
    }
}
