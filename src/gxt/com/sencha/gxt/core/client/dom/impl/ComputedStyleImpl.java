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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.shared.FastMap;

public class ComputedStyleImpl {

  protected Map<String, String> camelCache = new FastMap<String>();
  protected Map<String, String> hyphenCache = new FastMap<String>();

  public FastMap<String> getStyleAttribute(Element elem, List<String> names) {
    return getComputedStyle(elem, names, checkHyphenCache(names), checkCamelCache(names), null);
  }

  public String getStyleAttribute(Element elem, String prop) {
    return getComputedStyle(elem, checkHyphenCache(prop), checkCamelCache(prop), null);
  }

  public void setStyleAttribute(Element elem, String name, Object value) {
    elem.getStyle().setProperty(checkCamelCache(name), value == null ? "" : String.valueOf(value));
  }

  protected List<String> checkCamelCache(List<String> l) {
    List<String> list = new ArrayList<String>(l);
    for (int i = 0; i < list.size(); i++) {
      String t = checkCamelCache(list.get(i));
      list.set(i, t);
    }
    return list;
  }

  protected String checkCamelCache(String s) {
    String t = camelCache.get(s);
    if (t == null) {
      t = Format.camelize(getPropertyName(s));
      camelCache.put(s, t);
    }
    return t;
  }

  protected List<String> checkHyphenCache(List<String> l) {
    List<String> list = new ArrayList<String>(l);
    for (int i = 0; i < list.size(); i++) {
      String s = list.get(i);
      String t = checkHyphenCache(s);
      list.set(i, t);
    }
    return list;
  }

  protected String checkHyphenCache(String s) {
    String t = hyphenCache.get(s);
    if (t == null) {
      t = Format.hyphenize(getPropertyName(s));
      hyphenCache.put(s, t);
    }
    return t;
  }

  protected String getPropertyName(String name) {
    if ("float".equals(name)) {
      return "cssFloat";
    }
    return name;
  }
  
  protected native String getComputedStyle(Element elem, String name, String name2, String psuedo) /*-{
    var v = elem.style[name2];
    if (v) {
      return String(v);
    }
    var cStyle = $doc.defaultView.getComputedStyle(elem, psuedo);
    return cStyle ? String(cStyle.getPropertyValue(name)) : null;
  }-*/;

  protected native FastMap<String> getComputedStyle(Element elem, List<String> originals, List<String> names,
      List<String> names2, String pseudo) /*-{
    var cStyle;
    var map = @com.sencha.gxt.core.shared.FastMap::new()();
    var size = originals.@java.util.List::size()();
    for(var i = 0;i<size;i++){
      var original = originals.@java.util.List::get(I)(i);

      var name2 = names2.@java.util.List::get(I)(i);
      var v = elem.style[name2];
      if(v){
        map.@com.sencha.gxt.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,String(v));
        continue;
      }
      var name = names.@java.util.List::get(I)(i);
      if(!cStyle){
        cStyle = $doc.defaultView.getComputedStyle(elem, pseudo);
      }
      map.@com.sencha.gxt.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,cStyle ? String(cStyle.getPropertyValue(name)) : null);
    }
    return map;
  }-*/;

}
