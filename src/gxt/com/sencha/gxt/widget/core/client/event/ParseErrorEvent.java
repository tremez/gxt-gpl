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

import java.text.ParseException;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;

public class ParseErrorEvent extends GwtEvent<ParseErrorHandler> {

  /**
   * A widget that implements this interface is a public source of
   * {@link ParseErrorEvent} events.
   */
  public interface HasParseErrorHandlers {

    /**
     * Adds a {@link ParseErrorHandler} handler for {@link ParseErrorEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addParseErrorHandler(ParseErrorHandler handler);
  }

  /**
   * Handler for {@link ParseErrorEvent} events.
   */
  public interface ParseErrorHandler extends EventHandler {

    void onParseError(ParseErrorEvent event);

  }

  /**
   * Handler type.
   */
  private static Type<ParseErrorHandler> TYPE;

  private ParseException exception;
  private String value;

  public ParseErrorEvent(String errorValue, ParseException exception) {
    this.value = errorValue;
    this.exception = exception;
  }

  /**
   * Returns the parse exception.
   * 
   * @return the exception
   */
  public ParseException getException() {
    return exception;
  }

  /**
   * Returns the value that could not be parsed.
   * 
   * @return the value
   */
  public String getErrorValue() {
    return value;
  }

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ParseErrorHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<ParseErrorHandler>());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Type<ParseErrorHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(ParseErrorHandler handler) {
    handler.onParseError(this);
  }

}
