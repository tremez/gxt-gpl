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
package com.sencha.gxt.examples.resources.server.data;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import com.sencha.gxt.examples.resources.server.EntityManagerUtil;

@Entity
public class Music {

  public static final EntityManager entityManager() {
    return EntityManagerUtil.getEntityManager();
  }

  public static Music findMusic(Integer id) {
    if (id == null) {
      return null;
    }
    EntityManager em = entityManager();
    return em.find(Music.class, id);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Integer id;

  @Version
  private Integer version;

  @Size(min = 1, max = 100)
  private String name;

  @Size(min = 1,max = 100)
  private String author;

  private String genre;


  public String getAuthor() {
    return author;
  }

  public String getGenre() {
    return genre;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getVersion() {
    return version;
  }

  public Music persist() {
    EntityManager em = entityManager();

    EntityTransaction tx = em.getTransaction();
    if (tx.isActive()) {
      return em.merge(this);
    } else {
      tx.begin();
      Music saved = em.merge(this);
      tx.commit();
      return saved;
    }
  }

  public void remove() {
    EntityManager em = entityManager();
    em.remove(this);

  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

}
