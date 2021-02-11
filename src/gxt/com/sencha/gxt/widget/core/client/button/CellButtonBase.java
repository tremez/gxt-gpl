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
package com.sencha.gxt.widget.core.client.button;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHTML;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonArrowAlign;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.HasIcon;
import com.sencha.gxt.widget.core.client.cell.CellComponent;
import com.sencha.gxt.widget.core.client.cell.DefaultHandlerManagerContext;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent.ArrowSelectHandler;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent.HasArrowSelectHandlers;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.HasBeforeSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

public class CellButtonBase<C> extends CellComponent<C> implements HasHTML, HasIcon, HasBeforeSelectHandlers,
    HasSelectHandlers, HasArrowSelectHandlers, HasSafeHtml {

  public CellButtonBase() {
    this(new ButtonCell<C>());
  }

  public CellButtonBase(ButtonCell<C> cell) {
    this(cell, null);
  }

  public CellButtonBase(ButtonCell<C> cell, C initialValue) {
    super(cell, initialValue, null, true);

    setAllowTextSelection(false);

    sinkEvents(Event.ONCLICK | Event.TOUCHEVENTS);
  }

  @Override
  public HandlerRegistration addArrowSelectHandler(ArrowSelectHandler handler) {
    return addHandler(handler, ArrowSelectEvent.getType());
  }

  @Override
  public HandlerRegistration addBeforeSelectHandler(BeforeSelectHandler handler) {
    return addHandler(handler, BeforeSelectEvent.getType());
  }

  @Override
  public HandlerRegistration addSelectHandler(SelectHandler handler) {
    return addHandler(handler, SelectEvent.getType());
  }

  /**
   * Returns the button's arrow alignment.
   * 
   * @return the arrow alignment
   */
  public ButtonArrowAlign getArrowAlign() {
    return getCell().getArrowAlign();
  }

  @Override
  public ButtonCell<C> getCell() {
    return (ButtonCell<C>) super.getCell();
  }

  @Override
  public String getHTML() {
    return getCell().getHTML();
  }

  @Override
  public ImageResource getIcon() {
    return getCell().getIcon();
  }

  /**
   * Returns the button's icon alignment.
   *
   * @return the icon alignment
   */
  public IconAlign getIconAlign() {
    return getCell().getIconAlign();
  }

  /**
   * Returns the button's menu (if it has one).
   *
   * @return the menu
   */
  public Menu getMenu() {
    return getCell().getMenu();
  }

  /**
   * Returns the button's menu alignment.
   *
   * @return the menu alignment
   */
  public AnchorAlignment getMenuAlign() {
    return getCell().getMenuAlign();
  }

  /**
   * Returns the button's minimum width.
   *
   * @return the minWidth the minimum width
   */
  public int getMinWidth() {
    return getCell().getMinWidth();
  }

  /**
   * Returns false if mouse over effect is disabled.
   *
   * @return false if mouse effects disabled
   */
  public boolean getMouseEvents() {
    return getCell().getMouseEvents();
  }

  /**
   * Returns the button's scale.
   * 
   * @return the button scale
   */
  public ButtonScale getScale() {
    return getCell().getScale();
  }

  @Override
  public String getText() {
    return getCell().getText();
  }

  /**
   * Hide this button's menu (if it has one).
   */
  public void hideMenu() {
    getCell().hideMenu();
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    switch (event.getTypeInt()) {
      case Event.ONCLICK:
        onClick(event);
        break;
    }
  }

  @Override
  protected void onRedraw() {
    super.onRedraw();
    setTabIndex(tabIndex);
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    getCell().getAppearance().onFocus(getElement(), false);
    getCell().getAppearance().onOver(getElement(), false);
  }

  /**
   * Sets the arrow alignment (defaults to RIGHT).
   *
   * @param arrowAlign the arrow alignment
   */
  public void setArrowAlign(ButtonArrowAlign arrowAlign) {
    getCell().setArrowAlign(arrowAlign);
    redraw();
  }

  @Override
  public void setHTML(SafeHtml html) {
    setHTML(html.asString());
  }

  @Override
  public void setHTML(String html) {
    getCell().setHTML(html);
    redraw();
  }

  @Override
  public void setIcon(ImageResource icon) {
    getCell().setIcon(icon);
    redraw();
  }

  /**
   * Sets the icon alignment (defaults to LEFT).
   *
   * @param iconAlign the icon alignment
   */
  public void setIconAlign(IconAlign iconAlign) {
    getCell().setIconAlign(iconAlign);
    redraw();
  }

  /**
   * Sets the button's menu.
   *
   * @param menu the menu
   */
  @UiChild(limit = 1, tagname = "menu")
  public void setMenu(Menu menu) {
    getCell().setMenu(menu);
    redraw();
  }

  /**
   * Sets the position to align the menu to, see {@link XElement#alignTo} for
   * more details (defaults to 'tl-bl?', pre-render).
   *
   * @param menuAlign the menu alignment
   */
  public void setMenuAlign(AnchorAlignment menuAlign) {
    getCell().setMenuAlign(menuAlign);
    redraw();
  }

  /**
   * Sets he minimum width for this button (used to give a set of buttons a
   * common width)
   *
   * @param minWidth the minimum width
   */
  public void setMinWidth(int minWidth) {
    getCell().setMinWidth(minWidth);
    redraw();
  }

  /**
   * False to disable visual cues on mouseover, mouseout and mousedown (defaults
   * to true).
   *
   * @param handleMouseEvents false to disable mouse over changes
   */
  public void setMouseEvents(boolean handleMouseEvents) {
    getCell().setMouseEvents(handleMouseEvents);
  }

  /**
   * Sets the button's scale.
   * 
   * @param scale the scale
   */
  public void setScale(ButtonScale scale) {
    getCell().setScale(scale);
    redraw();
  }

  @Override
  public void setTabIndex(int tabIndex) {
    this.tabIndex = tabIndex;
    getFocusEl().setTabIndex(tabIndex);
  }

  @Override
  public void setText(String text) {
    getCell().setText(text);
    redraw();
  }

  /**
   * Show this button's menu (if it has one).
   */
  public void showMenu() {
    getCell().showMenu(getElement());
  }
  
  @Override
  protected Context createContext() {
    return new DefaultHandlerManagerContext(0, 0, getKey(getValue()), ComponentHelper.ensureHandlers(this));
  }

  protected void onClick(Event event) {

  }

}
