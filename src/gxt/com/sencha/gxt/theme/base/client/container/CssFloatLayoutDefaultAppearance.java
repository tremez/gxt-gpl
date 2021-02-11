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
package com.sencha.gxt.theme.base.client.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer.CssFloatLayoutAppearance;

public class CssFloatLayoutDefaultAppearance implements CssFloatLayoutAppearance {

  public interface CssFloatLayoutTemplate extends XTemplates {
    @XTemplate("<div class='{style.container}'><div class='{style.inner}'></div></div>")
    SafeHtml render(CssFloatLayoutStyle style);
  }

  public interface CssFloatLayoutResources extends ClientBundle {
    @Source("CssFloatLayout.gss")
    CssFloatLayoutStyle css();
  }

  public interface CssFloatLayoutStyle extends CssResource {
    String container();

    String inner();
    
    String child();
  }

  private final CssFloatLayoutResources resources;
  private final CssFloatLayoutStyle style;
  private CssFloatLayoutTemplate template;

  public CssFloatLayoutDefaultAppearance() {
    this(GWT.<CssFloatLayoutResources> create(CssFloatLayoutResources.class));
  }

  public CssFloatLayoutDefaultAppearance(CssFloatLayoutResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
    this.style.ensureInjected();

    this.template = GWT.create(CssFloatLayoutTemplate.class);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public XElement getContainerTarget(XElement parent) {
    return parent.getFirstChildElement().cast();
  }
  
  @Override
  public void onInsert(Widget child) {
    child.addStyleName(style.child());
  }

  @Override
  public void onRemove(Widget child) {
    child.removeStyleName(style.child());
  }

}
