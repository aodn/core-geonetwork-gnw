//===    Copyright (C) 2001-2007 Food and Agriculture Organization of the
//===    United Nations (FAO-UN), United Nations World Food Programme (WFP)
//===    and United Nations Environment Programme (UNEP)
//===
//===    This program is free software; you can redistribute it and/or modify
//===    it under the terms of the GNU General Public License as published by
//===    the Free Software Foundation; either version 2 of the License, or (at
//===    your option) any later version.
//===
//===    This program is distributed in the hope that it will be useful, but
//===    WITHOUT ANY WARRANTY; without even the implied warranty of
//===    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
//===    General Public License for more details.
//===
//===    You should have received a copy of the GNU General Public License
//===    along with this program; if not, write to the Free Software
//===    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
//===
//===    Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
//===    Rome - Italy. email: geonetwork@osgeo.org
//==============================================================================

package org.fao.geonet.kernel.search.facet;

import org.fao.geonet.kernel.search.Translator;

public class ItemConfig {
    /**
     * Default number of values for a facet
     */
    public static final int DEFAULT_MAX_KEYS = 10;

    /**
     * Default depth of sub categories to count
     */
    public static final int DEFAULT_DEPTH = 1;

    private final Dimension dimension;
    private SortBy sortBy;
    private SortOrder sortOrder;
    private int max;
    private int depth;
    private Format format;
    private String translator;
    private TranslatorFactory translatorFactory;

    public ItemConfig(Dimension dimension, TranslatorFactory translatorFactory) {
        this.dimension = dimension;
        this.translatorFactory = translatorFactory;
        // Defaults
        max = DEFAULT_MAX_KEYS;
        sortBy = SortBy.COUNT;
        sortOrder = SortOrder.DESCENDING;
        depth = DEFAULT_DEPTH;
        format = Format.FACET_NAME;
    }

    /**
     * @return the dimension this config relates to
     */

    public Dimension getDimension() {
        return dimension; 
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * @return the ordering for the facet. Defaults is by {@link Facet.SortBy#COUNT}.
     */

    public SortBy getSortBy() {
        return sortBy;
    }

    /**
     * @return asc or desc. Defaults is {@link Facet.SortOrder#DESCENDING}.
     */
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * @return the depth to go to returning facet values
     */
    public int getDepth() {
        return depth;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return (optional) the number of values to be returned for the facet.
     * Defaults is {@link ItemConfig#DEFAULT_MAX_KEYS} and never greater than
     * {@link ItemConfig#MAX_SUMMARY_KEY}.
     */

    public int getMax() {
        return max;
    }

    /**
     * @return a formatter for creating item summaries
     */

    public Formatter getFormatter() {
        return format.getFormatter(this.dimension);
    }

    public Translator getTranslator(String langCode) {
        return translatorFactory.createTranslator(translator, langCode);
    }

    /**
     * @return a string representation of this configuration item
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("dimension: ");
        sb.append(dimension.getName());
        sb.append("\tmax:");
        sb.append(getMax() + "");
        sb.append("\tsort by");
        sb.append(getSortBy().toString());
        sb.append("\tsort order:");
        sb.append(getSortOrder().toString());
        sb.append("\tdepth:");
        sb.append(Integer.toString(depth));
        sb.append("\tformat:");
        sb.append(format);
        return sb.toString();
    }

}
