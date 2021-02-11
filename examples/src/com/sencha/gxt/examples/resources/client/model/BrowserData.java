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

public class BrowserData {

  private double IE;
  private double Firefox;
  private double Chrome;
  private double Safari;
  private double Opera;
  private double Other;
  private String date;

  public BrowserData() {
  }

  public BrowserData(double iE, double firefox, double chrome, double safari, double opera, double other, String date) {
    super();
    IE = iE;
    this.Firefox = firefox;
    this.Chrome = chrome;
    this.Safari = safari;
    this.Opera = opera;
    this.Other = other;
    this.date = date;
  }

  public double getChrome() {
    return Chrome;
  }

  public String getDate() {
    return date;
  }

  public double getFirefox() {
    return Firefox;
  }

  public double getIE() {
    return IE;
  }

  public double getOpera() {
    return Opera;
  }

  public double getOther() {
    return Other;
  }

  public double getSafari() {
    return Safari;
  }

  public void setChrome(double chrome) {
    this.Chrome = chrome;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setFirefox(double firefox) {
    this.Firefox = firefox;
  }

  public void setIE(double iE) {
    IE = iE;
  }

  public void setOpera(double opera) {
    this.Opera = opera;
  }

  public void setOther(double other) {
    this.Other = other;
  }

  public void setSafari(double safari) {
    this.Safari = safari;
  }

}
