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
package com.sencha.gxt.widget.core.client.tree;

import com.google.gwt.resources.client.ImageResource;

/**
 * Style information for Trees. There are two types for tree items: nodes and
 * leafs. Leafs are item's without children. Nodes are items with children or
 * items with their leaf flag set to false.
 */
public class TreeStyle {

  private ImageResource leafIcon;
  private ImageResource nodeCloseIcon;
  private ImageResource nodeOpenIcon;
  private ImageResource jointOpenIcon;
  private ImageResource jointCloseIcon;

  public ImageResource getJointCloseIcon() {
    return jointCloseIcon;
  }

  public ImageResource getJointOpenIcon() {
    return jointOpenIcon;
  }

  /**
   * Returns the icon for leaf items.
   * 
   * @return the leaf icon
   */
  public ImageResource getLeafIcon() {
    return leafIcon;
  }

  /**
   * Returns the global closed node icon.
   * 
   * @return the node closed icon
   */
  public ImageResource getNodeCloseIcon() {
    return nodeCloseIcon;
  }

  /**
   * Returns the global open node icon.
   * 
   * @return the node open icon
   */
  public ImageResource getNodeOpenIcon() {
    return nodeOpenIcon;
  }

  public void setJointCloseIcon(ImageResource jointCloseIcon) {
    this.jointCloseIcon = jointCloseIcon;
  }

  public void setJointOpenIcon(ImageResource jointOpenIcon) {
    this.jointOpenIcon = jointOpenIcon;
  }

  /**
   * Sets the global icon style for leaf tree items.
   * 
   * @param itemIcon the leaf icon
   */
  public void setLeafIcon(ImageResource itemIcon) {
    this.leafIcon = itemIcon;
  }

  /**
   * Sets the icon used for closed tree items.
   * 
   * @param folderCloseIcon the closed folder icon
   */
  public void setNodeCloseIcon(ImageResource folderCloseIcon) {
    this.nodeCloseIcon = folderCloseIcon;
  }

  /**
   * Sets the global icon for expanded tree items.
   * 
   * @param folderOpenIcon the open folder icon
   */
  public void setNodeOpenIcon(ImageResource folderOpenIcon) {
    this.nodeOpenIcon = folderOpenIcon;
  }

}
