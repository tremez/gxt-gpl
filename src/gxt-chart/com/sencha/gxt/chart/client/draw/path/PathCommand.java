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

import java.util.List;

import com.sencha.gxt.core.client.util.PrecisePoint;

/**
 * A command that represents a segment of a path.
 */
public abstract class PathCommand {

  protected boolean relative = false;

  /**
   * Creates a path command. Defaults to absolute.
   */
  public PathCommand() {
  }

  /**
   * Creates a path command.
   * 
   * @param relative true if the command is relative
   */
  public PathCommand(boolean relative) {
    this.relative = relative;
  }

  /**
   * Creates a copy of the given command.
   * 
   * @param command the command to be copied
   */
  public PathCommand(PathCommand command) {
    this.relative = command.relative;
  }

  public abstract void appendTo(StringBuilder build);

  /**
   * Returns a copy of the path command.
   * 
   * @return a copy of the path command
   */
  public abstract PathCommand copy();

  /**
   * Returns true if the command is relative.
   * 
   * @return true if the command is relative
   */
  public boolean isRelative() {
    return relative;
  }

  /**
   * Determines equality of two commands using rounded values.
   * 
   * @param command the command to compare
   * @return true if equal and false otherwise
   */
  public abstract boolean nearEqual(PathCommand command);

  /**
   * Sets the command to relative or absolute
   * 
   * @param relative true if relative
   */
  public void setRelative(boolean relative) {
    this.relative = relative;
  }

  /**
   * Converts the path commands to absolute coordinates using the given frame of
   * reference and the last move.
   * 
   * @param currentPoint frame of reference
   * @param movePoint last move point
   */
  public void toAbsolute(PrecisePoint currentPoint, PrecisePoint movePoint) {
    relative = false;
  }

  /**
   * Converts the {@link PathCommand} to a {@link CurveTo} command. If a
   * {@link MoveTo} no conversion takes place, but the current move point is
   * updated.
   * 
   * @param currentPoint the current point of the path
   * @param movePoint the frame of reference for the path
   * @param curvePoint the frame of reference for the Bézier
   * @param quadraticPoint the beginning of the last quadratic curve
   * @return the converted command
   */
  public abstract List<PathCommand> toCurve(PrecisePoint currentPoint, PrecisePoint movePoint, PrecisePoint curvePoint,
      PrecisePoint quadraticPoint);

}
