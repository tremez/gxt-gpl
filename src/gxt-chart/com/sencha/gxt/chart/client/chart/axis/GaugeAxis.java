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

import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.series.GaugeSeries;
import com.sencha.gxt.chart.client.draw.path.ClosePath;
import com.sencha.gxt.chart.client.draw.path.LineTo;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.util.PrecisePoint;
import com.sencha.gxt.core.client.util.PreciseRectangle;

/**
 * {@link Axis} to be used with {@link GaugeSeries}.
 * 
 * @param <M> the data type of the axis
 */
public class GaugeAxis<M> extends Axis<M, Number> {

  private PrecisePoint center = new PrecisePoint();
  private double margin = 10;
  private String title;
  private double minimum = Double.NaN;
  private double maximum = Double.NaN;
  private int gaugeSteps = 10;

  /**
   * Creates a gauge {@link Axis}.
   */
  public GaugeAxis() {
    labelConfig.setTextAnchor(TextAnchor.MIDDLE);
  }

  @Override
  public void drawAxis(boolean init) {
    PreciseRectangle chartBBox = chart.getBBox();
    center.setX(chartBBox.getX() + (chartBBox.getWidth() / 2.0));
    center.setY(chartBBox.getY() + chartBBox.getHeight());
    double rho = Math.min(chartBBox.getWidth(), 2.0 * chartBBox.getHeight()) / 2.0 + margin;

    if (this.margin >= 0 && displayGrid) {
      final PathSprite line;
      if (lines.size() == 0) {
        line = axisConfig.copy();
        chart.addSprite(line);
        lines.add(line);
      } else {
        line = lines.get(0);
        line.clearCommands();
      }
      for (double i = 0; i <= gaugeSteps; i++) {
        double position = i / gaugeSteps * Math.PI - Math.PI;
        line.addCommand(new MoveTo(center.getX() + (rho - margin) * Math.cos(position), center.getY() + (rho - margin)
            * Math.sin(position)));
        line.addCommand(new LineTo(center.getX() + rho * Math.cos(position), center.getY() + rho * Math.sin(position)));
        line.addCommand(new ClosePath());
      }
      line.redraw();
    }
    drawLabels();
    if (title != null) {
      drawTitle();
    }
  }

  @Override
  public void drawLabels() {
    PreciseRectangle chartBBox = chart.getBBox();
    center.setX(chartBBox.getX() + (chartBBox.getWidth() / 2.0));
    center.setY(chartBBox.getY() + chartBBox.getHeight());
    double rho = Math.min(chartBBox.getWidth(), chartBBox.getHeight()) + 2.0 * margin;

    // draw scale
    for (double i = 0; i <= gaugeSteps; i++) {
      final TextSprite label;
      if (i >= labels.size()) {
        label = labelConfig.copy();
        chart.addSprite(label);
        labels.add(label);
      } else {
        label = labels.get((int) i);
      }
      label.setText(labelProvider.getLabel((Math.round(i / gaugeSteps * maximum))));
      label.redraw();
      double height = label.getBBox().getHeight() / 2.0;
      label.setX(center.getX() + rho * Math.cos(i / gaugeSteps * Math.PI - Math.PI));
      label.setY(center.getY() + rho * Math.sin(i / gaugeSteps * Math.PI - Math.PI) - height);
      label.redraw();
    }

  }

  /**
   * Returns the margins of the axis.
   * 
   * @return the margins of the axis
   */
  public double getMargin() {
    return margin;
  }

  /**
   * Returns the maximum value displayed on the axis.
   * 
   * @return the maximum value displayed on the axis
   */
  public double getMaximum() {
    return maximum;
  }

  /**
   * Returns the minimum value displayed on the axis.
   * 
   * @return the minimum value displayed on the axis
   */
  public double getMinimum() {
    return minimum;
  }

  @Override
  public Position getPosition() {
    return null;
  }

  /**
   * Returns the steps of the axis.
   * 
   * @return the steps of the axis
   */
  public int getSteps() {
    return gaugeSteps;
  }

  /**
   * Sets the margins of the axis.
   * 
   * @param margin the margins of the axis
   */
  public void setMargin(double margin) {
    this.margin = margin;
  }

  /**
   * Sets the maximum value displayed on the axis.
   * 
   * @param maximum the maximum value displayed on the axis
   */
  public void setMaximum(double maximum) {
    this.maximum = maximum;
  }

  /**
   * Sets the minimum value displayed on the axis
   * 
   * @param minimum the minimum value displayed on the axis
   */
  public void setMinimum(double minimum) {
    this.minimum = minimum;
  }

  /**
   * Sets the number of steps on the axis.
   * 
   * @param steps the number of steps on the axis
   */
  public void setSteps(int steps) {
    gaugeSteps = steps;
  }

  /**
   * Renders the title of the axis.
   */
  private void drawTitle() {
    PreciseRectangle bbox = chart.getBBox();
    if (titleSprite == null && titleConfig != null) {
      titleSprite = titleConfig.copy();
      chart.addSprite(titleSprite);
    }
    titleSprite.setText(title);

    PreciseRectangle titleBBox = titleSprite.getBBox();
    titleSprite.setX(bbox.getX() + (bbox.getWidth() / 2.0) - (titleBBox.getWidth() / 2.0));
    titleSprite.setY(bbox.getY() + bbox.getHeight() - (titleBBox.getHeight() / 2.0) - 4);

    titleSprite.redraw();
  }

}
