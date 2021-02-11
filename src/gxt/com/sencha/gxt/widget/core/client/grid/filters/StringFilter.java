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

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.StringFilterHandler;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * A string filter. See {@link Filter} for more information.
 * 
 * @param <M> the model type
 */
public class StringFilter<M> extends Filter<M, String> {

  /**
   * The default locale-sensitive messages used by this class.
   */
  public class DefaultStringFilterMessages implements StringFilterMessages {

    @Override
    public String emptyText() {
      return DefaultMessages.getMessages().stringFilter_emptyText();
    }

  }

  /**
   * The locale-sensitive messages used by this class.
   */
  public interface StringFilterMessages {
    String emptyText();
  }

  protected TextField field;

  private StringFilterMessages messages;
  private DelayedTask updateTask = new DelayedTask() {

    @Override
    public void onExecute() {
      fireUpdate();
    }
  };

  /**
   * Creates a string filter for the specified value provider. See {@link Filter#Filter(ValueProvider)} for more
   * information.
   * 
   * @param valueProvider the value provider
   */
  public StringFilter(ValueProvider<? super M, String> valueProvider) {
    super(valueProvider);
    setHandler(new StringFilterHandler());

    field = new TextField() {
      protected void onKeyUp(Event event) {
        super.onKeyUp(event);
        onFieldKeyUp(event);
      }
    };

    menu.add(field);
    menu.addBeforeHideHandler(new BeforeHideHandler() {

      @Override
      public void onBeforeHide(BeforeHideEvent event) {
        // blur the field because of empty text
        // field.el().firstChild().blur();
        // blurField(field);
        field.getElement().selectNode("input").blur();
      }
    });

    setMessages(getMessages());
  }

  @Override
  public List<FilterConfig> getFilterConfig() {
    FilterConfig cfg = createNewFilterConfig();
    cfg.setType("string");
    cfg.setComparison("contains");
    String valueToSend = field.getCurrentValue();
    cfg.setValue(getHandler().convertToString(valueToSend));

    return Collections.singletonList(cfg);
  }

  /**
   * Returns the locale-sensitive messages used by this class.
   * 
   * @return the local-sensitive messages used by this class.
   */
  public StringFilterMessages getMessages() {
    if (messages == null) {
      messages = new DefaultStringFilterMessages();
    }
    return messages;
  }

  @Override
  public Object getValue() {
    return field.getCurrentValue();
  }

  @Override
  public boolean isActivatable() {
    return field.getCurrentValue() != null && field.getCurrentValue().length() > 0;
  }

  /**
   * Sets the local-sensitive messages used by this class.
   * 
   * @param messages the locale sensitive messages used by this class.
   */
  public void setMessages(StringFilterMessages messages) {
    this.messages = messages;
    field.setEmptyText(messages.emptyText());
  }

  /**
   * Sets the value of this filter. In order for the filter to be applied, {@link #setActive(boolean, boolean)} must be
   * called when setting filter value programmatically.
   * 
   * @param value the value
   */
  public void setValue(String value) {
    field.setValue(value);
  }

  protected Class<String> getType() {
    return String.class;
  }

  protected void onFieldKeyUp(Event event) {
    int key = event.getKeyCode();
    if (key == KeyCodes.KEY_ENTER && field.isValid()) {
      event.stopPropagation();
      event.preventDefault();
      menu.hide(true);
      return;
    }
    updateTask.delay(getUpdateBuffer());
  }

  protected boolean validateModel(M model) {
    String val = getValueProvider().getValue(model);
    Object value = getValue();
    String v = value == null ? "" : value.toString();
    if (v.length() == 0 && (val == null || val.length() == 0)) {
      return true;
    } else if (val == null) {
      return false;
    } else {
      return val.toLowerCase().indexOf(v.toLowerCase()) > -1;
    }
  }

  @Override
  public void setFilterConfig(List<FilterConfig> configs) {
    if (configs.size() > 0) {
      String val = configs.get(0).getValue();
      setValue(val);
      setActive(val != null, false);
    } else {
      setValue(null);
      setActive(false, false);
    }
  }
}
