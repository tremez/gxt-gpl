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
import java.util.Date;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.loader.DateFilterHandler;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.grid.filters.RangeMenu.RangeItem;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.DateMenu;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;

/**
 * A date filter. See {@link Filter} for more information.
 *
 * @param <M> the model type
 */
public class DateFilter<M> extends Filter<M, Date> {

  /**
   * The locale-sensitive messages used by this class.
   */
  public interface DateFilterMessages {
    String afterText();

    String beforeText();

    String onText();
  }

  /**
   * The default locale-sensitive messages used by this class.
   */
  public class DefaultDateFilterMessages implements DateFilterMessages {

    @Override
    public String afterText() {
      return DefaultMessages.getMessages().dateFilter_afterText();
    }

    @Override
    public String beforeText() {
      return DefaultMessages.getMessages().dateFilter_beforeText();
    }

    @Override
    public String onText() {
      return DefaultMessages.getMessages().dateFilter_onText();
    }

  }

  private class Handler implements CheckChangeHandler<CheckMenuItem>, ValueChangeHandler<Date> {

    @Override
    public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
      handleCheckChange(event);
      fireUpdate();
    }

    @Override
    public void onValueChange(ValueChangeEvent<Date> event) {
      handleMenuSelect(event);
    }

  }

  private CheckMenuItem beforeItem, afterItem, onItem;
  private DateMenu beforeMenu, afterMenu, onMenu;
  private DateFilterMessages messages;
  private Date minDate, maxDate;
  private List<RangeItem> rangeItems = new ArrayList<RangeItem>();
  private Handler handler = new Handler();

  /**
   * Creates a date filter for the specified value provider. See {@link Filter#Filter(ValueProvider)} for more
   * information.
   *
   * @param valueProvider the value provider
   */
  public DateFilter(ValueProvider<? super M, Date> valueProvider) {
    super(valueProvider);
    setHandler(new DateFilterHandler());

    rangeItems.add(RangeItem.LESSTHAN);
    rangeItems.add(RangeItem.GREATERTHAN);
    rangeItems.add(RangeItem.EQUAL);

    menu = new Menu();
    beforeItem = new CheckMenuItem();
    beforeItem.addCheckChangeHandler(handler);

    beforeMenu = new DateMenu();
    beforeMenu.setDate(new Date());
    beforeMenu.addValueChangeHandler(handler);
    beforeItem.setSubMenu(beforeMenu);
    menu.add(beforeItem);

    afterItem = new CheckMenuItem();
    afterItem.addCheckChangeHandler(handler);
    afterMenu = new DateMenu();
    afterMenu.setDate(new Date());
    afterMenu.addValueChangeHandler(handler);
    afterItem.setSubMenu(afterMenu);
    menu.add(afterItem);

    menu.add(new SeparatorMenuItem());

    onItem = new CheckMenuItem();
    onItem.addCheckChangeHandler(handler);
    onMenu = new DateMenu();
    onMenu.setDate(new Date());
    onMenu.addValueChangeHandler(handler);
    onItem.setSubMenu(onMenu);
    menu.add(onItem);

    setMessages(getMessages());
  }

  public DateMenu getAfterMenu() {
    return afterMenu;
  }

  public DateMenu getBeforeMenu() {
    return beforeMenu;
  }

  @Override
  public List<FilterConfig> getFilterConfig() {
    List<FilterConfig> configs = new ArrayList<FilterConfig>();
    if (beforeItem != null && beforeItem.isChecked()) {
      FilterConfig c = createNewFilterConfig();
      c.setType("date");
      c.setComparison("before");
      c.setValue(getHandler().convertToString(beforeMenu.getDate()));
      configs.add(c);
    }
    if (afterItem != null && afterItem.isChecked()) {
      FilterConfig c = createNewFilterConfig();
      c.setComparison("after");
      c.setType("date");
      c.setValue(getHandler().convertToString(afterMenu.getDate()));
      configs.add(c);
    }
    if (onItem != null && onItem.isChecked()) {
      FilterConfig c = createNewFilterConfig();
      c.setType("date");
      c.setComparison("on");
      c.setValue(getHandler().convertToString(onMenu.getDate()));
      configs.add(c);
    }
    return configs;
  }

  /**
   * Returns the max date.
   *
   * @return the max date
   */
  public Date getMaxDate() {
    return maxDate;
  }

  /**
   * Returns the locale-sensitive messages used by this class.
   *
   * @return the local-sensitive messages used by this class.
   */
  public DateFilterMessages getMessages() {
    if (messages == null) {
      messages = new DefaultDateFilterMessages();
    }
    return messages;
  }

  /**
   * Returns the minimum date.
   *
   * @return the minimum date
   */
  public Date getMinDate() {
    return minDate;
  }

  public DateMenu getOnMenu() {
    return onMenu;
  }

  @Override
  public Object getValue() {
    return getFilterConfig();
  }

  /**
   * Handles the menu select event generated by the filter menu.
   *
   * @param event the menu select event
   */
  public void handleMenuSelect(ValueChangeEvent<Date> event) {
    DateMenu menu = (DateMenu) event.getSource();
    if (menu == beforeMenu) {
      maxDate = event.getValue();
      updateMenuState(beforeItem, true);
    } else if (menu == afterMenu) {
      minDate = event.getValue();
      updateMenuState(afterItem, true);
    } else if (menu == onMenu) {
      updateMenuState(onItem, true);
    }
    menu.hide(true);
    fireUpdate();
  }

  @Override
  public boolean isActivatable() {
    if (beforeItem != null && beforeItem.isChecked() && maxDate != null) {
      return true;
    }
    if (afterItem != null && afterItem.isChecked() && minDate != null) {
      return true;
    }
    if (onItem != null && onItem.isChecked()) {
      return true;
    }
    return false;
  }

  /**
   * Sets the after date. In order for the filter to be applied, {@link #setActive(boolean, boolean)} must be called
   * when setting filter value programmatically.
   *
   * @param date the after date
   */
  public void setAfterDate(Date date) {
    afterMenu.setDate(date);
  }

  /**
   * Sets the before date. In order for the filter to be applied, {@link #setActive(boolean, boolean)} must be called
   * when setting filter value programmatically.
   *
   * @param date the before date
   */
  public void setBeforeDate(Date date) {
    beforeMenu.setDate(date);
  }

  /**
   * Sets the max date as passed to the date picker.
   *
   * @param maxDate the max date
   */
  public void setMaxDate(Date maxDate) {
    this.maxDate = maxDate;
    beforeMenu.getDatePicker().setMaxDate(maxDate);
    onMenu.getDatePicker().setMaxDate(maxDate);
    afterMenu.getDatePicker().setMaxDate(maxDate);
  }

  /**
   * Sets the local-sensitive messages used by this class.
   *
   * @param messages the locale sensitive messages used by this class.
   */
  public void setMessages(DateFilterMessages messages) {
    this.messages = messages;
    onItem.setText(getMessages().onText());
    afterItem.setText(getMessages().afterText());
    beforeItem.setText(getMessages().beforeText());
  }

  /**
   * Set's the minimum date as passed to the date picker.
   *
   * @param minDate the minimum date
   */
  public void setMinDate(Date minDate) {
    this.minDate = minDate;
    beforeMenu.getDatePicker().setMinDate(minDate);
    onMenu.getDatePicker().setMinDate(minDate);
    afterMenu.getDatePicker().setMinDate(minDate);
  }

  /**
   * Sets the on date. In order for the filter to be applied, {@link #setActive(boolean, boolean)} must be called when
   * setting filter value programmatically.
   *
   * @param date the on date
   */
  public void setOnDate(Date date) {
    onMenu.setDate(date);
  }

  /**
   * Sets the filter's values.
   *
   * @param values the values
   */
  public void setValue(List<FilterConfig> values) {
    for (FilterConfig config : values) {
      String c = config.getComparison();
      String v = config.getValue();

      if ("before".equals(c)) {
        beforeMenu.setDate(getHandler().convertToObject(v));
      } else if ("after".equals(c)) {
        afterMenu.setDate(getHandler().convertToObject(v));
      } else if ("on".equals(c)) {
        onMenu.setDate(getHandler().convertToObject(v));
      }
    }
  }

  protected Class<Date> getType() {
    return Date.class;
  }

  protected void handleCheckChange(CheckChangeEvent<CheckMenuItem> event) {
    updateMenuState(event.getItem(), event.getItem().isChecked());
  }

  protected void updateMenuState(CheckMenuItem item, boolean checked) {
    if (item == onItem) {
      onItem.setChecked(checked, true);
      if (checked) {
        beforeItem.setChecked(false, true);
        afterItem.setChecked(false, true);
      }
    } else if (item == afterItem) {
      if (afterMenu.getDate() == null) {
        afterItem.setChecked(false, true);
      } else {
        minDate = afterMenu.getDate();
        afterItem.setChecked(checked, true);
        if (checked) {
          // EXTGWT-3257 - Sanity check to before and after don't conflict
          if (!(beforeMenu.getDate() != null && beforeMenu.getDate().after(afterMenu.getDate()))) {
            beforeItem.setChecked(false, true);
          }
          onItem.setChecked(false, true);
        }
      }
    } else if (item == beforeItem) {
      if (beforeMenu.getDate() == null) {
        beforeItem.setChecked(false, true);
      } else {
        beforeItem.setChecked(checked, true);
        if (checked) {
          maxDate = beforeMenu.getDate();
          onItem.setChecked(false, true);
          // EXTGWT-3257 - Sanity check to before and after don't conflict
          if (!(afterMenu.getDate() != null && afterMenu.getDate().before(beforeMenu.getDate()))) {
            afterItem.setChecked(false, true);
          }
        }
      }
    }
  }

  protected boolean validateModel(M model) {
    super.validateModel(model);
    Date d = getValueProvider().getValue(model);

    long time = d == null ? 0l : new DateWrapper(d).clearTime().getTime();
    if (beforeItem.isChecked() && beforeMenu.getDate() != null) {
      long pvalue = new DateWrapper(beforeMenu.getDate()).clearTime().getTime();
      if (d == null || pvalue <= time) {
        return false;
      }
    }
    if (afterItem.isChecked() && afterMenu.getDate() != null) {
      long pvalue = new DateWrapper(afterMenu.getDate()).clearTime().getTime();
      if (d == null || pvalue >= time) {
        return false;
      }
    }
    if (onItem.isChecked() && onMenu.getDate() != null) {
      long pvalue = new DateWrapper(onMenu.getDate()).resetTime().getTime();
      if (d == null || pvalue != (d == null ? 0l : new DateWrapper(d).resetTime().getTime())) {
        return false;
      }
    }
    return true;
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
