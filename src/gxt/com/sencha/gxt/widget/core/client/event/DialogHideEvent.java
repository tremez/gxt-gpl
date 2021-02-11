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
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;

/**
 * Fires after a dialog is hidden.
 */
public class DialogHideEvent extends GwtEvent<DialogHideHandler> {

  /**
   * Handler for {@link DialogHideEvent} events.
   */
  public interface DialogHideHandler extends EventHandler {

    /**
     * Called after a dialog is hidden.
     * 
     * @param event the {@link DialogHideEvent} that was fired
     */
    void onDialogHide(DialogHideEvent event);

  }

  /**
   * An object that implements this interface is a public source of {@link DialogHideEvent} events.
   */
  public interface HasDialogHideHandlers {

    /**
     * Adds a {@link DialogHideHandler} handler for {@link DialogHideEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addDialogHideHandler(DialogHideHandler handler);

  }

  /**
   * Handler type.
   */
  private static Type<DialogHideHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<DialogHideHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<DialogHideHandler>();
    }
    return TYPE;
  }

  /**
   * The button that resulted in the DialogHideEvent or null if none of the predefined buttons did.
   */
  private final PredefinedButton hideButton;

  /**
   * Creates a DialogHideEvent that is by default not associated with a predefined button. If the dialog hide event is
   * associated with a predefined button, use the {@link #DialogHideEvent(PredefinedButton)} form of the constructor.
   */
  public DialogHideEvent() {
    this.hideButton = null;
  }

  /**
   * Creates a DialogHideEvent and initializes the value of predefined button that generated the event.
   * 
   * @param hideButton the predefined button that that generated this DialogHideEvent, or null if the hide event was not
   *          generated as a result of one of the predefined buttons
   */
  public DialogHideEvent(PredefinedButton hideButton) {
    this.hideButton = hideButton;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<DialogHideHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Gets the predefined button that resulted in the DialogHideEvent, or null if none of the predefined buttons did
   * (e.g. the user clicked on the close tool button or the dialog was hidden under program control).
   * 
   * @return the predefined button that resulted in the DialogHideEvent, or null if none of the predefined buttons did
   */
  public PredefinedButton getHideButton() {
    return hideButton;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(DialogHideHandler handler) {
    handler.onDialogHide(this);
  }

}
