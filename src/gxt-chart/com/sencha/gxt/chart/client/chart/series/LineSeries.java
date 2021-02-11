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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Translation;
import com.sencha.gxt.chart.client.draw.path.LineTo;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathCommand;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.SpriteList;
import com.sencha.gxt.core.client.util.PrecisePoint;
import com.sencha.gxt.core.client.util.PreciseRectangle;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;

/**
 * Creates a Line Chart. A Line Chart is a useful visualization technique to
 * display quantitative information for different categories or other real
 * values (as opposed to the {@link BarSeries}), that can show some progression
 * (or regression) in the data set.
 * 
 * Here is an example line configuration:
 * 
 * <pre>
    LineSeries<Data> series = new LineSeries<Data>();
    series.setYAxisPosition(Position.LEFT);
    series.setYField(dataAccess.data1());
    series.setStroke(new RGB(194,0,36));
    chart.addSeries(series);
 * </pre>
 * 
 * First the series is created with its associated data type. The y-axis
 * position is set to tell the series the scale of the displayed axis. Otherwise
 * the series will use its own internal scale. Next the value provider field is
 * set, which provides the value of each point on the line. The stroke is set to
 * represent the color of the line. Finally the series is added to the chart
 * where it will be displayed.
 * 
 * @param <M> the data type used by the series
 */
public class LineSeries<M> extends ScatterSeries<M> {

  private PathSprite line;
  private PathSprite fillSprite;
  private SpriteList<PathSprite> lineShadows = new SpriteList<PathSprite>();
  private boolean showMarkers = false;
  private boolean smooth = false;
  private int segments = 3;
  private Color fill = RGB.NONE;
  private List<PathCommand> lineCommands = new ArrayList<PathCommand>();
  private List<PathCommand> previousCommands;
  private double markerIndex = 0;
  private SeriesRenderer<M> lineRenderer;
  private SeriesRenderer<M> fillRenderer;
  private boolean gapless = true;
  private SeriesHighlighter lineHighlighter = new LineHighlighter();
  private FastMap<Integer> gapPosition = new FastMap<Integer>();

  /**
   * Creates a line {@link Series}.
   */
  public LineSeries() {
    // setup shadow attributes
    shadowAttributes = new ArrayList<Sprite>();
    Sprite config = new PathSprite();
    config.setStrokeWidth(6);
    config.setStrokeOpacity(0.05);
    config.setStroke(RGB.BLACK);
    config.setTranslation(1, 1);
    config.setZIndex(9);
    config.setFill(Color.NONE);
    shadowAttributes.add(config);
    config = new PathSprite();
    config.setStrokeWidth(4);
    config.setStrokeOpacity(0.1);
    config.setStroke(RGB.BLACK);
    config.setTranslation(1, 1);
    config.setZIndex(9);
    config.setFill(Color.NONE);
    shadowAttributes.add(config);
    config = new PathSprite();
    config.setStrokeWidth(2);
    config.setStrokeOpacity(0.15);
    config.setStroke(RGB.BLACK);
    config.setTranslation(1, 1);
    config.setZIndex(9);
    config.setFill(Color.NONE);
    shadowAttributes.add(config);

    // initialize shadow groups
    if (shadowGroups.size() == 0) {
      for (int i = 0; i < shadowAttributes.size(); i++) {
        shadowGroups.add(new SpriteList<Sprite>());
      }
    }

    setHighlighter(new ScatterHighlighter());
    strokeWidth = 0.5;
  }

  @Override
  public void clear() {
    super.clear();
    if (line != null) {
      line.remove();
      line = null;
    }
    if (fillSprite != null) {
      fillSprite.remove();
      fillSprite = null;
    }
    lineShadows.clear();
  }

  @Override
  public void drawSeries() {
    List<PathCommand> fillCommands = new ArrayList<PathCommand>();
    double firstY = Double.NaN;
    Sprite marker;
    ListStore<M> store = chart.getCurrentStore();
    lineCommands.clear();
    gapPosition.clear();

    if (store == null || store.size() == 0) {
      this.clear();
      return;
    }

    if (markerIndex > 0 && sprites.size() > 0) {
      for (int i = 0; i < markerIndex; i++) {
        marker = sprites.get(i);
        sprites.remove(i);
        sprites.add(marker);
        Sprite markerTemp = sprites.get(sprites.size() - 2);
        marker.setTranslation(new Translation(markerTemp.getTranslation()));
        marker.redraw();
      }
    }

    calculateBounds();

    double x = 0;
    double y = 0;
    boolean gap = false;
    double ratio = 1;
    if (coordinates != null && coordinates.length < chart.getCurrentStore().size()) {
      ratio = (double) chart.getCurrentStore().size() / (double) coordinates.length;
    }
    for (int i = 0; i < coordinates.length; i++) {
      PrecisePoint point = coordinates[i];
      if (point != null) {
        x = point.getX();
        y = point.getY();
      } else {
        gap = !gapless;
        continue;
      }
      if (!Double.isNaN(x) && !Double.isNaN(y)) {
        if (lineCommands.size() > 0 && !gap) {
          lineCommands.add(new LineTo(x, y));
        } else {
          lineCommands.add(new MoveTo(x, y));
          gap = false;
        }
        if (i == coordinates.length - 1) {
          gapPosition.put(String.valueOf(lineCommands.size() - 1), chart.getCurrentStore().size() - 1);
        } else {
          gapPosition.put(String.valueOf(lineCommands.size() - 1), (int) Math.ceil(i * ratio));
        }
      } else if (!gapless) {
        gap = true;
      }

      if (Double.isNaN(firstY) && !Double.isNaN(y)) {
        firstY = y;
      }
    }

    List<PathCommand> renderCommands;

    if (smooth && lineCommands.size() > 0) {
      PathSprite smooth = new PathSprite();
      smooth.setCommands(lineCommands);
      renderCommands = smooth.copy().toSmooth(segments).getCommands();
    } else {
      renderCommands = PathSprite.copyCommands(lineCommands);
    }

    // Correct path if we're animating timeAxis intervals
    if (markerIndex > 0 && previousCommands != null && previousCommands.size() > 1) {
      previousCommands.remove(1);
      line.setCommands(previousCommands);
      if (chart.hasShadows()) {
        for (int i = 0; i < lineShadows.size(); i++) {
          PathSprite shadow = lineShadows.get(i);
          shadow.setCommands(previousCommands);
        }
      }
    }

    List<PathCommand> dummyCommands = new ArrayList<PathCommand>();
    dummyCommands.add(new MoveTo(bbox.getX(), bbox.getY() + bbox.getHeight() / 2.0));
    for (int k = 1; k < lineCommands.size(); k++) {
      dummyCommands.add(new LineTo(bbox.getX() + bbox.getWidth() / k, bbox.getY() + bbox.getHeight() / 2.0));
    }

    // Only create a line if one doesn't exist.
    if (line == null) {
      line = new PathSprite();
      line.setStroke(stroke);
      chart.addSprite(line);
      line.setCommands(dummyCommands);

      // create shadows
      for (int i = 0; i < shadowGroups.size(); i++) {
        PathSprite shadow = new PathSprite();
        Sprite shadowAttr = shadowAttributes.get(i);
        shadow.update(shadowAttr);
        shadow.setHidden(true);
        chart.addSprite(shadow);
        lineShadows.add(shadow);
      }
    }
    if (stroke != null) {
      line.setStroke(stroke);
    }
    if (!Double.isNaN(strokeWidth)) {
      line.setStrokeWidth(strokeWidth);
    }
    line.setFill(Color.NONE);

    if (chart.isAnimated() && line.size() > 0) {
      if (markerIndex > 0) {
        if (smooth) {
          renderCommands.remove(1);
        } else {
          MoveTo move = (MoveTo) renderCommands.get(0);
          renderCommands.add(1, new LineTo(move.getX(), move.getY()));
        }
        previousCommands = renderCommands;
      }
      DrawFx.createCommandsAnimator(line, renderCommands).run(500);
    } else {
      line.setCommands(renderCommands);
      line.redraw();
    }

    if (lineRenderer != null) {
      lineRenderer.spriteRenderer(line, 0, chart.getStore());
    }

    if (fill != Color.NONE && fill != null) {
      fillCommands.addAll(renderCommands);
      fillCommands.add(new LineTo(x, bbox.getY() + bbox.getHeight()));
      fillCommands.add(new LineTo(bbox.getX(), bbox.getY() + bbox.getHeight()));
      fillCommands.add(new LineTo(bbox.getX(), firstY));

      if (fillSprite == null) {
        fillSprite = new PathSprite();
        fillSprite.setOpacity(0.3);
        fillSprite.setFill(fill);
        fillSprite.setCommands(dummyCommands);
        chart.addSprite(fillSprite);
      }
      if (chart.isAnimated() && fillSprite.size() > 0) {
        DrawFx.createCommandsAnimator(fillSprite, fillCommands).run(chart.getAnimationDuration(),
            chart.getAnimationEasing());
      } else {
        fillSprite.setCommands(fillCommands);
        fillSprite.redraw();
      }
      if (fillRenderer != null) {
        fillRenderer.spriteRenderer(fillSprite, 0, chart.getStore());
      }
    }

    if (chart.hasShadows()) {
      for (int i = 0; i < lineShadows.size(); i++) {
        PathSprite shadow = lineShadows.get(i);
        if (!hidden) {
          shadow.setHidden(false);
        }
        if (chart.isAnimated()) {
          DrawFx.createCommandsAnimator(shadow, renderCommands).run(chart.getAnimationDuration(),
              chart.getAnimationEasing());
        } else {
          shadow.setCommands(renderCommands);
          shadow.redraw();
        }
        if (shadowRenderer != null) {
          shadowRenderer.spriteRenderer(shadow, i, chart.getCurrentStore());
        }
      }
      shadowed = true;
    } else {
      hideShadows();
    }

    if (showMarkers) {
      for (int i = 0; i < lineCommands.size(); i++) {
        int index = i;
        PrecisePoint point = getPointFromCommand(lineCommands.get(index));
        if (index < sprites.size()) {
          marker = sprites.get(index);
          marker.setHidden(false);
        } else {
          marker = markerConfig.copy();
          if (i == 0) {
            marker.setTranslation(bbox.getX(), bbox.getY() + bbox.getHeight() / 2.0);
          } else {
            marker.setTranslation(bbox.getX() + bbox.getWidth() / i, bbox.getY() + bbox.getHeight() / 2.0);
          }
          sprites.add(marker);
          chart.addSprite(marker);
        }
        if (hidden) {
          marker.setHidden(true);
        }
        if (chart.isAnimated() && marker.getTranslation() != null
            && ((markerIndex <= 0) || (markerIndex > 0 && index != sprites.size() - 1))) {
          DrawFx.createTranslationAnimator(marker, point.getX(), point.getY()).run(chart.getAnimationDuration(),
              chart.getAnimationEasing());
        } else {
          marker.setTranslation(point.getX(), point.getY());
          marker.redraw();
        }
        if (renderer != null) {
          renderer.spriteRenderer(marker, (int) Math.ceil(index * ratio), store);
        }
      }
    }

    for (int j = lineCommands.size(); j < sprites.size(); j++) {
      Sprite unusedSprite = sprites.get(j);
      unusedSprite.setHidden(true);
      unusedSprite.redraw();
    }
    drawLabels();
  }

  /**
   * Returns the fill color of the line.
   * 
   * @return the fill color of the line
   */
  public Color getFill() {
    return fill;
  }

  /**
   * Returns the {@link SeriesRenderer} used on the fill sprite.
   * 
   * @return the series renderer used on the fill sprite
   */
  public SeriesRenderer<M> getFillRenderer() {
    return fillRenderer;
  }

  public SeriesHighlighter getLineHighlighter() {
    return lineHighlighter;
  }

  /**
   * Returns the {@link SeriesRenderer} used on the line sprite.
   * 
   * @return the series renderer used on the line sprite
   */
  public SeriesRenderer<M> getLineRenderer() {
    return lineRenderer;
  }

  /**
   * Returns the marker index of the series. Determines the number of markers to
   * animate to simulate progression.
   * 
   * @return the marker index of the series
   */
  public double getMarkerIndex() {
    return markerIndex;
  }

  /**
   * Returns the number of segments of the smoothed line.
   * 
   * @return the number of segments of the smoothed line
   */
  public int getSegments() {
    return segments;
  }

  @Override
  public void hide(int yFieldIndex) {
    toggle(true);
  }

  @Override
  public void highlight(int yFieldIndex) {
    if (showMarkers) {
      highlighter.highlight(sprites.get(yFieldIndex));
    }
  }

  @Override
  public void highlightAll(int index) {
    if (line != null) {
      lineHighlighter.highlight(line);
    }
    for (int i = 0; i < sprites.size(); i++) {
      highlighter.highlight(sprites.get(i));
    }
  }

  /**
   * Returns whether the line is drawn over gaps in field.
   * 
   * @return whether the line is drawn over gaps in field
   */
  public boolean isGapless() {
    return gapless;
  }

  /**
   * Returns whether or not markers are shown.
   * 
   * @return whether or not markers are shown
   */
  public boolean isShowMarkers() {
    return showMarkers;
  }

  /**
   * Returns whether or not the line is smoothed or straight.
   * 
   * @return true if smooth
   */
  public boolean isSmooth() {
    return smooth;
  }


  @Override
  public int onMouseMove(PrecisePoint point, Event event) {
    int index = getIndex(point);
    if (index > -1) {
      Sprite sprite = sprites.get(index);
      if (sprite != null && sprite.isHidden()) {
        return -1;
      }
    }
    return super.onMouseMove(point, event);
  }

  /**
   * Sets the fill color of the line. If none the fill will not be drawn.
   * 
   * @param fill the color of the fill
   */
  public void setFill(Color fill) {
    this.fill = fill;
  }

  /**
   * Sets the {@link SeriesRenderer} used on the fill sprite.
   * 
   * @param fillRenderer the series renderer used on the fill sprite
   */
  public void setFillRenderer(SeriesRenderer<M> fillRenderer) {
    this.fillRenderer = fillRenderer;
  }

  /**
   * Sets whether the line is drawn over gaps in field. Gaps do not work with
   * {@link #setFill(Color)}.
   * 
   * @param gapless whether the line is drawn over gaps in field
   */
  public void setGapless(boolean gapless) {
    this.gapless = gapless;
  }

  public void setLineHighlighter(SeriesHighlighter lineHighlighter) {
    this.lineHighlighter = lineHighlighter;
  }

  /**
   * Sets the {@link SeriesRenderer} used on the line sprite
   * 
   * @param lineRenderer the series renderer used on the line sprite
   */
  public void setLineRenderer(SeriesRenderer<M> lineRenderer) {
    this.lineRenderer = lineRenderer;
  }

  /**
   * Sets the marker index. Determines the number of markers to animate to
   * simulate progression.
   * 
   * @param markerIndex the marker index
   */
  public void setMarkerIndex(double markerIndex) {
    this.markerIndex = markerIndex;
  }

  /**
   * Sets the number of segments of the smoothed line. Smooth line enabled using
   * {@link #setSmooth(boolean)}.
   * 
   * @param segments the number of segments of the smoothed line
   */
  public void setSegments(int segments) {
    this.segments = segments;
  }

  /**
   * Sets whether or not to show markers.
   * 
   * @param showMarkers whether or not to show markers
   */
  public void setShowMarkers(boolean showMarkers) {
    if (this.showMarkers != showMarkers) {
      this.showMarkers = showMarkers;
      sprites.clear();
      for (int i = 0; i < shadowGroups.size(); i++) {
        shadowGroups.get(i).clear();
      }
    }
  }

  /**
   * Sets whether or not the line is smoothed or straight.
   * 
   * @param smooth true if smooth
   */
  public void setSmooth(boolean smooth) {
    this.smooth = smooth;
  }

  @Override
  public void show(int yFieldIndex) {
    toggle(false);
  }

  @Override
  public void unHighlight(int yFieldIndex) {
    if (showMarkers) {
      highlighter.unHighlight(sprites.get(yFieldIndex));
    }
  }

  @Override
  public void unHighlightAll(int index) {
    if (line != null) {
      lineHighlighter.unHighlight(line);
    }
    for (int i = 0; i < sprites.size(); i++) {
      highlighter.unHighlight(sprites.get(i));
    }
  }

  @Override
  public boolean visibleInLegend(int index) {
    if (line == null) {
      return true;
    } else {
      return !line.isHidden();
    }
  }

  /**
   * Draws the labels on the series.
   */
  protected void drawLabels() {
    for (int j = lineCommands.size(); j < labels.size(); j++) {
      Sprite unusedSprite = labels.get(j);
      unusedSprite.setHidden(true);
      unusedSprite.redraw();
    }
    if (labelConfig != null) {
      for (int i = 0; i < lineCommands.size(); i++) {
        final Sprite sprite;
        if (labels.get(i) != null) {
          sprite = labels.get(i);
          if (!hidden) {
            sprite.setHidden(false);
          }
        } else {
          sprite = labelConfig.getSpriteConfig().copy();
          labels.put(i, sprite);
          chart.addSprite(sprite);
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
        PrecisePoint point = getPointFromCommand(lineCommands.get(i));
        double x = point.getX();
        double y = point.getY();
        if (showMarkers) {
          PreciseRectangle textBox = sprite.getBBox();

          y -= textBox.getHeight() / 2.0;
        }
        if (chart.isAnimated() && sprite.getTranslation() != null) {
          DrawFx.createTranslationAnimator(sprite, x, y).run(chart.getAnimationDuration(), chart.getAnimationEasing());
        } else {
          sprite.setTranslation(x, y);
          sprite.redraw();
        }
      }
    }
  }

  @Override
  protected int getIndex(PrecisePoint point) {
    int bestMatchIndex = -1;
    double bestDistance = Double.MAX_VALUE;
    double selectionToleranceSq = selectionTolerance * selectionTolerance;
    for (int i = 0; i < lineCommands.size(); i++) {
      PrecisePoint pointCommand = getPointFromCommand(lineCommands.get(i));
      double distanceSq = Math.pow(pointCommand.getX() - point.getX(), 2) + Math.pow(pointCommand.getY() - point.getY(), 2);
      if (distanceSq < selectionToleranceSq && distanceSq < bestDistance) {
        bestMatchIndex = i;
        bestDistance = distanceSq;
      }
    }
    return bestMatchIndex;
  }

  @Override
  protected int getStoreIndex(int index) {
    return gapPosition.get(String.valueOf(index));
  }

  @Override
  protected void hideShadows() {
    if (shadowed) {
      for (int i = 0; i < lineShadows.size(); i++) {
        lineShadows.get(i).setHidden(true);
        lineShadows.get(i).redraw();
      }
    }
    super.hideShadows();
  }

  /**
   * Toggles all the sprites in the series to be hidden or shown.
   * 
   * @param hide if true hides
   */
  private void toggle(boolean hide) {
    hidden = hide;
    if (line != null) {
      line.setHidden(hide);
      line.redraw();
      if (chart.hasShadows()) {
        for (int i = 0; i < lineShadows.size(); i++) {
          Sprite shadow = lineShadows.get(i);
          shadow.setHidden(hide);
          shadow.redraw();
        }
      }
    }
    if (fillSprite != null) {
      fillSprite.setHidden(hide);
      fillSprite.redraw();
    }
    if (labelConfig != null) {
      for (Sprite s : labels.values()) {
        s.setHidden(hide);
        s.redraw();
      }
    }
    if (sprites.size() > 0) {
      for (int i = 0; i < sprites.size(); i++) {
        sprites.get(i).setHidden(hide);
        sprites.get(i).redraw();
      }
      // Series needs to be redrawn if shown and there are gaps in the data
      if (hide == false && lineCommands.size() != chart.getCurrentStore().size()) {
        drawSeries();
      }
    }

  }

}
