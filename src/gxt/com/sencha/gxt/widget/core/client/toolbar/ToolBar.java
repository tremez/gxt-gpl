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
package com.sencha.gxt.widget.core.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HasMargins;

/**
 * A standard tool bar.
 */
public class ToolBar extends HBoxLayoutContainer {

  public interface ToolBarAppearance extends HBoxLayoutContainerAppearance {

    String toolBarClassName();

  }

  private int minButtonWidth = Style.DEFAULT;
  private int verticalSpacing = 0;
  private int horizontalSpacing = 0;

  /**
   * Creates a new tool bar.
   */
  public ToolBar() {
    this(GWT.<ToolBarAppearance> create(ToolBarAppearance.class));
  }

  /**
   * Creates a new tool bar.
   * 
   * @param appearance the tool bar appearance
   */
  public ToolBar(ToolBarAppearance appearance) {
    super(appearance);
    setStyleName(appearance.toolBarClassName());

    addStyleName("x-toolbar");
    addStyleName("x-toolbar-mark");
    addStyleName("x-small-editor");
    setSpacing(3);
    setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);

    sinkEvents(Event.FOCUSEVENTS);
  }

  /**
   * Returns the tool bar appearance.
   *
   * @return the appearance
   */
  public ToolBarAppearance getAppearance() {
    return (ToolBarAppearance) super.getAppearance();
  }

  /**
   * Returns the child widget horizontal spacing.
   * 
   * @return the spacing in pixels
   */
  public int getHorizontalSpacing() {
    return horizontalSpacing;
  }

  /**
   * Returns the minimum button width.
   * 
   * @return the minimum button width
   */
  public int getMinButtonWidth() {
    return minButtonWidth;
  }

  /**
   * Returns the child widget vertical spacing.
   * 
   * @return the spacing in pixels
   */
  public int getVerticalSpacing() {
    return verticalSpacing;
  }

  /**
   * Sets the minimum width for any buttons in the toolbar.
   * 
   * @param minWidth the minimum button width to set
   */
  public void setMinButtonWidth(int minWidth) {
    this.minButtonWidth = minWidth;
    for (int i = 0; i < getWidgetCount(); i++) {
      Widget w = getWidget(i);
      if (w instanceof CellButtonBase<?> && w != more) {
        ((CellButtonBase<?>) w).setMinWidth(minButtonWidth);
      }
    }
  }

  /**
   * Sets the vertical spacing between child items (defaults to 0). Spacing is implemented using layout data margins.
   * Margins will be overridden by layout if spacing >= 0. Set spacing to -1 to prevent margin calculations.
   * 
   * @param spacing the spacing
   */
  public void setVerticalSpacing(int spacing) {
    this.verticalSpacing = spacing;
    for (int i = 0; i < getWidgetCount(); i++) {
      setSpacingLayoutData(getWidget(i));
    }

    if (isAttached()) {
      forceLayout();
    }
  }

  /**
   * Sets the horizontal spacing between child items (defaults to 0). Spacing is implemented using layout data margins.
   * Margins will be overridden by layout if spacing >= 0. Set spacing to -1 to prevent margin calculations.
   * 
   * @param spacing the spacing
   */
  public void setHorizontalSpacing(int spacing) {
    this.horizontalSpacing = spacing;
    for (int i = 0; i < getWidgetCount(); i++) {
      setSpacingLayoutData(getWidget(i));
    }

    if (isAttached()) {
      forceLayout();
    }
  }

  /**
   * Sets both the horizontal and vertical spacing between child items (defaults to 2). Spacing is implemented using
   * layout data margins. Margins will be overridden by layout if spacing >= 0. Set spacing to -1 to prevent margin
   * calculations.
   * 
   * @param spacing the spacing
   */
  public void setSpacing(int spacing) {
    this.horizontalSpacing = spacing;
    this.verticalSpacing = spacing;
    for (int i = 0; i < getWidgetCount(); i++) {
      setSpacingLayoutData(getWidget(i));
    }

    if (isAttached()) {
      forceLayout();
    }
  }

  @Override
  protected void onFocus(Event event) {
    super.onFocus(event);

    // stopping default is causing fields to not blur when toolbar is clicked
    // event.preventDefault();
    event.stopPropagation();

    for (int i = 0; i < getWidgetCount(); i++) {
      Component c = (Component) getWidget(i);
      if (c.isEnabled() && !c.getFocusSupport().isIgnore()) {
        c.focus();
        break;
      }
    }
  }

  @Override
  protected void onInsert(int index, Widget child) {
    super.onInsert(index, child);
    setSpacingLayoutData(child);
    if (minButtonWidth != -1 && child instanceof CellButtonBase<?> && child != more) {
      ((CellButtonBase<?>) child).setMinWidth(minButtonWidth);
    }
  }

  private void setSpacingLayoutData(Widget child) {
    if (verticalSpacing == -1 && horizontalSpacing == -1) {
      return;
    }
    Object data = child.getLayoutData();
    HasMargins hasMargins = null;
    if (!(data instanceof HasMargins)) {
      hasMargins = new BoxLayoutData();
      child.setLayoutData(hasMargins);
    } else {
      hasMargins = (HasMargins) data;
    }

    Margins m = hasMargins.getMargins();

    if (m == null) {
      m = new Margins();
      hasMargins.setMargins(m);
    }

    if (verticalSpacing != -1) {
      int vs = (int) Math.round(verticalSpacing / 2d);
      m.setTop(vs);
      m.setBottom(vs);
    }

    if (horizontalSpacing != -1) {
      int hs = (int) Math.round(horizontalSpacing / 2d);
      m.setLeft(hs);
      m.setRight(hs);
    }

  }
}
