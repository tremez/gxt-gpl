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
package com.sencha.gxt.widget.core.client.grid;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;

/**
 * Defines the configuration for a header group. Header groups support rowspan
 * and colspan and horizontal alignment. Groups support both HTML and widget for
 * their content.
 */
public class HeaderGroupConfig {

  private int colspan = 1;
  private int column;
  private HorizontalAlignmentConstant horizontalAlignment = HasHorizontalAlignment.ALIGN_CENTER;
  private SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private int row;
  private int rowspan = 1;
  private Widget widget;

  /**
   * Creates a new header group without rowspan and colspan.
   * 
   * @param html the group text or html
   */
  public HeaderGroupConfig(String html) {
    this(SafeHtmlUtils.fromString(html));
  }

  /**
   * Creates a new header group without rowspan and colspan.
   * 
   * @param html the group html
   */
  public HeaderGroupConfig(SafeHtml html) {
    this.html = html;
  }

  /**
   * Creates a header group.
   * 
   * @param html the group text or html
   * @param rowspan the rowspan
   * @param colspan the colspan
   */
  public HeaderGroupConfig(String html, int rowspan, int colspan) {
    this(SafeHtmlUtils.fromString(html), rowspan, colspan);
  }

  /**
   * Creates a header group.
   * 
   * @param html the group html
   * @param rowspan the rowspan
   * @param colspan the colspan
   */
  public HeaderGroupConfig(SafeHtml html, int rowspan, int colspan) {
    this(html);
    this.rowspan = rowspan;
    this.colspan = colspan;
  }

  /**
   * Creates a header group.
   * 
   * @param widget the group's widget
   * @param rowspan the rowspan
   * @param colspan the colspan
   */
  public HeaderGroupConfig(Widget widget, int rowspan, int colspan) {
    this.widget = widget;
    this.rowspan = rowspan;
    this.colspan = colspan;
  }

  /**
   * Returns the colspan.
   * 
   * @return the colspan
   */
  public int getColspan() {
    return colspan;
  }

  /**
   * Returns the column.
   * 
   * @return the column
   */
  public int getColumn() {
    return column;
  }

  /**
   * Returns the horizontal alignment.
   * 
   * @return the alignment
   */
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /**
   * Returns the html.
   * 
   * @return the html
   */
  public SafeHtml getHtml() {
    return html;
  }

  /**
   * Returns the row.
   * 
   * @return the row
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the rowspan.
   * 
   * @return the rowspan
   */
  public int getRowspan() {
    return rowspan;
  }

  /**
   * Returns the widget.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return widget;
  }

  /**
   * Sets the colspan (defaults to 1).
   * 
   * @param colspan the colspan
   */
  public void setColspan(int colspan) {
    this.colspan = colspan;
  }

  /**
   * Sets the 0-indexed column
   * 
   * @param column the column
   */
  public void setColumn(int column) {
    this.column = column;
  }

  /**
   * Sets the horizontal alignment
   * 
   * @param horizontalAlignment the alignment
   */
  public void setHorizontalAlignment(HorizontalAlignmentConstant horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Sets the group's html.
   * 
   * @param html the html text
   */
  public void setHtml(SafeHtml html) {
    this.html = html;
  }

  /**
   * Sets the 0-indexed row.
   * 
   * @param row the row
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * Sets the rowspan (defaults to 1).
   * 
   * @param rowspan the rowspan
   */
  public void setRowspan(int rowspan) {
    this.rowspan = rowspan;
  }

  /**
   * Sets the group's widget.
   * 
   * @param widget the widget
   */
  public void setWidget(Widget widget) {
    this.widget = widget;
  }

}
