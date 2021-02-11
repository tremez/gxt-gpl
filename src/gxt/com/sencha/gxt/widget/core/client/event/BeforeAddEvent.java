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
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.event.BeforeAddEvent.BeforeAddHandler;

/**
 * Fires before a widget is added to a container.
 */
public class BeforeAddEvent extends GwtEvent<BeforeAddHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<BeforeAddHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeAddHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<BeforeAddHandler>();
    }
    return TYPE;
  }

  private Widget widget;
  private int index;
  private boolean cancelled;

  public BeforeAddEvent(Widget widget, int index) {
    this.widget = widget;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Type<BeforeAddHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the insert index of the widget to be added.
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
   * Returns the widget to be added.
   * 
   * @return the widget to be added
   */
  public Widget getWidget() {
    return widget;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  protected void dispatch(BeforeAddHandler handler) {
    handler.onBeforeAdd(this);
  }
  
  /**
   * Handler for {@link BeforeAddEvent} events.
   */
  public interface BeforeAddHandler extends EventHandler {

    /**
     * Called before a widget is added to a container. The action can be cancelled
     * using {@link BeforeAddEvent#setCancelled(boolean)}.
     * 
     * @param event the {@link BeforeAddEvent} that was fired
     */
    void onBeforeAdd(BeforeAddEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeAddEvent} events.
   */
  public interface HasBeforeAddHandlers {

    /**
     * Adds a {@link BeforeAddEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeAddHandler(BeforeAddHandler handler);
  }

}
