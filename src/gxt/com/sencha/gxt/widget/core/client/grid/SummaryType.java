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
package com.sencha.gxt.widget.core.client.grid;

import java.util.List;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * Calculates the value of a summary column.
 * 
 * @param <N> the value type
 * @param <O> the return type
 */
public interface SummaryType<N, O extends Number> {

  public static final class AvgSummaryType<N> implements SummaryType<N, Double> {
    @Override
    public <M> Double calculate(List<? extends M> l, ValueProvider<? super M, N> valueProvider) {
      double avg = 0d;
      for (int i = 0, len = l.size(); i < len; i++) {
        N d = valueProvider.getValue(l.get(i));
        if (d instanceof Number) {
          avg += ((Number) d).doubleValue();
        }
      }
      return avg / l.size();
    }
  }

  public static final class CountSummaryType<V> implements SummaryType<V, Integer> {
    @Override
    public <M> Integer calculate(List<? extends M> l, ValueProvider<? super M, V> valueProvider) {
      return l.size();
    }
  }

  public static final class MaxSummaryType<N> implements SummaryType<N, Double> {
    @Override
    public <M> Double calculate(List<? extends M> l, ValueProvider<? super M, N> valueProvider) {
      Double max = null;
      for (int i = 0, len = l.size(); i < len; i++) {
        N d = valueProvider.getValue(l.get(i));
        if (d instanceof Number) {
          if (max == null) {
            max = ((Number) d).doubleValue();
          } else {
            max = Math.max(((Number) d).doubleValue(), max);
          }
        }
      }
      return max == null ? 0d : max;

    }
  }

  public static final class MinSummaryType<N> implements SummaryType<N, Double> {
    @Override
    public <M> Double calculate(List<? extends M> l, ValueProvider<? super M, N> valueProvider) {
      Double min = null;
      for (int i = 0, len = l.size(); i < len; i++) {
        N d = valueProvider.getValue(l.get(i));
        if (d instanceof Number) {
          if (min == null) {
            min = ((Number) d).doubleValue();
          } else {
            min = Math.min(((Number) d).doubleValue(), min);
          }
        }
      }
      return min == null ? 0d : min;
    }
  }

  public static final class SumSummaryType<N> implements SummaryType<N, Double> {
    @Override
    public <M> Double calculate(List<? extends M> l, ValueProvider<? super M, N> valueProvider) {
      Double sum = 0d;
      for (int i = 0, len = l.size(); i < len; i++) {
        N d = valueProvider.getValue(l.get(i));
        if (d instanceof Number) {
          sum += ((Number) d).doubleValue();
        }
      }
      return sum;
    }
  }

  /**
   * Returns the value for a summary calculation.
   * 
   * @param models the list of models
   * @param vp the value provider
   * @return the summary value
   */
  public abstract <M> O calculate(List<? extends M> models, ValueProvider<? super M, N> vp);

}
