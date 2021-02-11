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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.ActivateEvent;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.HasActivateHandlers;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.HasDeactivateHandlers;
import com.sencha.gxt.widget.core.client.event.XEvent;

/**
 * The base class for all items that render into menus. BaseItem provides
 * default rendering, activated state management.
 */
public abstract class Item extends Component implements HasSelectionHandlers<Item>, HasActivateHandlers<Item>,
    HasDeactivateHandlers<Item>, HasBeforeSelectionHandlers<Item> {

  /**
   * True if this item can be visually activated (defaults to false).
   */
  protected boolean canActivate;

  /**
   * True to hide the containing menu after this item is clicked (defaults to
   * true).
   */
  protected boolean hideOnClick = true;

  private ItemAppearance appearance;

  public interface ItemAppearance {

    void onActivate(XElement parent);

    void onDeactivate(XElement parent);

  }

  public Item(ItemAppearance appearance) {
    this.appearance = appearance;
  }

  @Override
  public HandlerRegistration addActivateHandler(ActivateHandler<Item> handler) {
    return addHandler(handler, ActivateEvent.getType());
  }

  @Override
  public HandlerRegistration addDeactivateHandler(DeactivateHandler<Item> handler) {
    return addHandler(handler, DeactivateEvent.getType());
  }

  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<Item> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  @Override
  public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<Item> handler) {
    return addHandler(handler, BeforeSelectionEvent.getType());
  }

  public ItemAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the hide on click state.
   * 
   * @return the hide on click state
   */
  public boolean getHideOnClick() {
    return hideOnClick;
  }

  /**
   * Returns true if the widget can be activated.
   * 
   * @return true if can be activated
   */
  public boolean isCanActivate() {
    return this.canActivate;
  }

  /**
   * Return true if the widget can be hidden on click
   *
   * @return true if the widget can be hidden
   */
  public boolean isHideOnClick() {
    return hideOnClick;
  }

  /**
   * Sets whether the item can be activated (defaults to false).
   * 
   * @param canActivate true to activate
   */
  public void setCanActivate(boolean canActivate) {
    this.canActivate = canActivate;
  }

  /**
   * True to hide the containing menu after this item is clicked (defaults to
   * true).
   * 
   * @param hideOnClick true to hide, otherwise false
   */
  public void setHideOnClick(boolean hideOnClick) {
    this.hideOnClick = hideOnClick;
  }

  protected void activate(boolean autoExpand) {
    if (disabled) {
      return;
    }

    appearance.onActivate(getElement());

    fireEvent(new ActivateEvent<Item>(this));
    if (getParent() instanceof HasActivateHandlers<?>) {
      getParent().fireEvent(new ActivateEvent<Item>(this));
    }

  }

  protected void deactivate() {
    appearance.onDeactivate(getElement());

    fireEvent(new DeactivateEvent<Item>(this));
    if (getParent() instanceof HasDeactivateHandlers<?>) {
      getParent().fireEvent(new DeactivateEvent<Item>(this));
    }
  }

  protected void expandMenu(boolean autoActivate) {

  }

  protected void handleClick(NativeEvent be) {
    if (hideOnClick && getParent() instanceof Menu) {
      ((Menu) getParent()).hide(true);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  protected void onClick(NativeEvent be) {
    be.<XEvent> cast().stopEvent();

    BeforeSelectionEvent<Item> itemEvent = BeforeSelectionEvent.fire(this, this);

    BeforeSelectionEvent<Item> menuEvent = null;

    if (getParent() instanceof HasBeforeSelectionHandlers<?>) {
      menuEvent = BeforeSelectionEvent.fire((HasBeforeSelectionHandlers) getParent(), this);
    }

    if (!disabled && (itemEvent == null || !itemEvent.isCanceled()) && (menuEvent == null || !menuEvent.isCanceled())) {
      if (getParent() instanceof HasSelectionHandlers<?>) {
        SelectionEvent.fire((HasSelectionHandlers) getParent(), this);
      }

      SelectionEvent.fire(this, this);
      handleClick(be);
    }
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    Element li = getElement().getParentElement();
    if (li != null) {
      li.addClassName(disabledStyle);
    }
  }
  
  @Override
  protected void onAttach() {
    super.onAttach();
    if (!isEnabled()) {
      Element li = getElement().getParentElement();
      if (li != null) {
        li.addClassName(disabledStyle);
      }
    }
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    Element li = getElement().getParentElement();
    if (li != null) {
      li.removeClassName(disabledStyle);
    }
  }

  protected boolean onEscape() {
    return true;
  }

  protected boolean shouldDeactivate(NativeEvent ce) {
    return true;
  }

}
