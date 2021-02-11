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
package com.sencha.gxt.examples.resources.client.model;

public class TeamSales {

  private String month;
  private int a;
  private int b;
  private int c;
  
  
  public TeamSales(String month, int a, int b, int c) {
    setMonth(month);
    setAlphaSales(a);
    setBetaSales(b);
    setGammaSales(c);
//    setAvgSales();
  }

  public int getAlphaSales() {
    return a;
  }

  public int getBetaSales() {
    return b;
  }

  public int getGammaSales() {
    return c;
  }

  public String getMonth() {
    return month;
  }
  
//  @Override
//  public void notify(ChangeEvent evt) {
//    super.notify(evt);
//
//    PropertyChangeEvent e = (PropertyChangeEvent) evt;
//    if (!e.getName().equals("avgsales")) {
//      setAvgSales();
//    }
//  }

  public void setAlphaSales(int sales) {
    this.a = sales;
  }

//  public void setAvgSales() {
//    if (get("alphasales") != null && get("gammasales") != null
//        && get("betasales") != null) {
//      double avg = (getAlphaSales() + getBetaSales() + getGammaSales()) / 3.0;
//      set("avgsales", avg);
//    }
//  }

  public void setBetaSales(int sales) {
    this.b = sales;
  }

  public void setGammaSales(int sales) {
    this.c = sales;

  }

  public void setMonth(String month) {
    this.month = month;
  }
}
