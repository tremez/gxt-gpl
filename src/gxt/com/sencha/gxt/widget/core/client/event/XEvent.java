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
package com.sencha.gxt.widget.core.client.event;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;

public class XEvent extends Event {

  protected XEvent() {

  }

  /**
   * Returns true if the control or meta key was depressed.
   * 
   * @return true if control or meta
   */
  public final boolean getCtrlOrMetaKey() {
    return getCtrlKey() || getMetaKey();
  }

  /**
   * Returns the event target element.
   * 
   * @return the target element
   */
  public final XElement getEventTargetEl() {
    return getEventTarget().<XElement> cast();
  }

  /**
   * Returns the matching parent using the specified selector.
   * 
   * @param selector the CSS selector
   * @param maxDepth the maximum number of parents to search
   * @return the matching element or null
   */
  public final XElement getTargetEl(String selector, int maxDepth) {
    return getEventTargetEl().findParent(selector, maxDepth);
  }

  /**
   * Returns the mouse/touch location.
   * 
   * @return the mouse/touch location
   */
  public final Point getXY() {
    if (getChangedTouches() != null) {
      // touch event
      Touch touch = getChangedTouches().get(0);
      return new Point(touch.getClientX(), touch.getClientY());
    }
    return new Point(getClientX(), getClientY());
  }

  /**
   * Returns true if the key is a "navigation" key.
   * 
   * @return the nav state
   */
  public final boolean isNavKeyPress() {
    return isNavKeyPress(getKeyCode());
  }

  /**
   * Returns true if the key is a "navigation" key.
   * 
   * @param k the key code
   * @return the nav state
   */
  public final boolean isNavKeyPress(int k) {
    return (k >= 33 && k <= 40) || k == KeyCodes.KEY_ESCAPE || k == KeyCodes.KEY_ENTER || k == KeyCodes.KEY_TAB;
  }

  /**
   * Returns <code>true</code> if the event is a right click.
   * 
   * @return the right click state
   */
  public final boolean isRightClick() {
    if (getButton() == Event.BUTTON_RIGHT || (GXT.isMac() && getCtrlKey())) {
      return true;
    }
    return false;
  }

  /**
   * Returns true if the key is a "special" key.
   * 
   * @return the special state
   */
  public final boolean isSpecialKey() {
    return isSpecialKey(getKeyCode());
  }

  /**
   * Returns true if the key is a "special" key.
   * 
   * @param k the key code
   * @return the special state
   */
  public final boolean isSpecialKey(int k) {
    return isNavKeyPress(k) || k == KeyCodes.KEY_BACKSPACE || k == KeyCodes.KEY_CTRL || k == KeyCodes.KEY_SHIFT
        || k == KeyCodes.KEY_ALT || (k >= 19 && k <= 20) || (k >= 45 && k <= 46);
  }

  /**
   * Stops the event (stopPropagation and preventDefault).
   */
  public final void stopEvent() {
    stopPropagation();
    preventDefault();
  }

  /**
   * Returns <code>true</code> if the target of this event equals or is a child
   * of the given element.
   * 
   * @param element the element
   * @return the within state
   */
  public final boolean within(Element element) {
    return within(element, false);
  }

  /**
   * Returns <code>true</code> if the target of this event equals or is a child
   * of the given element.
   * 
   * @param element the element
   * @param toElement true to use {@link Event#getRelatedEventTarget()}
   * @return the within state
   */
  public final boolean within(Element element, boolean toElement) {
    if (Element.is(element)) {
      EventTarget target = toElement ? getRelatedEventTarget() : getEventTarget();
      if (Element.is(target)) {
        return element.isOrHasChild((Element) target.cast());
      }
    }
    return false;
  }

  /**
   * Temporary workaround for IE11 GWT bug (https://code.google.com/p/google-web-toolkit/issues/detail?id=8476).
   *
   * @deprecated this method will be removed when the bug is fixed
   * @return the correct velocity
   */
  @Deprecated
  public final int getMouseWheelVelocityFix() {
    int vel = getMouseWheelVelocityY();
    if (vel == 0) {
      return workaroundEventGetMouseWheelVelocityY(this);
    }
    return vel;
  }

  private static final native int workaroundEventGetMouseWheelVelocityY(NativeEvent evt) /*-{
    if (typeof evt.wheelDelta == "undefined") {
      return 0;
    }
    return Math.round(-evt.wheelDelta / 40) || 0;
  }-*/;
}
