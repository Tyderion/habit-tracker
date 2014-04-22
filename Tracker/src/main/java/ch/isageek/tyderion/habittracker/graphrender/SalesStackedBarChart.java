/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.isageek.tyderion.habittracker.graphrender;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Sales demo bar chart.
 */
public class SalesStackedBarChart extends AbstractDemoChart {
    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Sales stacked bar chart";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The monthly sales for the last 2 years (stacked bar chart)";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */
    public GraphicalView getView(Context context, double[] maximum, double[] average, double[] minimum, double completeMax) {
        String[] titles = new String[]{"Maximum", "Average","Minimum"};
        List<double[]> values = new ArrayList<double[]>();
        values.add(maximum);
        values.add(average);
        values.add(minimum);
        int[] colors = new int[]{Color.CYAN, Color.BLUE, Color.GREEN};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        setChartSettings(renderer, "Daily Min/Max/Median Occurrences", "Weekday", "No. Occurrences", 0.5,
                7.5, 0, ((int)completeMax)+1, Color.GRAY, Color.LTGRAY);
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
        renderer.getSeriesRendererAt(2).setDisplayChartValues(true);
//        renderer.getSeriesRendererAt(0).setDisplayBoundingPoints(false);
//        renderer.getSeriesRendererAt(1).setDisplayBoundingPoints(false);
        renderer.setXLabels(7);
        renderer.setXLabels(0);
        renderer.addXTextLabel(1, "Mon");
        renderer.addXTextLabel(2, "Tue");
        renderer.addXTextLabel(3, "Wen");
        renderer.addXTextLabel(4, "Thu");
        renderer.addXTextLabel(5, "Fri");
        renderer.addXTextLabel(6, "Sat");
        renderer.addXTextLabel(7, "Sun");
        renderer.setYLabels(10);
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setPanEnabled(true, true);
        renderer.setPanLimits(new double[]{0.5,7.5,0,completeMax+1});
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);
        return  ChartFactory.getBarChartView(context,buildBarDataset(titles, values) ,renderer,
                Type.STACKED);
    }

    @Override
    public Intent execute(Context context) {
        return null;
    }
}
