package com.filipzyla.diabeticapp.ui.utility;

import com.filipzyla.diabeticapp.backend.enums.SugarType;
import com.filipzyla.diabeticapp.backend.models.Sugar;
import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.backend.service.UserService;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Annotations;
import com.github.appreciated.apexcharts.config.DiscretePoint;
import com.github.appreciated.apexcharts.config.Markers;
import com.github.appreciated.apexcharts.config.annotations.YAxisAnnotations;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.markers.Shape;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class Charts {

    private final User user;
    private final ResourceBundle langResources;

    public Charts(UserService userService, SecurityService securityService) {
        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");
    }

    public ApexCharts sugarCircleChart(final List<Sugar> sugars) {
        ApexCharts pieChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.pie).build())
                .withLabels(langResources.getString("hypoglycemia"),
                        langResources.getString("normal"),
                        langResources.getString("hyperglycemia"))
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.right)
                        .build())
                .withSeries(getSugarsStatistics(sugars))
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom)
                                        .build())
                                .build())
                        .build())
                .build();
        pieChart.setWidth("500px");
        pieChart.setHeight("400px");
        pieChart.setColors("#EA0E0E", "#0EEA22", "#EAE00E");
        return pieChart;

    }

    public Double[] getSugarsStatistics(final List<Sugar> sugars) {
        long hipo = sugars.stream()
                .filter(s -> s.getSugar() < user.getHypoglycemia()).count();
        long beforeHiper = sugars.stream()
                .filter(s -> s.getType() != SugarType.AFTER_MEAL)
                .filter(s -> s.getSugar() > user.getHyperglycemia()).count();
        long beforeNormal = sugars.stream()
                .filter(s -> s.getType() != SugarType.AFTER_MEAL)
                .filter(s -> s.getSugar() >= user.getHypoglycemia() && s.getSugar() <= user.getHyperglycemia()).count();
        long afterHiper = sugars.stream()
                .filter(s -> s.getType() == SugarType.AFTER_MEAL)
                .filter(s -> s.getSugar() > user.getHyperglycemiaAfterMeal()).count();
        long afterNormal = sugars.stream()
                .filter(s -> s.getType() == SugarType.AFTER_MEAL)
                .filter(s -> s.getSugar() > user.getHypoglycemia() && s.getSugar() < user.getHyperglycemiaAfterMeal()).count();

        return new Double[]{(double) hipo, (double) beforeNormal + afterNormal, (double) beforeHiper + afterHiper};
    }

    public ApexCharts sugarLineChart(final List<Sugar> sugars) {
        ApexCharts lineChart =
                ApexChartsBuilder.get()
                        .withChart(
                                ChartBuilder.get()
                                        .withType(Type.line)
                                        .withZoom(
                                                ZoomBuilder.get().withEnabled(false).build()
                                        ).build()
                        )
                        .withDataLabels(
                                DataLabelsBuilder.get().withEnabled(false).build()
                        )
                        .withStroke(
                                StrokeBuilder.get().withCurve(Curve.straight).build()
                        )
                        .withSeries(new Series(langResources.getString("sugar"), sugars.stream().map(Sugar::getSugar).toArray()))
                        .withLabels(sugars.stream().map(s -> s.getTime().toString()).toArray(String[]::new))
                        .withXaxis(
                                XAxisBuilder.get().withType(XAxisType.datetime).build()
                        )
                        .withYaxis(
                                YAxisBuilder.get().withMin(30).withMax(sugars.stream().mapToInt(Sugar::getSugar).max().getAsInt() + 30).withOpposite(true).build()
                        )
                        .withLegend(
                                LegendBuilder.get().withHorizontalAlign(HorizontalAlign.left).build()
                        ).build();
        lineChart.setWidth("1000px");
        lineChart.setHeight("400px");
        lineChart.setMarkers(getMarkers());
        lineChart.setAnnotations(getAnnotations());

        return lineChart;
    }

    private Annotations getAnnotations() {
        Annotations annotations = new Annotations();
        final YAxisAnnotations y = new YAxisAnnotations();
        y.setY(Double.valueOf(user.getHypoglycemia()));
        y.setY2(Double.valueOf(user.getHyperglycemiaAfterMeal()));
        y.setBorderColor("#2DF542");
        y.setFillColor("#2DF542");
        annotations.setYaxis(List.of(y));
        return annotations;
    }

    private Markers getMarkers() {
        Markers markers = new Markers();
        markers.setSize(new Double[]{5.0});
        markers.setStrokeColor("#B50EEA");
        markers.setColors(Collections.singletonList("#B50EEA"));
        markers.setStrokeWidth(2.0);
        markers.setStrokeOpacity(0.9);
        markers.setFillOpacity(0.0);
        markers.setDiscrete(new DiscretePoint[]{});
        markers.setShape(Shape.circle);
        markers.setRadius(2.0);
        markers.setOffsetX(0.0);
        markers.setOffsetY(0.0);
        return markers;
    }
}