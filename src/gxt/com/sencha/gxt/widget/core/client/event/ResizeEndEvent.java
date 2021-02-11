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

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.ResizeEndHandler;

/**
 * Represents the source is resized.
 */
public class ResizeEndEvent extends GwtEvent<ResizeEndHandler> {

  /**
   * A widget that implements this interface is a public source of
   * {@link ResizeEndEvent} events.
   */
  public interface HasResizeEndHandlers {

    /**
     * Adds a {@link ResizeEndHandler} handler for {@link ResizeEndEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addResizeEndHandler(ResizeEndHandler handler);

  }

  /**
   * Handler class for {@link ResizeEndEvent} events.
   */
  public interface ResizeEndHandler extends EventHandler {

    /**
     * Called when a widget is resized.
     */
    void onResizeEnd(ResizeEndEvent event);
  }

  /**
   * Handler type.
   */
  private static Type<ResizeEndHandler> TYPE;
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ResizeEndHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<ResizeEndHandler>();
    }
    return TYPE;
  }

  private Component target;

  private NativeEvent nativeEvent;

  public ResizeEndEvent(Component target, Event event) {
    this.target = target;
    this.nativeEvent = event;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<ResizeEndHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the DOM native event.
   * 
   * @return the event
   */
  public NativeEvent getNativeEvent() {
    return nativeEvent;
  }

  public Resizable getSource() {
    return (Resizable) super.getSource();
  }

  /**
   * Returns the target component.
   * 
   * @return the component
   */
  public Component getTarget() {
    return target;
  }

  @Override
  protected void dispatch(ResizeEndHandler handler) {
    handler.onResizeEnd(this);
  }

}
