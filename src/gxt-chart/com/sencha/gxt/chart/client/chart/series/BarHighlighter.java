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
package com.sencha.gxt.chart.client.chart.series;

import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;

/**
 * Highlighting effects used by {@link BarSeries}.
 */
public class BarHighlighter implements SeriesHighlighter {

  @Override
  public void highlight(Sprite sprite) {
    if (sprite instanceof RectangleSprite) {
      RectangleSprite bar = (RectangleSprite) sprite;
      bar.setStroke(new RGB(85, 85, 204));
      if (((Chart<?>)sprite.getComponent()).isAnimated()) {
        DrawFx.createStrokeWidthAnimator(bar, 3).run(250);
        DrawFx.createOpacityAnimator(bar, 0.8).run(250);
      } else {
        bar.setStrokeWidth(3);
        bar.setOpacity(0.8);
        bar.redraw();
      }
    }
  }

  @Override
  public void unHighlight(Sprite sprite) {
    if (sprite instanceof RectangleSprite) {
      RectangleSprite bar = (RectangleSprite) sprite;
      bar.setStroke(Color.NONE);
      if (((Chart<?>)sprite.getComponent()).isAnimated()) {
        DrawFx.createStrokeWidthAnimator(bar, 0).run(250);
        DrawFx.createOpacityAnimator(bar, 1).run(250);
      } else {
        bar.setStrokeWidth(0);
        bar.setOpacity(1);
        bar.redraw();
      }
    }
  }

}
