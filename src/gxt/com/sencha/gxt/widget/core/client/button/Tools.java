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
package com.sencha.gxt.widget.core.client.button;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public  class Tools {

  private ToolResources resources;
  private ToolStyle style;
  
  Tools() {
   
  }

  public Tools(ToolResources resources) {
    this.resources = resources;
    this.style = this.resources.style();
    this.style.ensureInjected();
  }
  
  public ToolResources resources() {
    return resources;
  }
  
  public ToolStyle icons() {
    return style;
  }

  public interface ToolStyle extends CssResource {

    String close();

    String closeOver();

    String collapse();

    String collapseOver();

    String doubleDown();

    String doubleDownOver();

    String doubleLeft();

    String doubleLeftOver();

    String doubleRight();

    String doubleRightOver();

    String doubleUp();

    String doubleUpOver();

    String down();

    String downOver();

    String expand();

    String expandOver();

    String gear();

    String gearOver();

    String left();

    String leftOver();

    String maximize();

    String maximizeOver();

    String minimize();

    String minimizeOver();

    String minus();

    String minusOver();

    String pin();

    String pinOver();

    String unpin();

    String unpinOver();

    String plus();

    String plusOver();

    String print();

    String printOver();

    String question();

    String questionOver();

    String refresh();

    String refreshOver();

    String restore();

    String restoreOver();

    String right();

    String rightOver();

    String save();

    String saveOver();

    String search();

    String searchOver();

    String up();

    String upOver();

  }

  public interface ToolResources {

    ToolStyle style();

    ImageResource closeIcon();

    ImageResource closeOverIcon();

    ImageResource collapseIcon();

    ImageResource collapseOverIcon();

    ImageResource doubleDownIcon();

    ImageResource doubleDownOverIcon();

    ImageResource doubleLeftIcon();

    ImageResource doubleLeftOverIcon();

    ImageResource doubleRightIcon();

    ImageResource doubleRightOverIcon();

    ImageResource doubleUpIcon();

    ImageResource doubleUpOverIcon();

    ImageResource downIcon();

    ImageResource downOverIcon();

    ImageResource expandIcon();

    ImageResource expandOverIcon();

    ImageResource gearIcon();

    ImageResource gearOverIcon();

    ImageResource leftIcon();

    ImageResource leftOverIcon();

    ImageResource maximizeIcon();

    ImageResource maximizeOverIcon();

    ImageResource minimizeIcon();

    ImageResource minimizeOverIcon();

    ImageResource minusIcon();

    ImageResource minusOverIcon();

    ImageResource pinIcon();

    ImageResource pinOverIcon();

    ImageResource unpinIcon();

    ImageResource unpinOverIcon();

    ImageResource plusIcon();

    ImageResource plusOverIcon();

    ImageResource printIcon();

    ImageResource printOverIcon();

    ImageResource questionIcon();

    ImageResource questionOverIcon();

    ImageResource refreshIcon();

    ImageResource refreshOverIcon();

    ImageResource restoreIcon();

    ImageResource restoreOverIcon();

    ImageResource rightIcon();

    ImageResource rightOverIcon();

    ImageResource saveIcon();

    ImageResource saveOverIcon();

    ImageResource searchIcon();

    ImageResource searchOverIcon();

    ImageResource upIcon();

    ImageResource upOverIcon();

  }
}
