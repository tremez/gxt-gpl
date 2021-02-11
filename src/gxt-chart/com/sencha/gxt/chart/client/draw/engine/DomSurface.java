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
package com.sencha.gxt.chart.client.draw.engine;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.chart.client.draw.Surface;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.core.client.dom.XElement;

public abstract class DomSurface extends Surface {

  protected Map<Sprite, XElement> elements = new HashMap<Sprite, XElement>();
  protected Map<Sprite, String> spriteIds = new HashMap<Sprite, String>();

  /**
   * Assigns an ID to be set in the sprite's dom element once it has been created. Only makes sense Surface 
   * implementations that are built using a dom. Requires the sprite to be rendered or re-rendered to take effect.
   * 
   * @see Element#setId(String)
   * @param sprite the sprite
   * @param id the id to set.
   */
  public void setId(Sprite sprite, String id) {
    spriteIds.put(sprite, id);
  }

  /**
   * Returns the DOM element associated with the given sprite.
   * 
   * @param sprite the sprite
   * @return the DOM element
   */
  protected XElement getElement(Sprite sprite) {
    return elements.get(sprite);
  }

  /**
   * Associates the given sprite with the given dom element.
   * 
   * @param sprite the sprite
   * @param element the dom element
   */
  protected void setElement(Sprite sprite, XElement element) {
    elements.put(sprite, element);
  }

  @Override
  public void deleteSprite(Sprite sprite) {
    super.deleteSprite(sprite);
    if (surfaceElement != null) {
      //TODO - when chart sets shadow to false, elements are created, but not added to DOM.  element is null
      Element spriteElement = getElement(sprite);
      if (spriteElement != null) {
        surfaceElement.removeChild(spriteElement);
      }
    }
    elements.remove(sprite);
    spriteIds.remove(sprite);
  }
}
