//=============================================================================
//===    Copyright (C) 2010 Food and Agriculture Organization of the
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

package org.fao.geonet.kernel.search.classifier;

import org.fao.geonet.kernel.KeywordBean;
import org.fao.geonet.kernel.ThesaurusFinder;

public class BroaderTerm extends AbstractBroaderTerm {

    private final String langCode;

    public BroaderTerm(ThesaurusFinder finder, String conceptScheme, String langCode) {
        super(finder, conceptScheme, langCode);
        this.langCode = langCode;
    }

    protected String getCategory(KeywordBean term) {
        return term.getPreferredLabel(langCode);
    }

}
