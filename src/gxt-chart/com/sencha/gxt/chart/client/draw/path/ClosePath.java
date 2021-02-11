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
 * A {@link PathCommand} that represents the end of the current subpath.
 */
public class ClosePath extends PathCommand {

  private String absoluteName = "Z";
  private String relativeName = "z";

  /**
   * Creates a close {@link PathCommand}. Defaults to absolute.
   */
  public ClosePath() {
  }

  /**
   * Creates a close {@link PathCommand} with the given boolean as to whether or
   * not it is relative.
   * 
   * @param relative true if the command is relative
   */
  public ClosePath(boolean relative) {
    super(relative);
  }

  /**
   * Creates a copy of the given close {@link PathCommand}.
   * 
   * @param command the command to be copied
   */
  public ClosePath(ClosePath command) {
    super(command);
  }

  @Override
  public ClosePath copy() {
    return new ClosePath(this);
  }

  @Override
  public boolean nearEqual(PathCommand command) {
    if (!(command instanceof ClosePath)) {
      return false;
    }
    return true;
  }

  @Override
  public void toAbsolute(PrecisePoint currentPoint, PrecisePoint movePoint) {
    currentPoint.setX(movePoint.getX());
    currentPoint.setY(movePoint.getY());
  }

  @Override
  public List<PathCommand> toCurve(PrecisePoint currentPoint, PrecisePoint movePoint, PrecisePoint curvePoint,
      PrecisePoint quadraticPoint) {
    quadraticPoint.setX(currentPoint.getX());
    quadraticPoint.setY(currentPoint.getY());
    return Collections.<PathCommand>singletonList(new CurveTo(
            currentPoint.getX(), currentPoint.getY(),
            movePoint.getX(), movePoint.getY(),
            movePoint.getX(), movePoint.getY())
    );
  }

  @Override
  public String toString() {
    if (!relative) {
      return absoluteName;
    } else {
      return relativeName;
    }
  }

  @Override
  public void appendTo(StringBuilder build) {
    build.append(toString());
  }

}
