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
package com.sencha.gxt.cell.core.client.form;

import java.text.ParseException;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.cell.HandlerManagerContext;
import com.sencha.gxt.widget.core.client.event.CellBeforeSelectionEvent;
import com.sencha.gxt.widget.core.client.event.CellSelectionEvent;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;

/**
 * A numeric cell with up / down arrows that increment / decrement the value.
 * 
 * @param <N>
 */
public class SpinnerFieldCell<N extends Number & Comparable<N>> extends NumberInputCell<N> implements HasBeforeSelectionHandlers<N>,
    HasSelectionHandlers<N> {

  public interface SpinnerFieldAppearance extends TwinTriggerFieldAppearance {

  }

  // Double.MIN_VALUE is the smallest positive nonzero value, so using negative MAX_VALUE instead
  private Number minValue = Double.MAX_VALUE  * -1d;
  private Number maxValue = Double.MAX_VALUE;
  private int cursorPosition;
  public SpinnerFieldCell(NumberPropertyEditor<N> propertyEditor) {
    this(propertyEditor, GWT.<SpinnerFieldAppearance> create(SpinnerFieldAppearance.class));
  }

  public SpinnerFieldCell(NumberPropertyEditor<N> propertyEditor, SpinnerFieldAppearance appearance) {
    super(propertyEditor, appearance);
  }

  /**
   * Adds a {@link BeforeSelectionEvent} handler. The handler will be passed an instance of
   * {@link BeforeSelectionEvent} which can in this case be cast to {@link CellBeforeSelectionEvent}.
   *
   * @param handler the handler
   * @return the registration for the event
   */
  @Override
  public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<N> handler) {
    return addHandler(handler, BeforeSelectionEvent.getType());
  }

  /**
   * Adds a {@link SelectionEvent} handler. The handler will be passed an instance of {@link SelectionEvent} which
   * can in this case be cast to {@link CellSelectionEvent}.
   *
   * @param handler the handler
   * @return the registration for the event
   */
  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<N> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  /**
   * Sets the increment value.
   *
   * @param context the context
   * @return the increment
   */
  public N getIncrement(Context context) {
    return getPropertyEditor().getIncrement();
  }

  /**
   * Returns the fields max value.
   *
   * @param context the context
   * @return the max value
   */
  public Number getMaxValue(Context context) {
    return maxValue;
  }

  /**
   * Returns the field's minimum value.
   *
   * @param context the context
   * @return the min value
   */
  public Number getMinValue(Context context) {
    return minValue;
  }

  /**
   * Sets the increment that should be used (defaults to 1d).
   *
   * @param increment the increment to set.
   */
  public void setIncrement(N increment) {
    getPropertyEditor().setIncrement(increment);
  }

  /**
   * Sets the field's max allowable value.
   *
   * @param maxValue the max value
   */
  public void setMaxValue(Number maxValue) {
    this.maxValue = maxValue.doubleValue();
  }

  /**
   * Sets the field's minimum allowed value.
   *
   * @param minValue the minimum value
   */
  public void setMinValue(Number minValue) {
    this.minValue = minValue.doubleValue();
  }

  protected void doSpin(Cell.Context context, XElement parent, N value, ValueUpdater<N> updater, boolean up) {
    if (!isReadOnly()) {
      // use the current value in the input element
      InputElement input = getInputElement(parent);
      String v = input.getValue();

      if (!"".equals(v)) {
        try {
          value = getPropertyEditor().parse(v);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      boolean cancelled = false;
      if (context instanceof HandlerManagerContext) {
        HandlerManager manager = ((HandlerManagerContext) context).getHandlerManager();
        CellBeforeSelectionEvent<N> event = CellBeforeSelectionEvent.fire(manager, context, value);
        if (event != null && event.isCanceled()) {
          cancelled = true;
        }
      } else {
        CellBeforeSelectionEvent<N> event = CellBeforeSelectionEvent.fire(this, context, value);
        if (!fireCancellableEvent(event)) {
          cancelled = true;
        }
      }

      if (!cancelled) {
        N newVal = null;
        if (up) {
          // if the user clicks up with no value and a min value > 0 we "jump" to the min value
          // otherwise, default behavior calling increment
          if (value == null && minValue.doubleValue() > 0) {
            try {
              newVal = getPropertyEditor().parse("" + minValue);
            } catch (ParseException e) {
            }
          } else {
            newVal = getPropertyEditor().incr(value);
          }

          if (newVal.doubleValue() > maxValue.doubleValue() || newVal.doubleValue() < minValue.doubleValue()) {
            return;
          }
          input.setValue(getPropertyEditor().render(newVal));
        } else {
          // if the user clicks down no no value a min value < 0 we "jump" to the min value
          // otherwise, default behavior calling descrement
          if (value == null && maxValue.doubleValue() < 0) {
            try {
              newVal = getPropertyEditor().parse("" + maxValue);
            } catch (ParseException e) {
            }
          } else {
            newVal = getPropertyEditor().decr(value);
          }

          if (newVal.doubleValue() > maxValue.doubleValue() || newVal.doubleValue() < minValue.doubleValue()) {
            return;
          }
          input.setValue(getPropertyEditor().render(newVal));
        }

        if (context instanceof HandlerManagerContext) {
          HandlerManager manager = ((HandlerManagerContext) context).getHandlerManager();
          CellSelectionEvent.fire(manager, context, newVal);
        } else {
          CellSelectionEvent.fire(this, lastContext, newVal);
        }
      }
    }
  }

  @Override
  protected void onMouseDown(XElement parent, NativeEvent event) {
    super.onMouseDown(parent, event);
    cursorPosition = Math.max(0, getCursorPos(parent));
  }

  @Override
  protected void onNavigationKey(Context context, Element parent, N value, NativeEvent event,
      ValueUpdater<N> valueUpdater) {
    super.onNavigationKey(context, parent, value, event, valueUpdater);
    switch (event.getKeyCode()) {
      case KeyCodes.KEY_UP:
        event.stopPropagation();
        event.preventDefault();
        doSpin(context, parent.<XElement> cast(), value, valueUpdater, true);
        break;
      case KeyCodes.KEY_DOWN:
        event.stopPropagation();
        event.preventDefault();
        doSpin(context, parent.<XElement> cast(), value, valueUpdater, false);
        break;
    }
  }

  @Override
  protected void onTriggerClick(Context context, XElement parent, NativeEvent event, N value, ValueUpdater<N> updater) {
    super.onTriggerClick(context, parent, event, value, updater);

    // EXTGWT-2402
    if (GXT.isIE9() || GXT.isGecko()) {
      getInputElement(parent).focus();
    }

    boolean positive = !getText(parent).startsWith("-");

    if (!isReadOnly() && !isDisabled()) {
      doSpin(context, parent, value, updater, true);
    }
    if (!GXT.isIE9() && !GXT.isGecko() && !GXT.isTouch()) {
      getInputElement(parent).focus();
    }

    boolean afterSpinPositive = !getText(parent).startsWith("-");

    if (positive && !afterSpinPositive) {
      ++cursorPosition;
    } else if (!positive && afterSpinPositive && cursorPosition > 1) {
      --cursorPosition;
    }

    int length = getText(parent).length();
    if (cursorPosition <= length) {
      select(parent, cursorPosition, 0);
    }
  }

  @Override
  protected void onTwinTriggerClick(Context context, XElement parent, NativeEvent event, N value,
      ValueUpdater<N> updater) {
    super.onTwinTriggerClick(context, parent, event, value, updater);

    boolean positive = !getText(parent).startsWith("-");

    // EXTGWT-2402
    if (GXT.isIE9() || GXT.isGecko()) {
      getInputElement(parent).focus();
    }
    if (!isReadOnly() && !isDisabled()) {
      doSpin(context, parent, value, updater, false);
    }
    if (!GXT.isIE9() && !GXT.isGecko() && !GXT.isTouch()) {
      getInputElement(parent).focus();
    }

    boolean afterSpinPositive = !getText(parent).startsWith("-");

    if (positive && !afterSpinPositive) {
      ++cursorPosition;
    } else if (!positive && afterSpinPositive && cursorPosition > 1) {
      --cursorPosition;
    }

    int length = getText(parent).length();
    if (cursorPosition <= length) {
      select(parent, cursorPosition, 0);
    }

  }

}
