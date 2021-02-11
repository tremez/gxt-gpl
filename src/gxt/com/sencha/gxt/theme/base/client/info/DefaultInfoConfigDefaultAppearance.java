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
package com.sencha.gxt.theme.base.client.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;

public class DefaultInfoConfigDefaultAppearance implements DefaultInfoConfig.DefaultInfoConfigAppearance {

  public interface DefaultInfoConfigStyle extends CssResource {
    String infoTitle();

    String infoMessage();
  }

  public interface InfoConfigResources extends ClientBundle {
    @Source("InfoDefault.gss")
    DefaultInfoConfigStyle style();
  }

  private InfoConfigResources resources;
  private DefaultInfoConfigStyle style;

  public DefaultInfoConfigDefaultAppearance() {
    this((InfoConfigResources)GWT.create(InfoConfigResources.class));
  }

  public DefaultInfoConfigDefaultAppearance(InfoConfigResources resources) {
    this.resources = resources;
    this.style = this.resources.style();
    this.style.ensureInjected();
  }

  @Override
  public void render(SafeHtmlBuilder sb, SafeHtml title, SafeHtml message) {

    if (title != SafeHtmlUtils.EMPTY_SAFE_HTML) {
      sb.appendHtmlConstant("<div class='" + style.infoTitle() + "'>");
      sb.append(title);
      sb.appendHtmlConstant("</div>");
    }
    if (message != SafeHtmlUtils.EMPTY_SAFE_HTML) {
      sb.appendHtmlConstant("<div class='" + style.infoMessage() + "'>");
      sb.append(message);
      sb.appendHtmlConstant("</div>");
    }
  }

}
