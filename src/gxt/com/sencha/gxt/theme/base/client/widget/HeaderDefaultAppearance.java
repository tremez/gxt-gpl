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
package com.sencha.gxt.theme.base.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.widget.core.client.Header.HeaderAppearance;

public class HeaderDefaultAppearance implements HeaderAppearance {

  public interface HeaderStyle extends CssResource {
    String header();

    String headerBar();

    String headerHasIcon();

    String headerIcon();

    String headerText();
  }

  public interface HeaderResources extends ClientBundle {

    @Source("Header.gss")
    HeaderStyle style();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "Header.html")
    SafeHtml render(HeaderStyle style);
  }

  private final HeaderResources resources;
  private Template template;
  private final HeaderStyle style;

  public HeaderDefaultAppearance() {
    this(GWT.<HeaderResources> create(HeaderResources.class), GWT.<Template> create(Template.class));
  }

  public HeaderDefaultAppearance(HeaderResources resources) {
    this(resources, GWT.<Template> create(Template.class));
  }

  public HeaderDefaultAppearance(HeaderResources resources, Template template) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);

    this.template = template;
  }

  @Override
  public XElement getBarElem(XElement parent) {
    return parent.getChild(1).cast();
  }

  @Override
  public XElement getHeadingElem(XElement parent) {
    return parent.getChild(2).cast();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void setIcon(XElement parent, ImageResource icon) {
    XElement iconWrap = parent.getFirstChildElement().cast();
    iconWrap.removeChildren();
    if (icon != null) {
      iconWrap.appendChild(IconHelper.getElement(icon));
    }
    parent.setClassName(style.headerHasIcon(), icon != null);
  }

}
