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

import java.util.logging.Logger;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.dom.XElement;

public class TextInputCell extends ValueBaseInputCell<String> {

  public interface TextFieldAppearance extends ValueBaseFieldAppearance {
    void onResize(XElement parent, int width, int height);

    void render(SafeHtmlBuilder sb, String type, String value, FieldAppearanceOptions options);
  }

  private static Logger logger = Logger.getLogger(TextInputCell.class.getName());

  /**
   * Constructs a TextInputCell that renders its text without HTML markup.
   */
  public TextInputCell() {
    this(GWT.<TextFieldAppearance> create(TextFieldAppearance.class));
  }

  public TextInputCell(TextFieldAppearance appearance) {
    super(appearance, "change", "keyup");

    setWidth(150);
  }

  @Override
  public void finishEditing(Element parent, String value, Object key, ValueUpdater<String> valueUpdater) {
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("finishEditing");
    }

    String newValue = getText(XElement.as(parent));

    // Get the view data.
    FieldViewData vd = getViewData(key);
    if (vd == null) {
      vd = new FieldViewData(value);
      setViewData(key, vd);
    }
    vd.setCurrentValue(newValue);

    boolean change = valueUpdater != null && !vd.getCurrentValue().equals(vd.getLastValue());
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("finishEditing value " + (change ? "changed" : "not changed"));
    }

    // Fire the value updater if the value has changed.
    if (change) {
      vd.setLastValue(newValue);
      valueUpdater.update(newValue);
    }

    clearViewData(key);
    clearFocusKey();

    // calling super.finishEditing not needed as programmatic blurs causes issues
  }

  @Override
  public TextFieldAppearance getAppearance() {
    return (TextFieldAppearance) super.getAppearance();
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
      ValueUpdater<String> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);

    // Ignore events that don't target the input.
    InputElement input = getInputElement(parent);
    Element target = event.getEventTarget().cast();
    if (!input.isOrHasChild(target)) {
      return;
    }

    String eventType = event.getType();
    Object key = context.getKey();
    if ("change".equals(eventType)) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("onBrowserEvent change event fired");
      }
      finishEditing(parent, value, key, valueUpdater);
    } else if ("keyup".equals(eventType)) {
      // Record keys as they are typed.
      FieldViewData vd = getViewData(key);
      if (vd == null) {
        vd = new FieldViewData(value);
        setViewData(key, vd);
      }
      vd.setCurrentValue(getText(XElement.as(parent)));
    }
  }

  @Override
  public void render(Context context, String value, SafeHtmlBuilder sb) {
    ViewData viewData = checkViewData(context, value);
    String s = (viewData != null) ? viewData.getCurrentValue() : value;

    s = getPropertyEditor().render(s);

    FieldAppearanceOptions options = new FieldAppearanceOptions(getWidth(), getHeight(), isReadOnly(), getEmptyText());
    options.setName(name);
    options.setDisabled(isDisabled());
    getAppearance().render(sb, "text", s == null ? "" : s, options);
  }

  @Override
  public void setSize(XElement parent, int width, int height) {
    super.setSize(parent, width, height);
    getAppearance().onResize(parent, width, height);
  }

  private native void clearFocusKey() /*-{
        this.@com.google.gwt.cell.client.AbstractInputCell::focusedKey = null;
  }-*/;

}
