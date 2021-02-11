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
package com.sencha.gxt.theme.base.client.status;

import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.widget.core.client.Status;

public abstract class BoxStatusBaseAppearance implements Status.StatusAppearance {

  public interface BoxStatusResources {
    @Source("com/sencha/gxt/theme/base/client/grid/loading.gif")
    ImageResource loading();

    BoxStatusStyle style();

  }

  public interface BoxStatusStyle extends CssResource {

    String status();

    String statusBox();

    String statusIcon();

    String statusText();

  }

  public interface BoxTemplate extends XTemplates {

    @XTemplate(source = "BoxStatus.html")
    SafeHtml render(BoxStatusStyle style);

  }

  private final BoxStatusResources resources;
  private final BoxStatusStyle style;
  private BoxTemplate template;

  public BoxStatusBaseAppearance(BoxStatusResources resources, BoxTemplate template) {
    this.resources = resources;
    style = resources.style();
    style.ensureInjected();

    this.template = template;
  }

  @Override
  public ImageResource getBusyIcon() {
    return resources.loading();
  }

  @Override
  public XElement getHtmlElement(XElement parent) {
    return parent.selectNode("." + style.statusText());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void onUpdateIcon(XElement parent, ImageResource icon) {
    XElement iconWrap = parent.selectNode("." + style.statusIcon());
    iconWrap.setVisible(icon != null);
    if (icon != null) {
      iconWrap.removeChildren();
      iconWrap.appendChild(IconHelper.getElement(icon));
    }
  }
}
