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
 * A {@link PathCommand} that represents a vertical line.
 */
public class LineToVertical extends PathCommand {

  private double y = 0;
  private String absoluteName = "V";
  private String relativeName = "v";

  /**
   * Creates a vertical line {@link PathCommand}. Defaults to absolute.
   */
  public LineToVertical() {
  }

  /**
   * Creates a vertical line {@link PathCommand} using the given coordinates.
   * Defaults to absolute.
   * 
   * @param y the y-coordinate of the end point of the line
   */
  public LineToVertical(double y) {
    this.y = y;
  }

  /**
   * Creates a vertical line {@link PathCommand} using the given coordinates.
   * 
   * @param y the y-coordinate of the end point of the line
   * @param relative true if the command is relative
   */
  public LineToVertical(double y, boolean relative) {
    super(relative);
    this.y = y;
  }

  /**
   * Creates a copy of the given vertical line {@link PathCommand}.
   * 
   * @param vertical the command to be copied
   */
  public LineToVertical(LineToVertical vertical) {
    super(vertical);
    this.y = vertical.y;
  }

  @Override
  public LineToVertical copy() {
    return new LineToVertical(this);
  }

  /**
   * Returns the y-coordinate of the end point of the command.
   * 
   * @return the y-coordinate of the end point of the command
   */
  public double getY() {
    return y;
  }

  @Override
  public boolean nearEqual(PathCommand command) {
    if (!(command instanceof LineToVertical)) {
      return false;
    }
    LineToVertical vert = (LineToVertical) command;
    if (Math.round(this.getY()) != Math.round(vert.getY())) {
      return false;
    }

    return true;
  }

  /**
   * Sets the y-coordinate of the end point of the command.
   * 
   * @param y the y-coordinate of the end point of the command
   */
  public void setY(double y) {
    this.y = y;
  }

  @Override
  public void toAbsolute(PrecisePoint currentPoint, PrecisePoint movePoint) {
    if (isRelative()) {
      super.toAbsolute(currentPoint, movePoint);
      setY(getY() + currentPoint.getY());
    }
    currentPoint.setY(getY());
  }

  @Override
  public List<PathCommand> toCurve(PrecisePoint currentPoint, PrecisePoint movePoint, PrecisePoint curvePoint,
      PrecisePoint quadraticPoint) {
    quadraticPoint.setX(currentPoint.getX());
    quadraticPoint.setY(currentPoint.getY());
    return Collections.<PathCommand>singletonList(new CurveTo(
            currentPoint.getX(), currentPoint.getY(),
            currentPoint.getX(), this.getY(),
            currentPoint.getX(), this.getY())
    );
  }

  @Override
  public String toString() {
    StringBuilder build = new StringBuilder();
    if (!relative) {
      build.append(absoluteName);
    } else {
      build.append(relativeName);
    }
    build.append(y);
    return build.toString();
  }

  @Override
  public void appendTo(StringBuilder build) {
    if (!relative) {
      build.append(absoluteName);
    } else {
      build.append(relativeName);
    }
    build.append(y);
  }
}
