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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell.TextAreaAppearance;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell.TextAreaCellOptions;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.theme.base.client.field.TextFieldDefaultAppearance.TextFieldStyle;

public class TextAreaDefaultAppearance extends ValueBaseFieldDefaultAppearance implements TextAreaAppearance {

  public interface TextAreaResources extends ValueBaseFieldResources, ClientBundle {

    @Source({"ValueBaseField.gss", "TextField.gss", "TextArea.gss"})
    TextAreaStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource textBackground();

  }

  public interface TextAreaStyle extends TextFieldStyle {

  }

  private final TextAreaResources res;
  private final TextAreaStyle style;

  public TextAreaDefaultAppearance() {
    this(GWT.<TextAreaResources> create(TextAreaResources.class));
  }

  public TextAreaDefaultAppearance(TextAreaResources resources) {
    super(resources);
    this.res = resources;
    this.style = this.res.css();
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.getFirstChildElement().getFirstChildElement().cast();// textarea
  }

  @Override
  public void onResize(XElement parent, int width, int height) {
    Element div = parent.getFirstChildElement();

    Size adj = adjustTextAreaSize(width, height);

    if (width != -1) {
      div.getStyle().setWidth(width, Unit.PX);
      width = adj.getWidth();
      div.getFirstChildElement().getStyle().setWidth(width, Unit.PX);
    }

    if (height != -1) {
      height = adj.getHeight();
      if (height != -1) {
        div.getFirstChildElement().getStyle().setHeight(height, Unit.PX);
      }
    }
  }

  @Override
  public void render(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
    int width = options.getWidth();
    int height = options.getHeight();

    boolean empty = false;

    String name = options.getName() != null ? " name='" + options.getName() + "' " : "";
    String disabled = options.isDisabled() ? " disabled=true" : "";
    String ro = options.isReadonly() ? " readonly" : "";
    String placeholder = options.getEmptyText() != null ? " placeholder='" + SafeHtmlUtils.htmlEscape(options.getEmptyText()) + "' " : "";

    if ((value == null || value.equals("")) && options.getEmptyText() != null) {
      if (GXT.isIE8() || GXT.isIE9()) {
        value = options.getEmptyText();
      }
      empty = true;
    }

    if (width == -1) {
      width = 150;
    }

    String inputStyles = "";
    String wrapStyles = "";

    Size adjusted = adjustTextAreaSize(width, height);

    if (width != -1) {
      wrapStyles += "width:" + width + "px;";
      width = adjusted.getWidth();
      inputStyles += "width:" + width + "px;";
    }

    if (height != -1) {
      height = adjusted.getHeight();
      inputStyles += "height: " + height + "px;";
    }

    String cls = style.area() + " " + style.field();
    if (empty) {
      cls += " " + style.empty();
    }

    if (options instanceof TextAreaCellOptions) {
      TextAreaCellOptions opts = (TextAreaCellOptions) options;
      inputStyles += "resize:" + opts.getResizable().name().toLowerCase() + ";";
    }

    sb.appendHtmlConstant("<div style='" + wrapStyles + "' class='" + style.wrap() + "'>");
    sb.appendHtmlConstant("<textarea " + name + disabled + " style='" + inputStyles + "' type='text' class='" + cls
        + "'" + ro + placeholder + ">");
    sb.append(SafeHtmlUtils.fromString(value));
    sb.appendHtmlConstant("</textarea></div>");
  }

  protected Size adjustTextAreaSize(int width, int height) {
    if (width != -1) {
      // 2px border
      width -= 2;
      
      // 6px margin except for gecko which has 0px margin
      if (!GXT.isGecko()) {
        width -= 6;
      }
    }

    if (height != -1) {
      // 2px border
      height -= 2;

      // 2px margin except gecko which has 0px margin
      if (!GXT.isGecko()) {
        height -= 2;
      }
    }

    return new Size(width, height);
  }

}
