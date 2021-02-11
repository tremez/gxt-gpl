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
 * A {@link PathCommand} that represents a shorthand quadratic Bézier segment.
 * The control point is assumed to be the reflection of the control point on the
 * previous command relative to the current point.
 */
public class CurveToQuadraticSmooth extends EndPointCommand {

  private String absoluteName = "T";
  private String relativeName = "t";

  /**
   * Creates a shorthand quadratic curve {@link PathCommand} with the given
   * points. Defaults to absolute.
   */
  public CurveToQuadraticSmooth() {
  }

  /**
   * Creates a copy of the given shorthand quadratic curve {@link PathCommand}
   * with the given points.
   * 
   * @param smoothQuadratic the shorthand quadratic curve to copy
   */
  public CurveToQuadraticSmooth(CurveToQuadraticSmooth smoothQuadratic) {
    super(smoothQuadratic);
  }

  /**
   * Creates a shorthand quadratic curve {@link PathCommand} with the given
   * points. Defaults to absolute.
   * 
   * @param x the x-coordinate of the end of the segment
   * @param y the y-coordinate of the end of the segment
   */
  public CurveToQuadraticSmooth(double x, double y) {
    super(x, y);
  }

  /**
   * Creates a shorthand quadratic curve {@link PathCommand} with the given
   * points.
   * 
   * @param x the x-coordinate of the end of the segment
   * @param y the y-coordinate of the end of the segment
   * @param relative true if the command is relative
   */
  public CurveToQuadraticSmooth(double x, double y, boolean relative) {
    super(x, y, relative);
  }

  @Override
  public CurveToQuadraticSmooth copy() {
    return new CurveToQuadraticSmooth(this);
  }

  @Override
  public boolean nearEqual(PathCommand command) {
    if (!(command instanceof CurveToQuadraticSmooth)) {
      return false;
    }
    CurveToQuadraticSmooth quadraticSmooth = (CurveToQuadraticSmooth) command;
    if (Math.round(this.getX()) != Math.round(quadraticSmooth.getX())) {
      return false;
    }
    if (Math.round(this.getY()) != Math.round(quadraticSmooth.getY())) {
      return false;
    }

    return true;
  }

  @Override
  public List<PathCommand> toCurve(PrecisePoint currentPoint, PrecisePoint movePoint, PrecisePoint curvePoint,
      PrecisePoint quadraticPoint) {
    double x = currentPoint.getX();
    double y = currentPoint.getY();
    double qx = x + (x - quadraticPoint.getX());
    double qy = y + (y - quadraticPoint.getY());
    quadraticPoint.setX(qx);
    quadraticPoint.setY(qy);
    double ax = 2.0 * qx / 3.0;
    double ay = 2.0 * qy / 3.0;
    return Collections.<PathCommand>singletonList(new CurveTo(
            x / 3.0 + ax, y / 3.0 + ay,
            this.x / 3.0 + ax, this.y / 3.0 + ay,
            this.x, this.y)
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
    build.append(x).append(",").append(y);
    return build.toString();
  }

  @Override
  public void appendTo(StringBuilder build) {
    if (!relative) {
      build.append(absoluteName);
    } else {
      build.append(relativeName);
    }
    build.append(x).append(",").append(y);
  }
}
