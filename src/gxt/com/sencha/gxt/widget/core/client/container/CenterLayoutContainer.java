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
package com.sencha.gxt.widget.core.client.container;

import java.util.logging.Logger;

import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;

/**
 * A layout container that centers its single widget.
 * 
 * <p/>
 * Code Snippet:
 * 
 * <pre>
  public void onModuleLoad() {
    CenterLayoutContainer c = new CenterLayoutContainer();
    c.add(new Label("Hello world"));
    Viewport v = new Viewport();
    v.add(c);
    RootPanel.get().add(v);
  }
 * </pre>
 */
public class CenterLayoutContainer extends SimpleContainer {

  private static Logger logger = Logger.getLogger(CenterLayoutContainer.class.getName());

  @Override
  protected void doLayout() {
    if (widget != null) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("doLayout");
      }
      XElement con = getContainerTarget();
      XElement e = widget.getElement().cast();
      e.makePositionable(true);
      Point p = e.getAlignToXY(con, new AnchorAlignment(Anchor.CENTER, Anchor.CENTER), 0, 0);
      p = e.translatePoints(p);
      applyLayout(widget, new Rectangle(p.getX(), p.getY(), -1, -1));
    }
  }

}
