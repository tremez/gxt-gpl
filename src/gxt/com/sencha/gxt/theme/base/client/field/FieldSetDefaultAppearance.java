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
package com.sencha.gxt.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.form.FieldSet.FieldSetAppearance;

public class FieldSetDefaultAppearance implements FieldSetAppearance {

  public interface Template extends XTemplates {
    @XTemplate(source = "FieldSet.html")
    SafeHtml render(FieldSetStyle style, boolean isGecko);
  }
  
  public interface FieldSetResources extends ClientBundle {

    @Source({"FieldSet.gss"})
    FieldSetStyle css();

  }

  public interface FieldSetStyle extends CssResource {
    String fieldSet();
    
    String legend();
    
    String toolWrap();
    
    String header();
    
    String body();
    
    String collapsed();
    
    String noborder();
  }

  private final FieldSetResources resources;
  private final FieldSetStyle style;
  private final Template template;

  public FieldSetDefaultAppearance() {
    this(GWT.<FieldSetResources> create(FieldSetResources.class));
  }

  public FieldSetDefaultAppearance(FieldSetResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
   
    StyleInjectorHelper.ensureInjected(this.style, true);
    
    this.template = GWT.create(Template.class);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style, GXT.isGecko()));
  }

  @Override
  public XElement getHeadingElement(XElement parent) {
    return parent.selectNode("." + style.header());
  }

  @Override
  public XElement getToolElement(XElement parent) {
    return parent.selectNode("." + style.toolWrap());
  }

  @Override
  public XElement getContainerTarget(XElement parent) {
    return parent.selectNode("." + style.body());
  }

  @Override
  public IconButton.IconConfig collapseIcon() {
    return ToolButton.UP;
  }

  @Override
  public IconButton.IconConfig expandIcon() {
    return ToolButton.DOWN;
  }

  @Override
  public void onCollapse(XElement parent, boolean collapse) {
    
    parent.setClassName(style.collapsed(), collapse);
  }

}
