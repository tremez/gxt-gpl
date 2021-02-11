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
package com.sencha.gxt.core.client.gestures.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.user.client.impl.DOMImpl;
import com.google.gwt.user.client.impl.DOMImplStandard;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.PointerEvents;
import com.sencha.gxt.core.client.gestures.PointerEventsSupport;

/**
 * Base implementation. Verifies that the DOMIMpl we get is some subclass of DOMImplStandard
 * in order to ride on the same event system
 */
public class PointerEventsSupportImpl extends PointerEventsSupport {
  static final PointerEventsSupport impl = GWT.create(PointerEventsSupport.class);
  static final DOMImpl domImpl = GWT.create(DOMImpl.class);
  static final boolean isDomImplStandard = (domImpl instanceof DOMImplStandard);

  static {
    if (isDomImplStandard) {
      JavaScriptObject eventDispatchers = JavaScriptObject.createObject();
      for (PointerEvents pointerEvent : PointerEvents.values()) {
        addPointerEventDispatcher(eventDispatchers, pointerEvent.getEventName());
      }
      DOMImplStandard.addCaptureEventDispatchers(eventDispatchers);
    }
  }

  private static native void addPointerEventDispatcher(JavaScriptObject eventDispatchers, String eventType) /*-{
    eventDispatchers[eventType] = $entry(@com.google.gwt.user.client.impl.DOMImplStandard::dispatchCapturedEvent(*));
  }-*/;

  private static native void sinkPointerEventsImpl(Element element, String eventListener) /*-{
    element[eventListener] = $entry(@com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent(*));
  }-*/;

  @Override
  public boolean isSupported() {
    return true;
  }

  @Override
  public native void setPointerCapture(XElement element, NativeEvent event)/*-{
    element.setPointerCapture(event.pointerId);
  }-*/;

  @Override
  public native JsArray<Touch> getChangedTouches(NativeEvent event) /*-{
    var touch = {
      identifier: event.pointerId,
      clientX: event.clientX,
      clientY: event.clientY,
      pageX: event.pageX,
      pageY: event.pageY,
      screenX: event.screenX,
      screenY: event.screenY,
      target: event.target
    }

    return [touch];
  }-*/;

  @Override
  public void sinkPointerEvents(XElement element) {
    if (domImpl instanceof DOMImplStandard) {
      for (PointerEvents pointerEvent : PointerEvents.values()) {
        String eventListener = "on" + pointerEvent.getEventName().toLowerCase();
        sinkPointerEventsImpl(element, eventListener);
      }
    }
  }
}
