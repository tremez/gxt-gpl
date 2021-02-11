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

import java.util.Collections;
import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 * Fires after a field value marked invalid.
 */
public class InvalidEvent extends GwtEvent<InvalidHandler> {

  /**
   * Handler type.
   */
  private static Type<InvalidHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<InvalidHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<InvalidHandler>());
  }

  private List<EditorError> errors;

  public InvalidEvent(List<EditorError> errors) {
    this.errors = Collections.unmodifiableList(errors);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<InvalidHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public List<EditorError> getErrors() {
    return errors;
  }

  @Override
  public Field<?> getSource() {
    return (Field<?>) super.getSource();
  }

  @Override
  protected void dispatch(InvalidHandler handler) {
    handler.onInvalid(this);
  }
  
  /**
   * Handler class for {@link InvalidEvent} events.
   */
  public interface InvalidHandler extends EventHandler {

    /**
     * Called when a field becomes valid.
     */
    void onInvalid(InvalidEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link InvalidEvent} events.
   */
  public interface HasInvalidHandlers {

    /**
     * Adds a {@link InvalidHandler} handler for {@link InvalidEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addInvalidHandler(InvalidHandler handler);

  }

}
