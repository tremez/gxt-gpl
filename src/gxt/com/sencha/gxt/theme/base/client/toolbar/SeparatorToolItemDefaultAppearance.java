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
package com.sencha.gxt.theme.base.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem.SeparatorToolItemAppearance;

public class SeparatorToolItemDefaultAppearance implements SeparatorToolItemAppearance {

  public interface SeparatorToolItemResources extends ClientBundle {

    @Source("SeparatorToolItem.gss")
    SeparatorToolItemStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource background();
  }
  
  public interface SeparatorToolItemStyle extends CssResource {
    
    String separator();
  }
  
  public interface Template extends XTemplates {
    @XTemplate("<div class=\"{style.separator}\"></div>")
    SafeHtml render(SeparatorToolItemStyle style);
  }
  
  protected final SeparatorToolItemStyle style;
  protected Template template;
  
  public SeparatorToolItemDefaultAppearance() {
    this(GWT.<SeparatorToolItemResources>create(SeparatorToolItemResources.class), GWT.<Template>create(Template.class));
  }
  
  public SeparatorToolItemDefaultAppearance(SeparatorToolItemResources resources, Template template) {
    this.style = resources.css();
    this.style.ensureInjected();
    this.template = template; 
  }
  
  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

}
