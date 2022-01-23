package com.filipzyla.diabeticapp.ui.charts;

import com.filipzyla.diabeticapp.backend.models.Sugar;
import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.backend.service.InsulinService;
import com.filipzyla.diabeticapp.backend.service.SugarService;
import com.filipzyla.diabeticapp.backend.service.UserService;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;

import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class Charts {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;
    public final ResourceBundle langResources;

    public Charts(UserService userService, SecurityService securityService, SugarService sugarService, InsulinService insulinService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");
    }

    public ApexCharts sugarCircleChart(int lastNDays) {
        ApexCharts pieChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.pie).build())
                .withLabels("Hipo", "Normal", "Hiper")
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.right)
                        .build())
                .withSeries(getSugarsStates(lastNDays))
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
        return pieChart;

    }

    public Double[] getSugarsStates(int lastNDays) {
        final List<Sugar> sugars = sugarService.findAllOrderByTimeBetweenDates(user.getUserId(), LocalDate.now().minusDays(lastNDays), LocalDate.now().plusDays(1));
        long hipo = sugars.stream().filter(s -> s.getSugar() < user.getHypoglycemia()).count();
        long normal = sugars.stream().filter(s -> s.getSugar() > user.getHypoglycemia() && s.getSugar() < user.getHyperglycemiaAfterMeal()).count();
        long hiper = sugars.stream().filter(s -> s.getSugar() > user.getHyperglycemia()).count();

        return new Double[]{(double) hipo, (double) normal, (double) hiper};
    }

    public ApexCharts sugarLineChart(int lastNDays) {
        ApexCharts sugarChart =
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
                        .withSeries(getSugars(lastNDays))
                        .withLabels(getDates(lastNDays))
                        .withXaxis(
                                XAxisBuilder.get().withType(XAxisType.datetime).build()
                        )
                        .withYaxis(
                                YAxisBuilder.get().withMin(0).withMax(500).withOpposite(true).build()
                        )
                        .withLegend(
                                LegendBuilder.get().withHorizontalAlign(HorizontalAlign.left).build()
                        ).build();
        sugarChart.setWidth("1000px");
        sugarChart.setHeight("400px");

        return sugarChart;
    }

    private Series getSugars(int lastNDays) {
        final List<Sugar> sugars = sugarService.findAllOrderByTimeBetweenDates(user.getUserId(), LocalDate.now().minusDays(lastNDays), LocalDate.now().plusDays(1));
        Series series = new Series(sugars.stream().map(Sugar::getSugar).toArray());
        return series;
    }

    private String[] getDates(int lastNDays) {
        final List<Sugar> sugars = sugarService.findAllOrderByTimeBetweenDates(user.getUserId(), LocalDate.now().minusDays(lastNDays), LocalDate.now().plusDays(1));
        return sugars.stream().map(sugar -> sugar.getTime().toString()).toArray(String[]::new);
    }
}
