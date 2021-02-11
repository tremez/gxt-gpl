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
package com.sencha.gxt.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.event.ArrowClickEvent.ArrowClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * Fires after a button's arrow is clicked.
 */
public class ArrowClickEvent extends GwtEvent<ArrowClickHandler> {

  /**
   * Handler type.
   */
  private static Type<ArrowClickHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ArrowClickHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<ArrowClickHandler>();
    }
    return TYPE;
  }

  private Menu menu;

  public ArrowClickEvent(Menu menu) {
    this.menu = menu;
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<ArrowClickHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public Menu getMenu() {
    return menu;
  }

  public SplitButton getSource() {
    return (SplitButton) super.getSource();
  }

  @Override
  protected void dispatch(ArrowClickHandler handler) {
    handler.onArrowClick(this);
  }
  
  /**
   * Handler for {@link ArrowClickEvent} events.
   */
  public interface ArrowClickHandler extends EventHandler {

    /**
     * Called when the button's arrow is clicked.
     * 
     * @param event the {@link ArrowClickEvent} that was fired
     */
    void onArrowClick(ArrowClickEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link ArrowClickEvent} events.
   */
  public interface HasArrowClickHandlers {

    /**
     * Adds a {@link SelectHandler} handler for {@link ArrowClickEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addArrowClickHandler(ArrowClickHandler handler);
  }

}
