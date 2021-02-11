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
package com.sencha.gxt.widget.core.client.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.menu.MenuItem.MenuItemAppearance;

/**
 * Allows any widget to be placed in a menu. As of 2.0, any widget can be
 * added directly to a Menu therefore reducing the need for this class.
 * AdapterMenuItem allows arbitrary widgets to be added to Menu and also
 * configured as Items.
 * 
 * <p />
 * For example, when adding a ComboBox to a Menu, the Menu will be closed when a
 * item from the combo drop down list is clicked if the combo is added directly
 * to the Menu. To stop the menu from being closed, an AdapterMenuItem can be
 * used so that @link {@link #setHideOnClick(boolean)} can be called.
 */
public class AdapterMenuItem extends Item {

  /**
   * The wrapped widget.
   */
  protected Widget widget;

  protected boolean manageFocus = false;

  private boolean needsIndent = true;

  /**
   * Creates a new adapter.
   * 
   * @param widget the widget to be adapted
   */
  public AdapterMenuItem(Widget widget) {
    super(GWT.<MenuItemAppearance>create(MenuItemAppearance.class));
    assert widget != null : "Widget may not be null";
    widget.removeFromParent();
    this.widget = widget;
    setParent(this, widget);

    setElement((Element) widget.getElement());
  }

  @Override
  public XElement getElement() {
    // we need this because of lazy rendering
    return widget.getElement().cast();
  }

  /**
   * Returns the wrapped widget.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return widget;
  }

  @Override
  public boolean isAttached() {
    if (widget != null) {
      return widget.isAttached();
    }
    return false;
  }

  /**
   * Returns true if the widget will be indented.
   * 
   * @return true if indented
   */
  public boolean isNeedsIndent() {
    return needsIndent;
  }

  @Override
  public void onBrowserEvent(Event event) {
    // Fire any handler added to the composite itself.
    super.onBrowserEvent(event);

    // Delegate events to the widget.
    widget.onBrowserEvent(event);
  }

  /**
   * True to indent the widget to account for the icon space (defaults to true).
   * 
   * @param needsIndent true to indent
   */
  public void setNeedsIndent(boolean needsIndent) {
    this.needsIndent = needsIndent;
  }

  @Override
  protected void onAttach() {
    ComponentHelper.doAttach(widget);
    DOM.setEventListener(getElement(), this);
    onLoad();
  }

  @Override
  protected void onClick(NativeEvent be) {
    if (widget instanceof Component) {
      ((Component) widget).focus();
    }
  }

  @Override
  protected void onDetach() {
    try {
      onUnload();
    } finally {
      ComponentHelper.doDetach(widget);
    }
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    if (widget instanceof Component) {
      ((Component) widget).disable();
    }
  }

  /**
   * Returns true if the adapter manages focus for the wrapped widget.
   * 
   * @return true if focus being managed
   */
  public boolean isManageFocus() {
    return manageFocus;
  }

  /**
   * True to move focus to wrapped widget when the enter key is pressed and
   * remove focus of wrapped widget when escape is pressed (defaults to false).
   * 
   * @param manageFocus true to manage focus
   */
  public void setManageFocus(boolean manageFocus) {
    this.manageFocus = manageFocus;
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    if (widget instanceof Component) {
      ((Component) widget).enable();
    }
  }

  @Override
  protected boolean onEscape() {
    if (manageFocus) {
      focus();
      return false;
    }
    return super.onEscape();
  }

  private native void setParent(Widget parent, Widget child) /*-{
    child.@com.google.gwt.user.client.ui.Widget::parent = parent;
  }-*/;

}
