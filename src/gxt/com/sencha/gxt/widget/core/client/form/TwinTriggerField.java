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

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.TwinTriggerFieldCell;
import com.sencha.gxt.widget.core.client.event.TwinTriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TwinTriggerClickEvent.HasTwinTriggerClickHandlers;
import com.sencha.gxt.widget.core.client.event.TwinTriggerClickEvent.TwinTriggerClickHandler;

/**
 * An abstract base class for an input field and two clickable triggers. The
 * purpose of the triggers is defined by the derived class (e.g. modifying the
 * value of the input field).
 * 
 * @param <T> the field type
 */
public abstract class TwinTriggerField<T> extends TriggerField<T> implements HasTwinTriggerClickHandlers {

  /**
   * Creates a trigger field with the specified cell and property editor.
   * 
   * @param cell renders the trigger field
   * @param propertyEditor performs string / value conversions and other
   *          operations
   */
  public TwinTriggerField(TwinTriggerFieldCell<T> cell, PropertyEditor<T> propertyEditor) {
    super(cell);
    setPropertyEditor(propertyEditor);
  }

  @Override
  public HandlerRegistration addTwinTriggerClickHandler(TwinTriggerClickHandler handler) {
    return addHandler(handler, TwinTriggerClickEvent.getType());
  }

}
