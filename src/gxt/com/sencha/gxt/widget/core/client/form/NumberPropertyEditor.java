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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Comparator;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.constants.NumberConstants;

/**
 * A property editor that converts typed numbers to strings and strings back to
 * numbers. Also handles incrementing and decrementing the typed numbers.
 * 
 * @param <N> the number type
 */
public abstract class NumberPropertyEditor<N extends Number & Comparable<N>> extends PropertyEditor<N>
    implements Comparator<N> {

  /**
   * A number property editor for use with {@link BigDecimal}.
   */
  public static class BigDecimalPropertyEditor extends NumberPropertyEditor<BigDecimal> {

    /**
     * Creates a number property editor for use with {@link BigDecimal}.
     */
    public BigDecimalPropertyEditor() {
      super(BigDecimal.ONE);
    }

    /**
     * Creates a number property editor for use with {@link BigDecimal} that
     * uses the specified format when converting to (or from) a string.
     * 
     * @param format the format to use when converting to (or from) a string
     */
    public BigDecimalPropertyEditor(NumberFormat format) {
      super(format, BigDecimal.ONE);
    }

    @Override
    protected BigDecimal doDecr(BigDecimal value) {
      return value.subtract(getIncrement());
    }

    @Override
    protected BigDecimal doIncr(BigDecimal value) {
      return value.add(getIncrement());
    }

    @Override
    protected BigDecimal parseString(String string) {
      // Handle non-U.S. locales (e.g. 1.234 instead of 1,234)
      return new BigDecimal(format.parse(string));
    }

    @Override
    protected BigDecimal returnTypedValue(Number number) {
      if (number instanceof BigDecimal) {
        return (BigDecimal) number;
      } else {
        return BigDecimal.valueOf(number.doubleValue());
      }
    }

  }

  /**
   * A number property editor for use with {@link BigInteger}.
   */
  public static class BigIntegerPropertyEditor extends NumberPropertyEditor<BigInteger> {

    /**
     * Creates a number property editor for use with {@link BigInteger}.
     */
    public BigIntegerPropertyEditor() {
      super(BigInteger.ONE);
    }

    /**
     * Creates a number property editor for use with {@link BigInteger} that
     * uses the specified format when converting to (or from) a string.
     * 
     * @param format the format to use when converting to (or from) a string
     */
    public BigIntegerPropertyEditor(NumberFormat format) {
      super(format, BigInteger.ONE);
    }

    @Override
    protected BigInteger doDecr(BigInteger value) {
      return value.subtract(getIncrement());
    }

    @Override
    protected BigInteger doIncr(BigInteger value) {
      return value.add(getIncrement());
    }

    @Override
    protected BigInteger parseString(String string) {
      // Handle non-U.S. locales (e.g. 1.234 instead of 1,234)
      return new BigDecimal(format.parse(string)).toBigInteger();
    }

    @Override
    protected BigInteger returnTypedValue(Number number) {
      if (number instanceof BigInteger) {
        return (BigInteger) number;
      } else {
        return BigInteger.valueOf(number.longValue());
      }
    }

  }

  /**
   * A number property editor for use with {@link Double}.
   */
  public static class DoublePropertyEditor extends NumberPropertyEditor<Double> {
    /**
     * Creates a number property editor for use with {@link Double}.
     */
    public DoublePropertyEditor() {
      super(1d);
    }

    /**
     * Creates a number property editor for use with {@link Double} that uses
     * the specified format when converting to (or from) a string.
     * 
     * @param format the format to use when converting to (or from) a string
     */
    public DoublePropertyEditor(NumberFormat format) {
      super(format, 1d);
    }

    @Override
    public Double doDecr(Double value) {
      double newValue = value - getIncrement();
      newValue = newValue > value ? value : newValue;
      return newValue;
    }

    @Override
    public Double doIncr(Double value) {
      double newValue = value + getIncrement();
      newValue = newValue < value ? value : newValue;
      return newValue;
    }

    @Override
    protected Double parseString(String string) {
      // Handle non-U.S. locales (e.g. 1.234 instead of 1,234)
      return format.parse(string);
    }

    @Override
    protected Double returnTypedValue(Number number) {
      return number.doubleValue();
    }
  }

  /**
   * A number property editor for use with {@link Float}.
   */
  public static class FloatPropertyEditor extends NumberPropertyEditor<Float> {
    /**
     * Creates a number property editor for use with {@link Float}.
     */
    public FloatPropertyEditor() {
      super(1f);
    }

    /**
     * Creates a number property editor for use with {@link Float} that uses the
     * specified format when converting to (or from) a string.
     * 
     * @param format the format to use when converting to (or from) a string
     */
    public FloatPropertyEditor(NumberFormat format) {
      super(format, 1f);
    }

    @Override
    public Float doDecr(Float value) {
      float newValue = value - getIncrement();
      newValue = newValue > value ? value : newValue;
      return newValue;
    }

    @Override
    public Float doIncr(Float value) {
      float newValue = value + getIncrement();
      newValue = newValue < value ? value : newValue;
      return newValue;
    }

    @Override
    protected Float parseString(String string) {
      // Handle non-U.S. locales (e.g. 1.234 instead of 1,234)
      return (float) format.parse(string);
    }

    @Override
    protected Float returnTypedValue(Number number) {
      return number.floatValue();
    }
  }

  /**
   * A number property editor for use with {@link Integer}.
   */
  public static class IntegerPropertyEditor extends NumberPropertyEditor<Integer> {
    /**
     * Creates a number property editor for with {@link Integer}.
     */
    public IntegerPropertyEditor() {
      super(1);
    }

    /**
     * Creates a number property editor for use with {@link Integer} that uses
     * the specified format when converting to (or from) a string.
     * 
     * @param format the format to use when converting to (or from) a string
     */
    public IntegerPropertyEditor(NumberFormat format) {
      super(format, 1);
    }

    @Override
    public Integer doDecr(Integer value) {
      int newValue = value - getIncrement();
      newValue = newValue > value ? value : newValue;
      return newValue;
    }

    @Override
    public Integer doIncr(Integer value) {
      int newValue = value + getIncrement();
      newValue = newValue < value ? value : newValue;
      return newValue;
    }

    @Override
    protected Integer parseString(String string) {
      // Handle non-U.S. locales (e.g. 1.234 instead of 1,234)
      return (int) format.parse(string);
    }

    @Override
    protected Integer returnTypedValue(Number number) {
      return number.intValue();
    }
  }
  /**
   * A number property editor for use with {@link Long}.
   */
  public static class LongPropertyEditor extends NumberPropertyEditor<Long> {
    /**
     * Creates a number property editor for use with {@link Long}.
     */
    public LongPropertyEditor() {
      super(1l);
    }

    /**
     * Creates a number property editor for use with {@link Long} that uses the
     * specified format when converting to (or from) a string.
     * 
     * @param format the format to use when converting to (or from) a string
     */
    public LongPropertyEditor(NumberFormat format) {
      super(format, 1l);
    }

    @Override
    public Long doDecr(Long value) {
      long newValue = value - getIncrement();
      newValue = newValue > value ? value : newValue;
      return newValue;
    }

    @Override
    public Long doIncr(Long value) {
      long newValue = value + getIncrement();
      newValue = newValue < value ? value : newValue;
      return newValue;
    }

    @Override
    protected Long parseString(String string) {
      // Handle non-U.S. locales (e.g. 1.234 instead of 1,234)
      return (long) format.parse(string);
    }

    @Override
    protected Long returnTypedValue(Number number) {
      return number.longValue();
    }
  }
  /**
   * A number property editor for use with {@link Short}.
   */
  public static class ShortPropertyEditor extends NumberPropertyEditor<Short> {

    /**
     * Creates a number property editor for use with {@link Short}.
     */
    public ShortPropertyEditor() {
      super((short) 1);
    }

    /**
     * Creates a number property editor for use with {@link Short} that uses the
     * specified format when converting to (or from) a string.
     * 
     * @param format the format to use when converting to (or from) a string
     */
    public ShortPropertyEditor(NumberFormat format) {
      super(format, (short) 1);
    }

    @Override
    public Short doDecr(Short value) {
      short newValue = (short)(value - getIncrement());
      newValue = newValue > value ? value : newValue;
      return newValue;
    }

    @Override
    public Short doIncr(Short value) {
      short newValue = (short) (value + getIncrement());
      newValue = newValue < value ? value : newValue;
      return newValue;
    }

    @Override
    protected Short parseString(String string) {
      // Handle non-U.S. locales (e.g. 1.234 instead of 1,234)
      return (short) format.parse(string);
    }

    @Override
    protected Short returnTypedValue(Number number) {
      return number.shortValue();
    }
  }

  protected NumberConstants numbers;
  protected NumberFormat format;
  protected String alphaRegex = "[a-zA-Z]";
  protected String currencySymbolRegex = "\\$";
  protected String groupSeparator;

  private N incrAmount;

  private boolean stripCurrencySymbol;
  private boolean stripAlphas;
  private boolean stripGroupSeparator;

  /**
   * Creates a new number property editor with the default number type (Double).
   */
  public NumberPropertyEditor(N incrAmount) {
    this.incrAmount = incrAmount;
    numbers = LocaleInfo.getCurrentLocale().getNumberConstants();
    groupSeparator = numbers.groupingSeparator();
  }

  /**
   * Creates a new number property editor.
   * 
   * @param format the number format
   */
  public NumberPropertyEditor(NumberFormat format, N incrAmount) {
    this(incrAmount);
    this.format = format;
  }

  /**
   * Compares the first value with the second value using the objects natural compareTo.
   * If the first is a null and the second isn't, it will be greater than.
   *
   * @param n1 first number
   * @param n2 second number
   * @return returns compares equality based on -1, 0 or 1
   */
  @Override
  public int compare(N n1, N n2) {
    if (n1 == null && n2 == null) {
      return 0;
    } else if (n1 == null) {
      return 1;
    }
    return n1.compareTo(n2);
  }

  /**
   * Creates a new number property editor.
   * 
   * @param pattern the number format pattern
   */
  public NumberPropertyEditor(String pattern, N incrAmount) {
    this(NumberFormat.getFormat(pattern), incrAmount);
  }

  /**
   * Decrements a value by the current increment amount.
   * 
   * @param value the value to decrement
   * @return the decremented value
   */
  public N decr(N value) {
    if (value == null) {
      return doDecr(doDecr(getIncrement()));
    }
    return doDecr(value);
  }

  /**
   * Returns the editor's format.
   * 
   * @return the number format
   */
  public NumberFormat getFormat() {
    return format;
  }

  /**
   * Gets the current increment amount.
   * 
   * @return the current increment amount.
   */
  public N getIncrement() {
    return incrAmount;
  }

  /**
   * Increments a value by the current increment amount.
   * 
   * @param value the value to increment
   * @return the incremented value
   */
  public N incr(N value) {
    if (value == null) {
      return getIncrement();
    }
    return doIncr(value);
  }

  @Override
  public N parse(CharSequence text) throws ParseException {
    String value = text.toString();

    // workaround as GWT NumberFormat stripping -- and not throwing exception
    if (value.length() >= 2 && value.startsWith("--")) {
      throw new ParseException(value + " is not a valid number", 0);
    }

    // first try to create a typed value directly from the raw text
    try {
      return parseString(value);
    } catch (Exception e) {
    }

    // second, strip all unwanted characters
    String stripValue = stripValue(value);
    try {
      return parseString(stripValue);
    } catch (Exception e) {
    }

    try {
      // third try parsing with the formatter
      if (format != null) {
        Double d = format.parse(value);
        return (N) returnTypedValue(d);
      } else {
        Double d = NumberFormat.getDecimalFormat().parse(value);
        return (N) returnTypedValue(d);
      }
    } catch (Exception ex) {
      throw new ParseException(ex.getMessage(), 0);
    }
  }

  @Override
  public String render(Number value) {
    if (format != null) {
      return format.format(value.doubleValue());
    }
    return value.toString();
  }

  /**
   * Sets the editor's format.
   * 
   * @param format the format
   */
  public void setFormat(NumberFormat format) {
    this.format = format;
  }

  /**
   * Sets the increment amount (defaults to zero).
   * 
   * @param value the new increment amount
   */
  public void setIncrement(N value) {
    this.incrAmount = value;
  }

  protected abstract N doDecr(N value);

  protected abstract N doIncr(N value);

  protected abstract N parseString(String string);

  protected abstract N returnTypedValue(Number number);

  protected String stripValue(String value) {
    if (stripCurrencySymbol) {
      value = value.replaceAll(currencySymbolRegex, "");
    }
    if (stripAlphas) {
      value = value.replaceAll(alphaRegex, "");
    }
    if (stripGroupSeparator) {
      value = value.replaceAll(groupSeparator, "");
    }
    return value;
  }

}
