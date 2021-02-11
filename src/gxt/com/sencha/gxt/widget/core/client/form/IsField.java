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
package com.sencha.gxt.widget.core.client.form;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.BlurEvent.HasBlurHandlers;

public interface IsField<T> extends IsWidget, LeafValueEditor<T>, HasBlurHandlers, HasValueChangeHandlers<T> {

  /**
   * Clears the value from the field.
   *
   * @see #clearInvalid() to remove validation messages
   * @see #reset() to restore to original value and remove validation messages
   */
  void clear();

  /**
   * Clear any invalid styles / messages for this field.
   */
  void clearInvalid();

  /**
   * Used to indicate that this field must be completed with its editing process, as it may be able to be removed from
   * the dom, hidden, or its current results used.
   */
  void finishEditing();

  /**
   * Returns a list of the current errors.
   * 
   * @return the errors, or empty list if no errors
   */
  List<EditorError> getErrors();

  /**
   * Returns whether or not the field value is currently valid.
   * 
   * @param preventMark {@code true} for silent validation (no invalid event and field is not marked invalid)
   * 
   * @return {@code true} if the value is valid, otherwise false
   */
  boolean isValid(boolean preventMark);

  /**
   * Resets the current field value to the originally loaded value and clears any validation messages.
   */
  void reset();

  /**
   * Validates the field value.
   * 
   * @param preventMark {@code true} to not mark the field valid and fire invalid event when invalid
   * @return {@code true} if valid, otherwise false
   */
  boolean validate(boolean preventMark);

}
