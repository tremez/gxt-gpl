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
package com.sencha.gxt.widget.core.client.grid.filters;

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.NumberFilterHandler;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.filters.RangeMenu.RangeItem;

/**
 * Filter class for numeric fields. by default, converts data to {@code double} before comparing, but this behavior can
 * be change by overriding {@link #equals(Number, Number)} and {@link #greaterThan(Number, Number)}. See {@link Filter}
 * for more information.
 * 
 * @param <M> the model in the store and in each grid row
 * @param <V> the numeric type in the column to filter
 */
public class NumericFilter<M, V extends Number & Comparable<V>> extends Filter<M, V> {

  /**
   * The default locale-sensitive messages used by this class.
   */
  public class DefaultNumericFilterMessages implements NumericFilterMessages {
    @Override
    public String emptyText() {
      return DefaultMessages.getMessages().numericFilter_emptyText();
    }
  }

  /**
   * The locale-sensitive messages used by this class.
   */
  public interface NumericFilterMessages {
    String emptyText();
  }

  private final NumberPropertyEditor<V> propertyEditor;
  private List<RangeItem> rangeItems = new ArrayList<RangeItem>();
  private RangeMenu<M, V> rangeMenu;
  private NumericFilterMessages messages = new DefaultNumericFilterMessages();
  private int width = 125;

  /**
   * Creates a numeric filter for the specified value provider. See {@link Filter#Filter(ValueProvider)} for more
   * information.
   * 
   * @param valueProvider the value provider
   * @param propertyEditor property editor for numeric type {@code <V>}
   */
  public NumericFilter(ValueProvider<? super M, V> valueProvider, NumberPropertyEditor<V> propertyEditor) {
    super(valueProvider);

    this.propertyEditor = propertyEditor;

    setHandler(new NumberFilterHandler<V>(propertyEditor));

    rangeItems.add(RangeItem.LESSTHAN);
    rangeItems.add(RangeItem.GREATERTHAN);
    rangeItems.add(RangeItem.EQUAL);

    rangeMenu = new RangeMenu<M, V>(this);
    menu = rangeMenu;
    rangeMenu.setRangeItems(rangeItems);

    setWidth(getWidth());
  }

  /**
   * Sets the less than value.
   * 
   * @param value the value
   */
  public void setLessThanValue(V value) {
    rangeMenu.lt.setValue(value);
  }

  /**
   * Sets the greater than value.
   * 
   * @param value the value
   */
  public void setGreaterThanValue(V value) {
    rangeMenu.gt.setValue(value);
  }

  /**
   * Sets the equal value.
   * 
   * @param value the equal value
   */
  public void setEqualValue(V value) {
    rangeMenu.eq.setValue(value);
  }

  public void setValue(List<FilterConfig> values) {
    rangeMenu.setValue(values);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<FilterConfig> getFilterConfig() {
    return (List<FilterConfig>) getValue();
  }

  /**
   * Returns the locale-sensitive messages used by this class.
   * 
   * @return the local-sensitive messages used by this class.
   */
  public NumericFilterMessages getMessages() {
    return messages;
  }

  @Override
  public Object getValue() {
    return rangeMenu.getValue();
  }

  /**
   * Returns the width used for the range sub-menu.
   * 
   * @return the width used for the range sub-menu
   */
  public int getWidth() {
    return width;
  }

  @Override
  public boolean isActivatable() {
    if (rangeMenu.eq != null && rangeMenu.eq.getCurrentValue() != null) {
      return true;
    }
    if (rangeMenu.lt != null && rangeMenu.lt.getCurrentValue() != null) {
      return true;
    }
    if (rangeMenu.gt != null && rangeMenu.gt.getCurrentValue() != null) {
      return true;
    }
    return false;
  }

  public void setMessages(NumericFilterMessages messages) {
    this.messages = messages;
    rangeMenu.setEmptyText(messages.emptyText());
  }

  /**
   * Sets the width to use for the range sub-menu (defaults to 125).
   * 
   * @param width the width used for the range sub-menu.
   */
  public void setWidth(int width) {
    this.width = width;
    rangeMenu.setFieldWidth(width);
  }

  /**
   * Compares the two values for equality. Can be overridden to provide an equality check that allows for floating point
   * approximation issues, such as using an epsilon value to allow them to be the same within a few decimal points:
   * 
   * <code><pre>
return Math.abs(modelValue.doubleValue() - filterValue.doubleValue()) < epsilon * modelValue.doubleValue();
</code></pre>
   * 
   * where {@code epsilon} would represent the magnitude of difference between the two values. If the magnitude of the
   * values is known, this could instead be <code><pre>
return Math.abs(modelValue.doubleValue() - filterValue.doubleValue()) < epsilon;
</code></pre> where {@code epsilon} is instead the maximum difference
   * allowed.
   * 
   * @param n1 first number
   * @param n2 second number
   * @return true if the two values should be considered to be equal for the purposes of filtering
   */
  protected boolean equals(V n1, V n2) {
    return propertyEditor.compare(n1, n2) == 0;
  }

  protected NumberPropertyEditor<V> getPropertyEditor() {
    return propertyEditor;
  }

  @Override
  protected Class<V> getType() {
    return null;
  }

  /**
   * Compares the values given and returns true if the first is greater than the second.
   * 
   * @param n1 first number
   * @param n2 second number
   * @return true if the first parameter is greater than the second parameter
   */
  protected boolean greaterThan(V n1, V n2) {
    return propertyEditor.compare(n1, n2) > 0;
  }

  /**
   * Compares the values given and returns true if the first is less than the second.
   *
   * @param n1 first number
   * @param n2 second number
   * @return true if the first parameter is less than the second parameter
   */
  protected boolean lessThan(V n1, V n2) {
    return propertyEditor.compare(n1, n2) < 0;
  }

  @Override
  protected boolean validateModel(M model) {
    boolean isValid = true;
    V modelValue = getValueProvider().getValue(model);
    if (modelValue != null) {
      if (rangeMenu.eq != null) {
        V filterValue = rangeMenu.eq.getCurrentValue();
        isValid = filterValue == null || equals(modelValue, filterValue);
      }
      if (isValid && rangeMenu.lt != null) {
        V filterValue = rangeMenu.lt.getCurrentValue();
        isValid = filterValue == null || lessThan(modelValue, filterValue);
      }
      if (isValid && rangeMenu.gt != null) {
        V filterValue = rangeMenu.gt.getCurrentValue();
        isValid = filterValue == null || greaterThan(modelValue, filterValue);
      }
    }
    return isValid;
  }

  @Override
  public void setFilterConfig(List<FilterConfig> configs) {
    boolean hasValue = false;
    for (int i = 0; i < configs.size(); i++) {
      FilterConfig config = configs.get(i);
      if (config.getValue() != null && !"".equals(config.getValue())) {
        hasValue = true;
      }
    }
    setValue(configs);
    setActive(hasValue, false);
  }

}
