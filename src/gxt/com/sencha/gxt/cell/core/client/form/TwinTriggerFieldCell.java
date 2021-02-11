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

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.widget.core.client.event.TwinTriggerClickEvent;

public class TwinTriggerFieldCell<T> extends TriggerFieldCell<T> {

  public interface TwinTriggerFieldAppearance extends TriggerFieldAppearance {
    boolean twinTriggerIsOrHasChild(XElement parent, Element target);

    void onTwinTriggerOver(XElement parent, boolean over);

    void onTwinTriggerClick(XElement parent, boolean click);

  }

  public TwinTriggerFieldCell() {
    this(GWT.<TwinTriggerFieldAppearance> create(TwinTriggerFieldAppearance.class));
  }

  public TwinTriggerFieldCell(TwinTriggerFieldAppearance appearance) {
    super(appearance);
  }

  @Override
  public TwinTriggerFieldAppearance getAppearance() {
    return (TwinTriggerFieldAppearance) super.getAppearance();
  }

  @Override
  protected void onClick(Context context, XElement parent, NativeEvent event, T value, ValueUpdater<T> updater) {
    Element target = event.getEventTarget().cast();

    if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent, target)) {
      onTwinTriggerClick(context, parent, event, value, updater);
    }

    if (!isReadOnly() && getAppearance().triggerIsOrHasChild(parent, target)) {
      onTriggerClick(context, parent, event, value, updater);
    }

  }

  @Override
  protected void onTap(TouchData t, Context context, Element parent, T value, ValueUpdater<T> valueUpdater) {
    XElement target = t.getStartElement().asElement().cast();

    if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent.<XElement>cast(), target)) {
      onTwinTriggerClick(context, parent.<XElement>cast(), null, value, valueUpdater);
      t.getLastNativeEvent().preventDefault();
    } else  if (!isReadOnly() && getAppearance().triggerIsOrHasChild(parent.<XElement>cast(), target)) {
      onTriggerClick(context, parent.<XElement>cast(), null, value, valueUpdater);
      t.getLastNativeEvent().preventDefault();
    } else {
      getInputElement(parent).focus();
    }
  }

  @Override
  protected void onMouseDown(XElement parent, NativeEvent event) {
    super.onMouseDown(parent, event);

    Element target = event.getEventTarget().cast();
    if (!isReadOnly() && (!isEditable() && getInputElement(parent).isOrHasChild(target))
            || getAppearance().twinTriggerIsOrHasChild(parent,target)) {
      getAppearance().onTwinTriggerClick(parent, true);
      event.preventDefault();
    }
  }

  protected void onTwinTriggerClick(Context context, XElement parent, NativeEvent event, T value,
      ValueUpdater<T> updater) {
    fireEvent(context, new TwinTriggerClickEvent());
    getAppearance().onTwinTriggerClick(parent, false);
  }

  @Override
  protected void onMouseOver(XElement parent, NativeEvent event) {
    super.onMouseOver(parent, event);
    XElement target = event.getEventTarget().cast();
    if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent, target)) {
      getAppearance().onTwinTriggerOver(parent, true);
    }
  }

  @Override
  protected void onMouseOut(XElement parent, NativeEvent event) {
    super.onMouseOut(parent, event);
    XElement target = event.getEventTarget().cast();
    if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent, target)) {
      getAppearance().onTwinTriggerOver(parent, false);
    }
  }
  
}
