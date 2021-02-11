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
package com.sencha.gxt.widget.core.client.container;

/**
 * A class that implements this interface has a width specification. This
 * interface provides access to the width specification without compromising the
 * ability to provide a mock container instance in JRE unit tests.
 */
public interface HasWidth {
  /**
   * Returns the width specification. Values greater than 1 represent width in
   * pixels. Values between 0 and 1 (inclusive) represent a percent of the width
   * of the container. A value of -1 represents the default width of the
   * associated widget. Values less than -1 represent the width of the container
   * minus the absolute value of the widget width.
   * 
   * @return the width specification
   */
  double getWidth();

  /**
   * Sets the width specification. Values greater than 1 represent width in
   * pixels. Values between 0 and 1 (inclusive) represent a percent of the width
   * of the container. A value of -1 represents the default width of the
   * associated widget. Values less than -1 represent the width of the container
   * minus the absolute value of the widget width.
   * 
   * @param width the width specification
   */
  void setWidth(double width);
}
