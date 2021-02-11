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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import com.sencha.gxt.examples.resources.server.EntityManagerUtil;

@Entity
public class Folder {

  public static final EntityManager entityManager() {
    return EntityManagerUtil.getEntityManager();
  }

  public static List<Folder> findAllFolders() {
    EntityManager em = entityManager();
    List<Folder> list = em.createQuery("select f from Folder f", Folder.class).getResultList();
    return list;
  }

  public static Folder findFolder(Integer id) {
    if (id == null) {
      return null;
    }
    EntityManager em = entityManager();
    Folder session = em.find(Folder.class, id);
    return session;
  }

  public static Folder getRootFolder() {
    EntityManager em = entityManager();
    TypedQuery<Folder> q = em.createQuery("select f from Folder f where f.parentFolder is null", Folder.class);
    return q.getSingleResult();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Version
  private Integer version;

  @Size(min = 1, max = 100)
  private String name;

  @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {})
  private Folder parentFolder;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Folder> subFolders = new ArrayList<Folder>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Music> children = new ArrayList<Music>();

  public void addMusic(Music music) {
    children.add(music);
  }

  public void addSubFolder(Folder subFolder) {
    subFolder.parentFolder = this;
    subFolders.add(subFolder);
  }

  public List<Music> getChildren() {
    return children;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Folder> getSubFolders() {
    return subFolders;
  }

  public Integer getVersion() {
    return version;
  }

  public Folder persist() {
    EntityManager em = entityManager();
    return em.merge(this);
  }

  public void remove() {
    EntityManager em = entityManager();
    Folder attached = em.find(Folder.class, this.id);
    em.remove(attached);
  }

  public void setChildren(List<Music> children) {
    this.children = children;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSubFolders(List<Folder> subFolders) {
    this.subFolders = subFolders;
  }

  public Folder getParentFolder() {
    return parentFolder;
  }

}
