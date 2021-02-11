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
package com.sencha.gxt.widget.core.client.form.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 * Group field validation. This is useful with wizards validation behavior.
 *
 * <pre>
 * {@code
 * final GroupingValidHandler validGroup = new GroupingValidHandler(false);
 * validGroup.add(field1);
 * validGroup.add(field2);
 * validGroup.add(field3);
 * 
 * final TextButton finishButton = new TextButton("Can Click if Validators are Passing");
 * finishButton.setEnabled(false);
 *
 * validGroup.addInvalidChangeEventHandler(new InvalidHandler() {
 *   @Override
 *   public void onInvalid(InvalidEvent event) {
 *     finishButton.setEnabled(validGroup.isValid());
 *   }
 *  });
 *
 *  validGroup.addValidChangeEventHandler(new ValidHandler() {
 *    @Override
 *    public void onValid(ValidEvent event) {
 *      finishButton.setEnabled(validGroup.isValid());
 *    }
 *  });
 * }
 * </pre>
 */
public class GroupingValidHandler {

  /**
   * A model to track the fields valid or invalid state.
   */
  protected class Valid {
    private Field<?> field;
    private GroupingHandlerRegistration handlerRegistration;
    private boolean valid;

    public Valid(Field<?> field, boolean defaultValidState) {
      this.field = field;
      this.valid = defaultValidState;
    }

    protected void addHandlers() {
      if (handlerRegistration != null) {
        handlerRegistration.removeHandler();
        handlerRegistration = null;
      }

      HandlerRegistration validRegistration = field.addValidHandler(new ValidHandler() {
        @Override
        public void onValid(ValidEvent event) {
          valid = true;
          eventBus.fireEvent(new ValidEvent());
        }
      });

      HandlerRegistration invalidRegistration = field.addInvalidHandler(new InvalidHandler() {
        @Override
        public void onInvalid(InvalidEvent event) {
          valid = false;
          eventBus.fireEvent(new InvalidEvent(getErrors()));
        }
      });

      handlerRegistration = new GroupingHandlerRegistration();
      handlerRegistration.add(validRegistration);
      handlerRegistration.add(invalidRegistration);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      Valid other = (Valid) obj;
      if (field == null && other.field != null) {
        return false;
      } else if (!field.equals(other.field)) {
        return false;
      }
      return true;
    }

    public Field<?> getField() {
      return field;
    }

    @Override
    public int hashCode() {
      return 31 + field.hashCode();
    }

    public boolean isValid() {
      return valid;
    }

    protected void removeHandlers() {
      if (handlerRegistration != null) {
        handlerRegistration.removeHandler();
      }
    }
  }

  private final boolean defaultValidInvalidStateOfField;
  private final EventBus eventBus;

  private final Map<Field<?>, Valid> valids;

  /**
   * Track the invalid or valid state of a group of fields validators. The initial state of the fields will be valid.
   */
  public GroupingValidHandler() {
    this(true);
  }

  /**
   * Track the invalid or valid state of a group of fields validators.
   *
   * @param defaultValidInvalidStateOfField set the initial valid state of the field.
   */
  public GroupingValidHandler(boolean defaultValidInvalidStateOfField) {
    this.defaultValidInvalidStateOfField = defaultValidInvalidStateOfField;
    valids = new HashMap<Field<?>, Valid>();
    eventBus = new SimpleEventBus();
  }

  /**
   * Add a field to the group to observe valid and invalid state of the validators for that field.
   *
   * @param field a GXT field.
   */
  public void add(Field<?> field) {
    add(field, defaultValidInvalidStateOfField);
  }

  /**
   * Add a field to the group to observe valid and invalid state of the validators for that field.
   *
   * @param field a GXT field.
   * @param defaultValidState sets the default valid state of the field.
   */
  public void add(Field<?> field, boolean defaultValidState) {
    Valid valid = new Valid(field, defaultValidState);
    valid.addHandlers();
    valids.put(field, valid);
  }

  /**
   * Observe when this group of fields validators becomes invalid. Use {@link #isValid()} to determine state.
   */
  public HandlerRegistration addInvalidChangeEventHandler(InvalidHandler handler) {
    return eventBus.addHandler(InvalidEvent.getType(), handler);
  }

  /**
   * Observe when this group of fields validators becomes valid. Use {@link #isValid()} to determine state.
   */
  public HandlerRegistration addValidChangeEventHandler(ValidHandler handler) {
    return eventBus.addHandler(ValidEvent.getType(), handler);
  }

  /**
   * Return a list of errors for the given group of fields.
   *
   * @return the editor errors in the group ore return an empty list.
   */
  public List<EditorError> getErrors() {
    List<EditorError> errors = new ArrayList<EditorError>();
    for (Valid valid : valids.values()) {
      if (valid != null && !valid.isValid()) {
        errors.addAll(valid.getField().getErrors());
      }
    }

    return errors;
  }

  /**
   * Is the group valid.
   *
   * @return false if one of the validators in the group is invalid.
   */
  public boolean isValid() {
    for (Valid valid : valids.values()) {
      if (valid != null && !valid.isValid()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Is the field valid in the group?
   *
   * @param field the field in the group.
   * @return false if the field in the group is invalid.
   */
  public boolean isValid(Field<?> field) {
    Valid valid = valids.get(field);
    if (valid == null) {
      return true;
    }
    return valid.isValid();
  }

  /**
   * Remove the field from the group.
   */
  public void remove(Field<?> field) {
    Valid valid = valids.get(field);
    if (valid != null) {
      valid.removeHandlers();
      valids.remove(valid);
    }
  }

  /**
   * Returns the EventBus used.
   */
  protected EventBus getEventBus() {
    return eventBus;
  }

  /**
   * Returns the models that track the fields valid state.
   */
  protected Map<Field<?>, Valid> getValids() {
    return valids;
  }

  /**
   * Returns the initial valid state set for fields.
   */
  protected boolean isDefaultValidInvalidStateOfField() {
    return defaultValidInvalidStateOfField;
  }

}
