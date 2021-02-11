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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;

/**
 * Fires after a widget is added to a container.
 */
public class AddEvent extends GwtEvent<AddHandler> {

  /**
   * Handler type.
   */
  private static Type<AddHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<AddHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<AddHandler>();
    }
    return TYPE;
  }

  private Widget widget;
  private int index;

  public AddEvent(Widget widget, int index) {
    this.widget = widget;
    this.index = index;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Type<AddHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the insert index of the widget that was added.
   * 
   * @return the insert index
   */
  public int getIndex() {
    return index;
  }

  public Container getSource() {
    return (Container) super.getSource();
  }

  /**
   * Returns the widget that was added.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return widget;
  }

  @Override
  protected void dispatch(AddHandler handler) {
    handler.onAdd(this);
  }
  
  /**
   * Handler for {@link AddEvent} events.
   */
  public interface AddHandler extends EventHandler {

    /**
     * Called after a widget is added to a container.
     * 
     * @param event the {@link AddEvent} that was fired
     */
    void onAdd(AddEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link AddEvent} events.
   */
  public interface HasAddHandlers {

    /**
     * Adds a {@link AddHandler} handler for {@link AddEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addAddHandler(AddHandler handler);
  }

}
