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
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.Scaling;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;

/**
 * Highlighting effects used by {@link ScatterSeries}.
 */
public class ScatterHighlighter implements SeriesHighlighter {

  @Override
  public void highlight(Sprite sprite) {
    if (((Chart<?>)sprite.getComponent()).isAnimated()) {
      DrawFx.createScalingAnimator(sprite, new Scaling(1.2, 1.2, 0, 0)).run(250);
    } else {
      sprite.setScaling(new Scaling(1.2, 1.2, 0, 0));
      sprite.redraw();
    }
  }

  @Override
  public void unHighlight(Sprite sprite) {
    if (((Chart<?>)sprite.getComponent()).isAnimated()) {
      DrawFx.createScalingAnimator(sprite, new Scaling(1, 1, 0, 0)).run(250);
    } else {
      sprite.setScaling(new Scaling(1, 1, 0, 0));
      sprite.redraw();
    }
  }

}
