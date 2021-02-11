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
package com.sencha.gxt.chart.client.chart.series;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.Axis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.SpriteList;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.PrecisePoint;
import com.sencha.gxt.core.client.util.PreciseRectangle;
import com.sencha.gxt.data.shared.ListStore;

/**
 * Creates a Scatter Chart. The scatter plot is useful when trying to display
 * more than two variables in the same visualization. These variables can be
 * mapped into x, y coordinates and also to an element's radius/size, color,
 * etc.
 * 
 * Here is an example scatter configuration:
 * 
 * <pre>
    ScatterSeries<Data> series = new ScatterSeries<Data>();
    series.setYAxisPosition(Position.LEFT);
    series.setYField(dataAccess.data1());
    Sprite marker = Primitives.cross(0, 0, 8);
    marker.setFill(RGB.GREEN);
    series.setMarkerConfig(marker);
    chart.addSeries(series);
 * </pre>
 * 
 * First the series is created with its associated data type. The y-axis
 * position is set to tell the series the scale of the displayed axis. Otherwise
 * the series will use its own internal scale. Next the value provider field is
 * set, which provides the value of each point. A sprite is added that
 * determines the representation of each point in the scatter. Finally the
 * series is added to the chart where it will be displayed.
 * 
 * @param <M> the data type used by this series
 */
public class ScatterSeries<M> extends Series<M> {

  // The display style for the scatter series markers.
  protected Sprite markerConfig;
  protected double selectionTolerance = 20;

  protected ValueProvider<? super M, ? extends Number> yField;
  private String title;

  protected PrecisePoint[] coordinates;
  protected boolean hidden = false;

  protected Position yAxisPosition;
  protected Position xAxisPosition;
  protected PrecisePoint min = new PrecisePoint();
  protected PrecisePoint max = new PrecisePoint();
  protected PrecisePoint scale = new PrecisePoint();

  /**
   * Creates a scatter {@link Series}.
   */
  public ScatterSeries() {
    // setup shadow attributes
    Sprite config = new PathSprite();
    config.setStrokeWidth(6);
    config.setStrokeOpacity(0.05);
    config.setStroke(RGB.BLACK);
    config.setFill(Color.NONE);
    config.setZIndex(9);
    shadowAttributes.add(config);
    config = new PathSprite();
    config.setStrokeWidth(4);
    config.setStrokeOpacity(0.1);
    config.setStroke(RGB.BLACK);
    config.setFill(Color.NONE);
    config.setZIndex(9);
    shadowAttributes.add(config);
    config = new PathSprite();
    config.setStrokeWidth(2);
    config.setStrokeOpacity(0.15);
    config.setStroke(RGB.BLACK);
    config.setFill(Color.NONE);
    config.setZIndex(9);
    shadowAttributes.add(config);

    // initialize the shadow groups
    if (shadowGroups.size() == 0) {
      for (int i = 0; i < shadowAttributes.size(); i++) {
        shadowGroups.add(new SpriteList<Sprite>());
      }
    }

    setHighlighter(new ScatterHighlighter());

    legendTitles.add("");

    CircleSprite circle = new CircleSprite();
    circle.setRadius(8);
    circle.setZIndex(11);
    markerConfig = circle;
  }

  public void calculateBounds() {
    PreciseRectangle chartBBox = chart.getBBox();
    ListStore<M> store = chart.getCurrentStore();

    scale = new PrecisePoint();

    bbox.setX(chartBBox.getX() + chart.getMaxGutter()[0]);
    bbox.setY(chartBBox.getY() + chart.getMaxGutter()[1]);
    bbox.setWidth(chartBBox.getWidth() - (chart.getMaxGutter()[0] * 2));
    bbox.setHeight(chartBBox.getHeight() - (chart.getMaxGutter()[1] * 2));

    coordinates = new PrecisePoint[store.size()];

    Axis<M, ?> axis = chart.getAxis(yAxisPosition);
    if (axis != null) {
      if (axis.getPosition() == Position.TOP || axis.getPosition() == Position.BOTTOM) {
        min.setX(axis.getFrom());
        max.setX(axis.getTo());
      } else {
        min.setY(axis.getFrom());
        max.setY(axis.getTo());
      }
    } else if (yField != null) {
      NumericAxis<M> numAxis = new NumericAxis<M>();
      numAxis.setChart(chart);
      numAxis.addField(yField);
      numAxis.calcEnds();
      min.setY(numAxis.getFrom());
      max.setY(numAxis.getTo());
    } else {
      min.setY(Double.NaN);
      max.setY(Double.NaN);
    }
    axis = chart.getAxis(xAxisPosition);
    if (axis != null) {
      if (axis.getPosition() == Position.TOP || axis.getPosition() == Position.BOTTOM) {
        min.setX(axis.getFrom());
        max.setX(axis.getTo());
      } else {
        min.setY(axis.getFrom());
        max.setY(axis.getTo());
      }
    } else if (xField != null) {
      NumericAxis<M> numAxis = new NumericAxis<M>();
      numAxis.setChart(chart);
      numAxis.addField(xField);
      numAxis.calcEnds();
      min.setX(numAxis.getFrom());
      max.setX(numAxis.getTo());
    } else {
      min.setX(Double.NaN);
      max.setX(Double.NaN);
    }

    if (Double.isNaN(min.getX())) {
      min.setX(0);
      // For one series data point
      if (store.size() == 1) {
        scale.setX(bbox.getWidth());
      } else {
        scale.setX(bbox.getWidth() / (store.size() - 1));
      }
    } else {
      scale.setX(bbox.getWidth() / (max.getX() - min.getX()));
    }
    if (Double.isNaN(min.getY())) {
      min.setY(0);
      scale.setY(bbox.getHeight() / (store.size() - 1));
    } else {
      scale.setY(bbox.getHeight() / (max.getY() - min.getY()));
    }

    for (int i = 0; i < store.size(); i++) {
      M model = store.get(i);
      final double xValue;
      final double yValue;
      // Ensure a value
      if (xField == null) {
        xValue = i;
      } else if (xField.getValue(model) != null) {
        xValue = xField.getValue(model).doubleValue();
        if (Double.isNaN(xValue)) {
          continue;
        }
      } else {
        continue;
      }

      if (yField == null) {
        yValue = i;
      } else if (yField.getValue(model) != null) {
        yValue = yField.getValue(model).doubleValue();
        if (Double.isNaN(yValue)) {
          continue;
        }
      } else {
        continue;
      }

      double x = bbox.getX() + (xValue - min.getX()) * scale.getX();
      double y = bbox.getY() + bbox.getHeight() - (yValue - min.getY()) * scale.getY();
      assert (!Double.isNaN(x) && !Double.isNaN(y)) : x + ", " + y;
      coordinates[i] = new PrecisePoint(x, y);
    }

    if (this instanceof LineSeries && coordinates.length > bbox.getWidth()) {
      coordinates = shrink(bbox.getWidth());
    }
  }

  @Override
  public void drawSeries() {
    ListStore<M> store = chart.getCurrentStore();

    // if the store is empty then there's nothing to be rendered
    if (store == null || store.size() == 0) {
      this.clear();
      return;
    }

    calculateBounds();

    drawMarkers();
    drawLabels();
  }

  /**
   * Returns the marker configuration.
   * 
   * @return the marker configuration
   */
  public Sprite getMarkerConfig() {
    return markerConfig;
  }

  /**
   * Returns the selection tolerance of markers.
   * 
   * @return the selection tolerance of markers
   */
  public double getSelectionTolerance() {
    return selectionTolerance;
  }

  /**
   * Returns the series title used in the legend.
   * 
   * @return the series title used in the legend
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the x axis position of the series.
   * 
   * @return the x axis position of the series
   */
  public Position getXAxisPosition() {
    return xAxisPosition;
  }

  /**
   * Returns the y axis position of the series.
   * 
   * @return the y axis position of the series
   */
  public Position getYAxisPosition() {
    return yAxisPosition;
  }

  /**
   * Returns the {@link ValueProvider} used for the y axis of the series.
   * 
   * @return the value provider used for the y axis of the series
   */
  public ValueProvider<? super M, ? extends Number> getYField() {
    return yField;
  }

  @Override
  public void hide(int yFieldIndex) {
    toggle(true);
  }

  @Override
  public void highlight(int yFieldIndex) {
    Sprite sprite = sprites.get(yFieldIndex);
    if (sprite != null && highlighter != null) {
      highlighter.highlight(sprite);
    }
  }

  @Override
  public void highlightAll(int index) {
    for (int i = 0; i < sprites.size(); i++) {
      highlighter.highlight(sprites.get(i));
    }
  }

  /**
   * Set the series title used in the legend.
   * 
   * @param title the series title used in the legend
   */
  public void setLegendTitle(String title) {
    if (title != null) {
      legendTitles.set(0, title);
    } else {
      legendTitles.set(0, getValueProviderName(yField, 0));
    }
    this.title = title;
    if (chart != null) {
      Legend<M> legend = chart.getLegend();
      if (legend != null) {
        legend.create();
        legend.updatePosition();
      }
    }
  }

  /**
   * Sets the marker configuration.
   * 
   * @param markerConfig the marker configuration
   */
  public void setMarkerConfig(Sprite markerConfig) {
    if (this.markerConfig != markerConfig) {
      this.markerConfig = markerConfig;
      clear();
    }
  }

  /**
   * Sets the selection tolerance of markers.
   * 
   * @param selectionTolerance the selection tolerance of markers
   */
  public void setSelectionTolerance(double selectionTolerance) {
    this.selectionTolerance = selectionTolerance;
  }

  /**
   * Sets the position of the x axis on the chart to be used by the series.
   * 
   * @param xAxisPosition the position of the x axis on the chart to be used by
   *          the series
   */
  public void setXAxisPosition(Position xAxisPosition) {
    this.xAxisPosition = xAxisPosition;
  }

  /**
   * Sets the position of the y axis on the chart to be used by the series.
   * 
   * @param yAxisPosition the position of the y axis on the chart to be used by
   *          the series
   */
  public void setYAxisPosition(Position yAxisPosition) {
    this.yAxisPosition = yAxisPosition;
  }

  /**
   * Sets the {@link ValueProvider} used for the y axis of the series.
   * 
   * @param yField the value provider
   */
  public void setYField(ValueProvider<? super M, ? extends Number> yField) {
    this.yField = yField;
    if (title == null) {
      legendTitles.set(0, getValueProviderName(yField, 0));
    }
  }

  @Override
  public void show(int yFieldIndex) {
    toggle(false);
  }

  @Override
  public void unHighlight(int yFieldIndex) {
    Sprite sprite = sprites.get(yFieldIndex);
    if (sprite != null && highlighter != null) {
      highlighter.unHighlight(sprite);
    }
  }

  @Override
  public void unHighlightAll(int index) {
    for (int i = 0; i < sprites.size(); i++) {
      highlighter.unHighlight(sprites.get(i));
    }
  }

  @Override
  public boolean visibleInLegend(int index) {
    if (sprites.size() == 0) {
      return true;
    } else {
      return !sprites.get(0).isHidden();
    }
  }

  /**
   * Draws the labels on the series.
   */
  protected void drawLabels() {
    if (labelConfig != null) {
      for (int j = coordinates.length; j < labels.size(); j++) {
        Sprite unusedLabel = labels.get(j);
        unusedLabel.setHidden(true);
        unusedLabel.redraw();
      }
      for (int i = 0; i < chart.getStore().size(); i++) {
        final Sprite sprite;
        if (labels.get(i) != null) {
          sprite = labels.get(i);
          if (!hidden) {
            sprite.setHidden(false);
          }
        } else {
          sprite = labelConfig.getSpriteConfig().copy();
          sprite.setTranslation((bbox.getX() + bbox.getWidth()) / 2, (bbox.getY() + bbox.getHeight()) / 2);
          labels.put(i, sprite);
          chart.addSprite(sprite);
        }
        if (chart.isResizing()) {
          sprite.setTranslation((bbox.getX() + bbox.getWidth()) / 2, (bbox.getY() + bbox.getHeight()) / 2);
        }
        setLabelText(sprite, i);
        sprite.redraw();
        if (labelConfig.isLabelContrast()) {
          final Sprite back = sprites.get(i);
          if (chart.isAnimated()) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
              @Override
              public void execute() {
                setLabelContrast(sprite, labelConfig, back);
              }
            });
          } else {
            setLabelContrast(sprite, labelConfig, back);
          }
        }
        SeriesRenderer<M> spriteRenderer = labelConfig.getSpriteRenderer();
        if(spriteRenderer != null) {
          spriteRenderer.spriteRenderer(sprite, i, chart.getCurrentStore());
        }
        PreciseRectangle textBox = sprite.getBBox();
        PrecisePoint point = coordinates[i];
        if (point != null) {
          double x = point.getX();
          double y = point.getY();

          y -= textBox.getHeight() / 2.0;

          if (chart.isAnimated() && sprite.getTranslation() != null) {
            DrawFx.createTranslationAnimator(sprite, x, y).run(chart.getAnimationDuration(), chart.getAnimationEasing());
          } else {
            sprite.setTranslation(x, y);
            sprite.redraw();
          }
        } else {
          sprite.setHidden(true);
          sprite.redraw();
        }
      }
    }
  }

  protected void drawMarkers() {
    ListStore<M> store = chart.getCurrentStore();

    double ratio = 1;
    if (coordinates.length < chart.getCurrentStore().size()) {
      ratio = (double) chart.getCurrentStore().size() / (double) coordinates.length;
    }

    // hide unused sprites
    for (int j = coordinates.length; j < sprites.size(); j++) {
      Sprite unusedSprite = sprites.get(j);
      unusedSprite.setHidden(true);
      unusedSprite.redraw();
    }
    
    if (!chart.hasShadows()) {
      hideShadows();
    } else {
      for (int k = 0; k < shadowGroups.size(); k++) {
        SpriteList<Sprite> shadows = shadowGroups.get(k);
        for (int j = coordinates.length; j < shadows.size(); j++) {
          Sprite unusedSprite = shadows.get(j);
          unusedSprite.setHidden(true);
          unusedSprite.redraw();
        }
      }
    }

    // Create new or reuse sprites and animate/display
    for (int i = 0; i < chart.getStore().size(); i++) {
      final Sprite sprite;
      if (i < sprites.size()) {
        sprite = sprites.get(i);
        if (!hidden) {
          sprite.setHidden(false);
        }
      } else {
        sprite = markerConfig.copy();
        sprite.setTranslation((bbox.getX() + bbox.getWidth()) / 2, (bbox.getY() + bbox.getHeight()) / 2);
        sprites.add(sprite);
        chart.addSprite(sprite);
      }
      if (chart.isResizing()) {
        sprite.setTranslation((bbox.getX() + bbox.getWidth()) / 2, (bbox.getY() + bbox.getHeight()) / 2);
      }
      if (chart.hasShadows()) {
        drawShadows(i);
      }
      PrecisePoint point = coordinates[i];
      if (point != null) {
        double x = point.getX();
        double y = point.getY();
        if (chart.isAnimated()) {
          DrawFx.createTranslationAnimator(sprite, x, y).run(chart.getAnimationDuration(), chart.getAnimationEasing());
        } else {
          sprite.setTranslation(x, y);
          sprite.redraw();
        }
      } else {
        sprite.setHidden(true);
        sprite.redraw();
      }
      if (renderer != null) {
        renderer.spriteRenderer(sprite, (int) Math.ceil(i * ratio), store);
      }
    }
  }

  protected void drawShadows(int i) {
    double ratio = 1;
    if (coordinates.length < chart.getCurrentStore().size()) {
      ratio = (double) chart.getCurrentStore().size() / (double) coordinates.length;
    }

    for (int shindex = 0; shindex < shadowGroups.size(); shindex++) {
      SpriteList<Sprite> shadows = shadowGroups.get(shindex);
      Sprite shadowAttr = shadowAttributes.get(shindex);
      final Sprite shadowSprite;
      if (i < shadows.size()) {
        shadowSprite = shadows.get(i);
      } else {
        shadowSprite = markerConfig.copy();
        shadowSprite.setTranslation((bbox.getX() + bbox.getWidth()) / 2, (bbox.getY() + bbox.getHeight()) / 2);
        shadowSprite.update(shadowAttr);
        shadows.add(shadowSprite);
        chart.addSprite(shadowSprite);
      }
      shadowSprite.setHidden(hidden);
      if (chart.isResizing()) {
        shadowSprite.setTranslation((bbox.getX() + bbox.getWidth()) / 2, (bbox.getY() + bbox.getHeight()) / 2);
      }
      PrecisePoint point = coordinates[i];
      if (point != null) {
        if (chart.isAnimated()) {
          DrawFx.createTranslationAnimator(shadowSprite, point.getX(), point.getY()).run(chart.getAnimationDuration(),
              chart.getAnimationEasing());
        } else {
          shadowSprite.setTranslation(point.getX(), point.getY());
          shadowSprite.redraw();
        }
      } else {
        shadowSprite.setHidden(true);
        shadowSprite.redraw();
      }
      if (shadowRenderer != null) {
        shadowRenderer.spriteRenderer(shadowSprite, (int) Math.ceil(i * ratio), chart.getCurrentStore());
      }
    }
    shadowed = true;
  }

  @Override
  protected int getIndex(PrecisePoint point) {
    int bestMatchIndex = -1;
    double bestDistance = Double.MAX_VALUE;
    double selectionToleranceSq = selectionTolerance * selectionTolerance;
    for (int i = 0; i < coordinates.length; i++) {
      PrecisePoint coordinate = coordinates[i];
      double distanceSq = Math.pow(coordinate.getX() - point.getX(), 2) + Math.pow(coordinate.getY() - point.getY(), 2);
      if (distanceSq < selectionToleranceSq && distanceSq < bestDistance) {
        bestMatchIndex = i;
        bestDistance = distanceSq;
      }
    }
    return bestMatchIndex;
  }

  @Override
  protected int getStoreIndex(int index) {
    double ratio = 1;
    if (coordinates.length < chart.getCurrentStore().size()) {
      ratio = (double) chart.getCurrentStore().size() / (double) coordinates.length;
    }
    return (int) Math.ceil(index * ratio);
  }

  @Override
  protected ValueProvider<? super M, ? extends Number> getValueProvider(int index) {
    return yField;
  }

  /**
   * Shrinks the number of coordinates to fit the screen.
   * 
   * @param width the maximum width of the chart
   * @return the new shrunk coordinates
   */
  protected PrecisePoint[] shrink(double width) {
    // Used for one data point
    if (width < 1) {
      width = 1;
    }

    PrecisePoint[] result = new PrecisePoint[(int)width];
    result[0] = coordinates[0];
    result[result.length - 1] = coordinates[coordinates.length - 1];
    double xSum = 0;
    double ySum = 0;
    final double ratio = coordinates.length / width;
    int next = 0;
    double itemCount = 0d;
    int nullCount = 0;
    for (int i = 1; i < coordinates.length - 1; i++) {
      PrecisePoint point = coordinates[i];
      itemCount++; // resets after each average

      if (point != null) {
        xSum += point.getX();
        ySum += point.getY();
      } else {
        nullCount++;
      }

      if (i % ratio < 1 && itemCount != 0) {
        if (nullCount > (ratio/2)) {
          result[++next] = null;
        } else {
          result[++next] = new PrecisePoint(xSum / itemCount, ySum / itemCount);
        }
        xSum = 0;
        ySum = 0;
        itemCount = 0;
        nullCount = 0;
      }
    }
    return result;
  }

  /**
   * Toggles all the sprites in the series to be hidden or shown.
   * 
   * @param hide if true hides
   */
  private void toggle(boolean hide) {
    if (sprites.size() > 0) {
      hidden = hide;
      for (int i = 0; i < sprites.size(); i++) {
        sprites.get(i).setHidden(hide);
        sprites.get(i).redraw();
      }
      if (labelConfig != null) {
        for (Sprite s : labels.values()) {
          s.setHidden(hide);
          s.redraw();
        }
      }

      if (chart.hasShadows()) {
        for (int i = 0; i < shadowGroups.size(); i++) {
          SpriteList<Sprite> shadows = shadowGroups.get(i);
          for (int j = 0; j < shadows.size(); j++) {
            shadows.get(j).setHidden(hide);
            shadows.get(j).redraw();
          }
        }
      }
    }
  }
}
