/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.chart.client.chart.axis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sencha.gxt.chart.client.chart.RoundNumberProvider;
import com.sencha.gxt.chart.client.chart.series.AreaSeries;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.Series;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;

/**
 * An axis to handle numeric values. This axis is used for quantitative data as
 * opposed to the category axis. You can set minimum and maximum values to the
 * axis so that the values are bound to that. If no values are set, then the
 * scale will auto-adjust to the values.
 * 
 * @param <M> the data type that the axis uses
 */
public class NumericAxis<M> extends CartesianAxis<M, Number> {

  private List<ValueProvider<? super M, ? extends Number>> fields = new ArrayList<ValueProvider<? super M, ? extends Number>>();
  protected double minimum = Double.NaN;
  protected double maximum = Double.NaN;
  protected boolean adjustMaximumByMajorUnit = false;
  protected boolean adjustMinimumByMajorUnit = false;
  protected double step = Double.NaN;
  protected int stepsMax = -1;
  protected double interval = -1;

  /**
   * Creates a numeric axis.
   */
  public NumericAxis() {
    labelProvider = new RoundNumberProvider<Number>();
  }

  /**
   * Adds a {@link ValueProvider} to provide data to a field of the axis.
   * 
   * @param field the value provider
   */
  public void addField(ValueProvider<? super M, ? extends Number> field) {
    fields.add(field);
  }

  /**
   * Calculate the start, end and step points.
   */
  public void calcEnds() {
    double min = Double.isNaN(minimum) ? Double.POSITIVE_INFINITY : minimum;
    double max = Double.isNaN(maximum) ? Double.NEGATIVE_INFINITY : maximum;

    ListStore<M> store = chart.getCurrentStore();
    List<Series<M>> series = chart.getSeries();
    Set<Integer> excluded = null;

    if (fields.size() == 0) {
      this.from = 0;
      this.to = 0;
      this.step = 0;
      this.steps = 1;
      return;
    }

    boolean hasBarSeries = false;
    boolean hasAreaSeries = false;
    for (int i = 0; i < series.size(); i++) {
      if (series.get(i) instanceof BarSeries) {
        hasBarSeries = true;
      }

      if (series.get(i) instanceof AreaSeries) {
        hasAreaSeries = true;
      }

      if (series.get(i) instanceof BarSeries && ((BarSeries<?>) series.get(i)).isStacked()
          && ((BarSeries<?>) series.get(i)).getYAxisPosition() == this.position) {
        excluded = ((BarSeries<?>) series.get(i)).getExcluded();
        break;
      } else if (series.get(i) instanceof AreaSeries
          && ((AreaSeries<?>) series.get(i)).getYAxisPosition() == this.position) {
        excluded = ((AreaSeries<?>) series.get(i)).getExcluded();
        break;
      }
    }

    if (excluded != null && excluded.size() != fields.size()) {
      for (int i = 0; i < store.size(); i++) {
        double value = 0;
        if (Double.isInfinite(min)) {
          min = 0;
        }
        for (int j = 0; j < fields.size(); j++) {
          if (excluded.contains(j)) {
            continue;
          }
          Number fieldValue = fields.get(j).getValue(store.get(i));
          if (fieldValue != null && !Double.isNaN(fieldValue.doubleValue())) {
            value += fieldValue.doubleValue();
          }
        }
        max = Math.max(max, value);
        min = Math.min(min, value);
      }
    } else {
      for (int itemIndex = 0; itemIndex < store.size(); itemIndex++) {
        M item = store.get(itemIndex);
        for (int fieldIndex = 0; fieldIndex < fields.size(); fieldIndex++) {
          ValueProvider<? super M, ? extends Number> field = fields.get(fieldIndex);
          Number object = field.getValue(item);
          if (object != null) {
            double value = object.doubleValue();
            if (!Double.isNaN(value)) {
              max = Math.max(max, value);
              min = Math.min(min, value);
            }
          }
        }
      }
    }

    if (!Double.isNaN(maximum)) {
      max = Math.min(max, maximum);
    }

    if (!Double.isNaN(minimum)) {
      min = Math.max(min, minimum);
    }

    if (min == max) {
      if (max == 0) {
        max = 1;
      }
      this.from = 0;
      this.to = max;
      this.step = max;
      this.steps = 1;
    } else {
      // if either setMaximum or setMinimum were invoked,
      if (!Double.isNaN(maximum) || !Double.isNaN(minimum)) {
        // don't snap at all, just make it fit and trust user settings
        this.from = min;
        this.to = max;
        if (stepsMax >= 0) {
          this.steps = stepsMax;
          this.step = (to - from) / steps;
        } else if (interval >= 0) {
          this.step = interval;
          this.steps = (int) ((to - from) / step + 1);
        } else {
          //select a sane default
          this.steps = 10;
          this.step = (to - from) / steps;
        }
      } else {
        // auto set the base to 0 for bar and area series
        if (hasBarSeries || hasAreaSeries) {
          min = min >= 0 && max >= 0 ? 0 : min;
          max = min <= 0 && max <= 0 ? 0 : max;
        }

        if (stepsMax >= 0) { // manually setting the number of steps, try to respect that
          snapEnds(min, max, stepsMax);
        } else if (interval >= 0) {// solve for number of steps, based on given interval
          snapEnds(min, max, (max - min) / interval + 1);
        } else {// everyone else
          snapEnds(min, max, 10);
        }

      }

      if (this.adjustMaximumByMajorUnit) {
        this.to = Math.ceil(this.to / this.step) * this.step;
        this.steps = (int) ((this.to - this.from) / this.step);
      }

      if (this.adjustMinimumByMajorUnit) {
        this.from = Math.floor(this.from / this.step) * this.step;
        this.steps = (int) ((this.to - this.from) / this.step);
      }
    }
  }

  public List<ValueProvider<? super M, ? extends Number>> getFields() {
    return fields;
  }

  /**
   * Returns the manually set interval between tick marks.
   * 
   * @return the manually set interval between tick marks
   */
  public double getInterval() {
    return interval;
  }

  /**
   * Returns the maximum value of the axis.
   * 
   * @return the maximum value of the axis
   */
  public double getMaximum() {
    return maximum;
  }

  /**
   * Returns the minimum value of the axis.
   * 
   * @return the minimum value of the axis
   */
  public double getMinimum() {
    return minimum;
  }

  /**
   * Returns the steps of the axis.
   * 
   * @return the steps of the axis
   */
  public int getSteps() {
    return stepsMax;
  }

  /**
   * Returns true if the axis adjusts the minimum.
   * 
   * @return true if the axis adjusts the minimum
   */
  public boolean isAdjustMaximumByMajorUnit() {
    return adjustMaximumByMajorUnit;
  }

  /**
   * Returns true if the axis adjusts the maximum.
   * 
   * @return true if the axis adjusts the maximum
   */
  public boolean isAdjustMinimumByMajorUnit() {
    return adjustMinimumByMajorUnit;
  }

  public void removeField(ValueProvider<? super M, ? extends Number> field) {
    fields.remove(field);
  }

  /**
   * Sets true if the axis adjusts the maximum.
   * 
   * @param adjustMaximumByMajorUnit true if the axis adjusts the maximum
   */
  public void setAdjustMaximumByMajorUnit(boolean adjustMaximumByMajorUnit) {
    this.adjustMaximumByMajorUnit = adjustMaximumByMajorUnit;
  }

  /**
   * Sets true if the axis adjusts the minimum.
   * 
   * @param adjustMinimumByMajorUnit true if the axis adjusts the minimum
   */
  public void setAdjustMinimumByMajorUnit(boolean adjustMinimumByMajorUnit) {
    this.adjustMinimumByMajorUnit = adjustMinimumByMajorUnit;
  }

  public void setFields(List<ValueProvider<? super M, ? extends Number>> fields) {
    this.fields = fields;
  }

  /**
   * Sets the interval between tick marks. Is overridden if the number of steps is also set. If no maximum or minimum
   * is set, this will be treated as a suggestion, not a requirement.
   * 
   * @param interval the interval between tick marks
   */
  public void setInterval(double interval) {
    this.interval = interval;
  }

  /**
   * Sets the maximum value of the axis. When the axis has either a minimum or maximum value set, the bounds of the 
   * axis will not be automatically adjusted.
   * 
   * @param maximum the maximum value of the axis, or NaN to unset this property
   */
  public void setMaximum(double maximum) {
    this.maximum = maximum;
  }

  /**
   * Sets the minimum value of the axis. When the axis has either a minimum or maximum value set, the bounds of the 
   * axis will not be automatically adjusted.
   * 
   * @param minimum the minimum value of the axis, or NaN to unset this property
   */
  public void setMinimum(double minimum) {
    this.minimum = minimum;
  }

  /**
   * Sets the number of steps on the axis. If no maximum or minimum are set, this will be treated as a suggestion, not
   * a requirement. 
   * 
   * @param steps the number of steps on the axis
   */
  public void setSteps(int steps) {
    this.stepsMax = steps;
  }

  @Override
  protected void applyData() {
    calcEnds();
  }

  @Override
  protected void createLabels() {
    labelNames.clear();
    labelNames.add(from);
    for (int i = 0; i < ticks.size() - 2; i++) {
      labelNames.add(labelNames.get(labelNames.size() - 1).doubleValue() + step);
    }
    labelNames.add(to);
  }

  /**
   * Snaps the from, to and step points of the axis.
   * 
   * @param from the starting value of the axis
   * @param to the ending value of the axis
   * @param stepsMax maximum number of steps on the axis
   */
  private void snapEnds(double from, double to, double stepsMax) {
    double step = (to - from) / stepsMax;           // step size given parameters
    double level = Math.floor(Math.log10(step)) + 1;// order of magnitude of step
    double m = Math.pow(10, level);                 // nearest 10xxx above stepsMax
    double cur = from = Math.floor(from / m) * m;   // move from down to a 'pretty' number
    double modulo = Math.round((step % m) * Math.pow(10, 2 - level));
    double[][] intervals = {{0,15}, {20,4}, {30,2}, {40,4}, {50,9}, {60,4}, {70,2}, {80,4}, {100,15}};
    double stepsWeight = Double.POSITIVE_INFINITY, stepsVal = Double.NaN;
    for (int i = intervals.length - 1; i >= 0; i--) {
      double[] vals = intervals[i];
      double b = (vals[0] - modulo) / vals[1];
      if (b >= 0) {
        if (stepsWeight > b) {
          stepsWeight = b;
          stepsVal = vals[0];
        }
      } else {
        break;
      }
    }
    assert stepsVal != Double.NaN : "No interval value found - verify modulo is not negative or zero";
    stepsVal = Math.floor(step * Math.pow(10, -level)) * Math.pow(10, level) + stepsVal * Math.pow(10, level - 2);
    int stepCount = 0;
    while (cur < to) {
      cur += stepsVal;
      stepCount++;
    }
    to = cur;

    this.from = from;
    this.to = to;
    this.step = stepsVal;
    this.steps = stepCount;
  }

}