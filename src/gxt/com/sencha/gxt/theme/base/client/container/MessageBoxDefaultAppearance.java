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
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.box.MessageBox.MessageBoxAppearance;


public class MessageBoxDefaultAppearance implements MessageBoxAppearance {

  public interface MessageBoxResources extends ClientBundle {
    
    @Source("MessageBox.gss")
    MessageBoxBaseStyle style();
  }
  
  public interface MessageBoxBaseStyle extends CssResource {
    String content();
    
    String icon();
    
    String message();
  }
  
  interface Template extends XTemplates {
    @XTemplate(source = "MessageBox.html")
    SafeHtml render(MessageBoxBaseStyle style);
  }
  
  protected MessageBoxResources resources;
  protected MessageBoxBaseStyle style;
  protected Template template;

  public MessageBoxDefaultAppearance() {
    this(GWT.<MessageBoxResources>create(MessageBoxResources.class));
  }
  
  public MessageBoxDefaultAppearance(MessageBoxResources resources) {
    this.resources = resources;
    this.style = resources.style();
    
    StyleInjectorHelper.ensureInjected(this.style, true);
    
    this.template = GWT.<Template>create(Template.class);
  }

  @Override
  public XElement getContentElement(XElement parent) {
    return parent.selectNode("." + style.content());
  }

  @Override
  public XElement getIconElement(XElement parent) {
    return parent.selectNode("." + style.icon());
  }

  @Override
  public XElement getMessageElement(XElement parent) {
    return parent.selectNode("." + style.message());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }
  
}
