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
package com.sencha.gxt.chart.client.draw.path;

import java.util.Collections;
import java.util.List;

import com.sencha.gxt.core.client.util.PrecisePoint;

/**
 * A {@link PathCommand} that represents a horizontal line.
 */
public class LineToHorizontal extends PathCommand {

  private double x;
  private String absoluteName = "H";
  private String relativeName = "h";

  /**
   * Creates a horizontal line {@link PathCommand}. Defaults to absolute.
   */
  public LineToHorizontal() {
  }

  /**
   * Creates a horizontal line {@link PathCommand} using the given coordinates.
   * Defaults to absolute.
   * 
   * @param x the x-coordinate of the end point of the line
   */
  public LineToHorizontal(double x) {
    this.x = x;
  }

  /**
   * Creates a horizontal line {@link PathCommand} using the given coordinates.
   * 
   * @param x the x-coordinate of the end point of the line
   * @param relative true if the command is relative
   */
  public LineToHorizontal(double x, boolean relative) {
    super(relative);
    this.x = x;
  }

  /**
   * Creates a copy of the given horizontal line {@link PathCommand}.
   * 
   * @param horizontal the command to be copied
   */
  public LineToHorizontal(LineToHorizontal horizontal) {
    super(horizontal);
    this.x = horizontal.x;
  }

  @Override
  public LineToHorizontal copy() {
    return new LineToHorizontal(this);
  }

  /**
   * Returns the x-coordinate of the end point of the command.
   * 
   * @return the x-coordinate of the end point of the command
   */
  public double getX() {
    return x;
  }

  @Override
  public boolean nearEqual(PathCommand command) {
    if (!(command instanceof LineToHorizontal)) {
      return false;
    }
    LineToHorizontal horizontal = (LineToHorizontal) command;
    if (Math.round(this.getX()) != Math.round(horizontal.getX())) {
      return false;
    }

    return true;
  }

  /**
   * Sets the x-coordinate of the end point of the command.
   * 
   * @param x the x-coordinate of the end point of the command
   */
  public void setX(double x) {
    this.x = x;
  }

  @Override
  public void toAbsolute(PrecisePoint currentPoint, PrecisePoint movePoint) {
    if (isRelative()) {
      super.toAbsolute(currentPoint, movePoint);
      setX(getX() + currentPoint.getX());
    }
    currentPoint.setX(getX());
  }

  @Override
  public List<PathCommand> toCurve(PrecisePoint currentPoint, PrecisePoint movePoint, PrecisePoint curvePoint,
      PrecisePoint quadraticPoint) {
    quadraticPoint.setX(currentPoint.getX());
    quadraticPoint.setY(currentPoint.getY());
    return Collections.<PathCommand>singletonList(new CurveTo(
            currentPoint.getX(), currentPoint.getY(),
            this.getX(), currentPoint.getY(),
            this.getX(), currentPoint.getY()));
  }

  @Override
  public String toString() {
    StringBuilder build = new StringBuilder();
    if (!relative) {
      build.append(absoluteName);
    } else {
      build.append(relativeName);
    }
    build.append(x);
    return build.toString();
  }

  @Override
  public void appendTo(StringBuilder build) {
    if (!relative) {
      build.append(absoluteName);
    } else {
      build.append(relativeName);
    }
    build.append(x);
  }
}
