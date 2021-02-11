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
package com.sencha.gxt.widget.core.client.form.error;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;

/**
 * A <code>FieldError</code> instance that calls setInnerSafeHtml(SafeHtml) on a target
 * element. The target element can be specified directly or via a element id.
 */
public class ElementErrorHandler implements ErrorHandler {

  protected String elementId;
  protected Element element;

  public ElementErrorHandler(String elementId) {
    this.elementId = elementId;
  }

  public ElementErrorHandler(Element element) {
    this.element = element;
  }

  public Element getElement() {
    return element;
  }

  public void setElement(Element element) {
    this.element = element;
  }

  public String getElementId() {
    return elementId;
  }

  public void setElementId(String elementId) {
    this.elementId = elementId;
  }

  @Override
  public void clearInvalid() {
    Element elem = element;
    if (elem == null) {
      elem = DOM.getElementById(elementId);
    }
    if (elem != null) {
      elem.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    }
  }

  @Override
  public void markInvalid(List<EditorError> errors) {
    Element elem = element;
    if (elem == null) {
      elem = DOM.getElementById(elementId);
    }
    if (elem != null && errors != null && errors.size() > 0) {
      elem.setInnerSafeHtml(SafeHtmlUtils.fromString(errors.get(0).getMessage()));
    }
  }
  @Override
  public void release() {
    //no handlers to remove
  }

}