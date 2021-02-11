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
 * A widget that implements this interface has the ability to lay out its
 * children. This interface provides access to the layout capability without
 * compromising the ability to provide a mock container instance in JRE unit
 * tests.
 */
public interface HasLayout {
  /**
   * Forces a class that implements <code>HasLayout</code> to lay out its child
   * widgets.
   */
  void forceLayout();

  /**
   * Returns true if invoked when a class that implements <code>HasLayout</code>
   * is in the process of laying out it's children. Useful in avoiding recursive
   * lay out operations.
   * 
   * @return true if in the process of performing a lay out operation
   */
  boolean isLayoutRunning();

  /**
   * Returns true if invoked when a class that implements <code>HasLayout</code>
   * is in the process of laying out it's children or has performed a lay out in
   * the past.
   * 
   * @return true if a layout is running or has run, false if a layout has not
   *         yet been performed.
   */
  boolean isOrWasLayoutRunning();
}
