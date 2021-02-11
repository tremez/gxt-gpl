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
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer.PortalLayoutAppearance;

public class PortalLayoutDefaultAppearance implements PortalLayoutAppearance {

  public interface PortalLayoutTemplate extends XTemplates {
    @XTemplate("<div class='{style.insert}'><div></div></div>")
    SafeHtml render(PortalLayoutStyle style);
  }

  public interface PortalLayoutResources extends ClientBundle {
    @Source("PortalLayout.gss")
    PortalLayoutStyle css();
  }

  public interface PortalLayoutStyle extends CssResource {
    String insert();
  }

  private final PortalLayoutResources resources;
  private PortalLayoutTemplate template;

  public PortalLayoutDefaultAppearance() {
    this(GWT.<PortalLayoutResources> create(PortalLayoutResources.class));
  }

  public PortalLayoutDefaultAppearance(PortalLayoutResources resources) {
    this.resources = resources;
    this.resources.css().ensureInjected();

    this.template = GWT.create(PortalLayoutTemplate.class);
  }

  @Override
  public void renderInsert(SafeHtmlBuilder sb) {
    sb.append(template.render(resources.css()));
  }

}
