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
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;

/**
 * Fires after a widget is removed from a container.
 */
public class RemoveEvent extends GwtEvent<RemoveHandler> {

  /**
   * Handler type.
   */
  private static Type<RemoveHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RemoveHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<RemoveHandler>();
    }
    return TYPE;
  }

  private Widget widget;

  public RemoveEvent(Widget widget) {
    this.widget = widget;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Type<RemoveHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public Container getSource() {
    return (Container) super.getSource();
  }

  /**
   * Returns the widget that was removed.
   * 
   * @return the removed widget
   */
  public Widget getWidget() {
    return widget;
  }

  @Override
  protected void dispatch(RemoveHandler handler) {
    handler.onRemove(this);
  }
  
  /**
   * Handler for {@link RemoveEvent} events.
   */
  public interface RemoveHandler extends EventHandler {

    /**
     * Called before a widget is removed to a container.
     * 
     * @param event the {@link RemoveEvent} that was fired
     */
    void onRemove(RemoveEvent event);

  }
  
  public interface HasRemoveHandlers {

    /**
     * Adds a {@link RemoveHandler} handler for
     * {@link RemoveEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addRemoveHandler(RemoveHandler handler);
  }

}
