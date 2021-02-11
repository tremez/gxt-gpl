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
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;

/**
 * Fires after a widget is blurred. Unlike the GWT
 * {@link com.google.gwt.event.dom.client.BlurEvent}, this event is NOT a dom
 * event to allow components flexibility in when the focus event is fired.
 */
public class BlurEvent extends GwtEvent<BlurHandler> {

  /**
   * Handler for {@link BlurEvent} events.
   */
  public interface BlurHandler extends EventHandler {

    void onBlur(BlurEvent event);

  }

  /**
   * A widget that implements this interface is a public source of
   * {@link BlurEvent} events.
   */
  public interface HasBlurHandlers {

    /**
     * Adds a {@link BlurHandler} handler for {@link BlurEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBlurHandler(BlurHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<BlurHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BlurHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BlurHandler>());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Type<BlurHandler> getAssociatedType() {
    return (Type) TYPE;
  }
  
  public Component getSource() {
    return (Component) super.getSource();
  }
  
  @Override
  protected void dispatch(BlurHandler handler) {
    handler.onBlur(this);
  }

}
