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
package com.sencha.gxt.theme.base.client.button;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.frame.TableFrame.TableFrameResources;
import com.sencha.gxt.theme.base.client.frame.TableFrame.TableFrameStyle;

public interface ButtonTableFrameResources extends TableFrameResources, ClientBundle {
  
  @Source({"com/sencha/gxt/theme/base/client/frame/TableFrame.gss", "com/sencha/gxt/theme/base/client/button/ButtonTableFrame.gss"})
  @Override
  TableFrameStyle style();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource background();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource backgroundOverBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource backgroundPressedBorder();
  
  @Override
  ImageResource topLeftBorder();
  
  ImageResource topLeftOverBorder();
  
  ImageResource topLeftPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  @Override
  ImageResource topBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource topOverBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource topPressedBorder();
  
  @Override
  ImageResource topRightBorder();
  
  ImageResource topRightOverBorder();
  
  ImageResource topRightPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  @Override
  ImageResource leftBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource leftOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource leftPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  @Override
  ImageResource rightBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource rightOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource rightPressedBorder();
  
  @Override
  ImageResource bottomLeftBorder();

  ImageResource bottomLeftOverBorder();

  ImageResource bottomLeftPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  @Override
  ImageResource bottomBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource bottomOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource bottomPressedBorder();
  
  @Override
  ImageResource bottomRightBorder();

  ImageResource bottomRightOverBorder();

  ImageResource bottomRightPressedBorder();

}

