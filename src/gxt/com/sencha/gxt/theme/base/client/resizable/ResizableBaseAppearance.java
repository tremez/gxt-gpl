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
package com.sencha.gxt.theme.base.client.resizable;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.Resizable.ResizableAppearance;

public class ResizableBaseAppearance implements ResizableAppearance {

  public interface ResizableResources extends ClientBundle {
    @Source("Resizable.gss")
    ResizableStyle style();

  }

  public interface ResizableStyle extends CssResource {

    String handle();

    String handleEast();

    String handleNorth();

    String handleNortheast();

    String handleNorthwest();

    String handleSouth();

    String handleSoutheast();

    String handleSouthwest();

    String handleWest();

    String over();

    String overlay();

    String pinned();

    String proxy();

  }

  private final ResizableResources resources;
  private final ResizableStyle style;

  public ResizableBaseAppearance() {
    this(GWT.<ResizableResources>create(ResizableResources.class));
  }

  public ResizableBaseAppearance(ResizableResources resources) {
    this.resources = resources;
    this.style = resources.style();
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public Element createProxy() {
    XElement elem = Document.get().createDivElement().cast();
    elem.addClassName(resources.style().proxy());
    elem.disableTextSelection(true);
    return elem;
  }

  public String getHandleStyles(Dir dir) {
    StringBuilder styleBuilder = new StringBuilder();
    styleBuilder.append(style.handle());
    styleBuilder.append(' ');
    switch (dir) {
      case E:
        styleBuilder.append(style.handleEast());
        break;
      case N:
        styleBuilder.append(style.handleNorth());
        break;
      case W:
        styleBuilder.append(style.handleWest());
        break;
      case S:
        styleBuilder.append(style.handleSouth());
        break;
      case NE:
        styleBuilder.append(style.handleNortheast());
        break;
      case NW:
        styleBuilder.append(style.handleNorthwest());
        break;
      case SW:
        styleBuilder.append(style.handleSouthwest());
        break;
      case SE:
        styleBuilder.append(style.handleSoutheast());
        break;
    }
    return styleBuilder.toString();
  }

}
