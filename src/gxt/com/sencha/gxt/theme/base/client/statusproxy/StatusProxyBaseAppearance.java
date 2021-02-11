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
package com.sencha.gxt.theme.base.client.statusproxy;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.dnd.core.client.StatusProxy.StatusProxyAppearance;

public abstract class StatusProxyBaseAppearance implements StatusProxyAppearance {

  public interface StatusProxyResources {

    ImageResource dropAllowed();

    ImageResource dropNotAllowed();

    StatusProxyStyle style();

  }

  public interface StatusProxyStyle extends CssResource {

    String dragGhost();

    String dropAllowed();

    String dropDisallowed();

    String dropIcon();

    String proxy();

  }

  public interface StatusProxyTemplates extends XTemplates {

    @XTemplate("<div class=\"{style.proxy}\"><div class=\"{style.dropIcon}\"></div><div class=\"{style.dragGhost}\"></div></div>")
    SafeHtml template(StatusProxyBaseAppearance.StatusProxyStyle style);

  }

  private final StatusProxyTemplates templates;
  private final StatusProxyResources resources;
  private final StatusProxyStyle style;

  public StatusProxyBaseAppearance(StatusProxyBaseAppearance.StatusProxyResources resources,
      StatusProxyBaseAppearance.StatusProxyTemplates templates) {
    this.resources = resources;
    style = resources.style();
    this.templates = templates;

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(templates.template(style));
  }

  @Override
  public void setStatus(Element parent, boolean allowed) {
    if (allowed) {
      setStatus(parent, resources.dropAllowed());
    } else {
      setStatus(parent, resources.dropNotAllowed());
    }
  }

  @Override
  public void setStatus(Element parent, ImageResource icon) {
    XElement wrap = iconWrap(parent);
    wrap.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    if (icon != null) {
      wrap.appendChild(getImage(icon));
    }
  }

  @Override
  public void update(Element parent, SafeHtml html) {
    getDragGhost(parent).setInnerSafeHtml(html);
  }

  protected XElement iconWrap(Element parent) {
    return parent.getFirstChildElement().cast();
  }

  private Element getDragGhost(Element parent) {
    return XElement.as(parent).select("." + style.dragGhost()).getItem(0);
  }

  private Element getImage(ImageResource ir) {
    return AbstractImagePrototype.create(ir).createElement();
  }

}
