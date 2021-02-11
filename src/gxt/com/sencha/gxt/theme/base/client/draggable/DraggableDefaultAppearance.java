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
package com.sencha.gxt.theme.base.client.draggable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.fx.client.Draggable.DraggableAppearance;

public class DraggableDefaultAppearance implements DraggableAppearance {

  public interface DraggableResources extends ClientBundle {

    @Source("Draggable.gss")
    DraggableStyle style();

  }

  public interface DraggableStyle extends CssResource {

    String cursor();

    String proxy();

  }

  private final DraggableStyle style;
  private String proxyClass;

  public DraggableDefaultAppearance() {
    this(GWT.<DraggableResources> create(DraggableResources.class));
  }

  public DraggableDefaultAppearance(DraggableResources resources) {
    this.style = resources.style();
    StyleInjectorHelper.ensureInjected(style, true);
    proxyClass = style.proxy();
  }

  @Override
  public void addUnselectableStyle(Element element) {
    element.addClassName(CommonStyles.get().unselectable());
    element.addClassName(style.cursor());
  }

  @Override
  public Element createProxy() {
    XElement proxyEl = Document.get().createDivElement().cast();
    proxyEl.setVisibility(false);
    proxyEl.setClassName(proxyClass);
    proxyEl.disableTextSelection(true);
    return proxyEl;
  }

  @Override
  public void removeUnselectableStyle(Element element) {
    element.removeClassName(CommonStyles.get().unselectable());
    element.removeClassName(style.cursor());
  }

  @Override
  public void setProxyStyle(String proxyClass) {
    this.proxyClass = proxyClass;
  }

}
