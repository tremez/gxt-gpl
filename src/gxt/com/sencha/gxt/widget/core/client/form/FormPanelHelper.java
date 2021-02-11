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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Utility methods for form panels.
 */
public class FormPanelHelper {

  /**
   * Returns all of the container's child field labels. Field labels in nested
   * containers are included in the returned list.
   * 
   * @param container the container
   * @return the fields
   */
  public static List<FieldLabel> getFieldLabels(HasWidgets container) {
    List<FieldLabel> fields = new ArrayList<FieldLabel>();
    getLabels(fields, container);
    return fields;
  }

  /**
   * Returns all of the container's child fields. Fields in nested containers
   * are included in the returned list.
   * 
   * @param container the container
   * @return the fields
   */
  public static List<IsField<?>> getFields(HasWidgets container) {
    List<IsField<?>> fields = new ArrayList<IsField<?>>();
    getFields(fields, container);
    return fields;
  }

  /**
   * Returns true if the form is valid.
   * 
   * @return true if all fields are valid
   */
  public static boolean isValid(HasWidgets container) {
    return isValid(container, false);
  }

  /**
   * Returns the form's valid state by querying all child fields.
   * 
   * @param preventMark true for silent validation (no invalid event and field
   *          is not marked invalid)
   * 
   * @return true if all fields are valid
   */
  public static boolean isValid(HasWidgets container, boolean preventMark) {
    boolean valid = true;
    for (IsField<?> f : getFields(container)) {
      if (!f.isValid(preventMark)) {
        valid = false;
      }
    }
    return valid;
  }

  /**
   * Resets all field values.
   */
  public static void reset(HasWidgets container) {
    for (IsField<?> f : getFields(container)) {
      f.reset();
    }
  }

  private static void getFields(List<IsField<?>> widgets, HasWidgets container) {
    Iterator<Widget> it = container.iterator();
    while (it.hasNext()) {
      Widget w = it.next();

      if (w instanceof IsField<?>) {
        widgets.add((IsField<?>) w);
      }

      if (w instanceof HasWidgets) {
        getFields(widgets, (HasWidgets) w);
      }
    }
  }

  private static void getLabels(List<FieldLabel> widgets, HasWidgets container) {
    Iterator<Widget> it = container.iterator();
    while (it.hasNext()) {
      Widget w = it.next();

      if (w instanceof FieldLabel) {
        widgets.add((FieldLabel) w);
        continue;
      }

      if (w instanceof HasWidgets) {
        getLabels(widgets, (HasWidgets) w);
      }
    }
  }
}
