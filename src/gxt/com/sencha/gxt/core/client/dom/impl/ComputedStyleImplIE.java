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
package com.sencha.gxt.core.client.dom.impl;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.shared.FastMap;

public class ComputedStyleImplIE extends ComputedStyleImpl {

  @Override
  public FastMap<String> getStyleAttribute(Element elem, List<String> names) {
    return getComputedStyle(elem, names, checkCamelCache(names), null, null);
  }

  @Override
  public String getStyleAttribute(Element elem, String prop) {
    return getComputedStyle(elem, checkCamelCache(prop), null, null);
  }

  @Override
  public void setStyleAttribute(Element elem, String name, Object value) {
    if (!GXT.isIE10() && "opacity".equals(name)) {
      setOpacity(elem, Double.valueOf((String.valueOf(value))));
    } else {
      super.setStyleAttribute(elem, name, value);
    }
  }

  @Override
  protected native String getComputedStyle(Element elem, String name, String name2, String psuedo) /*-{
    var v, cs;
    if (name == "opacity") {
      if (typeof elem.style.filter == "string") {
        var m = elem.style.filter.match(/alpha\(opacity=(.*)\)/i);
        if (m) {
          v = parseFloat(m[1]);
          if (!isNaN(v)) {
            return String(v ? v / 100 : 0);
          }
        }
      }
      return "1";
    }
    if (v = elem.style[name]) {
      return v;
    } else if ((cs = elem.currentStyle) && (v = cs[name])) {
      return v;
    }
    return null;
  }-*/;

  @Override
  protected native FastMap<String> getComputedStyle(Element elem, List<String> originals, List<String> names, List<String> names2, String pseudo) /*-{
    var map = @com.sencha.gxt.core.shared.FastMap::new()();
    var size = originals.@java.util.List::size()()
    for(var i = 0;i<size;i++){
      var name = names.@java.util.List::get(I)(i);
      var original = originals.@java.util.List::get(I)(i);

      if(name == "opacity"){
        if(typeof elem.style.filter == "string"){
          var m = elem.style.filter.match(/alpha\(opacity=(.*)\)/i);
          if(m){
            var fv = parseFloat(m[1]);
            if(!isNaN(fv)){
              map.@com.sencha.gxt.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,String(fv ? fv / 100 : 0));
              continue;
            }
          }
        }
        map.@com.sencha.gxt.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,String(1));
        continue;
      }

      var v, cs;
      if(v = elem.style[name]){
        map.@com.sencha.gxt.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,String(v));
      } else if(cs = elem.currentStyle) {
        map.@com.sencha.gxt.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original, cs[name] ? String(cs[name]) : null);
      } else {
        map.@com.sencha.gxt.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,null);
      }
    }
    return map;
  }-*/;

  @Override
  protected String getPropertyName(String name) {
    if ("float".equals(name)) {
      return "styleFloat";
    }
    return name;
  }

  protected native void setOpacity(Element dom, double opacity)/*-{
    dom.style.zoom = 1;
    dom.style.filter = (dom.style.filter || '').replace(/alpha\([^\)]*\)/gi,"") + (opacity == 1 ? "" : " alpha(opacity=" + opacity * 100 + ")");
  }-*/;

}
