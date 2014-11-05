//===	Copyright (C) 2001-2005 Food and Agriculture Organization of the
//===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
//===	and United Nations Environment Programme (UNEP)
//===
//===	This program is free software; you can redistribute it and/or modify
//===	it under the terms of the GNU General Public License as published by
//===	the Free Software Foundation; either version 2 of the License, or (at
//===	your option) any later version.
//===
//===	This program is distributed in the hope that it will be useful, but
//===	WITHOUT ANY WARRANTY; without even the implied warranty of
//===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
//===	General Public License for more details.
//===
//===	You should have received a copy of the GNU General Public License
//===	along with this program; if not, write to the Free Software
//===	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//===
//===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
//===	Rome - Italy. email: GeoNetwork@fao.org
//==============================================================================

package org.fao.geonet.kernel.search.classifier;

import static org.fao.geonet.test.CategoryTestHelper.assertCategoryListEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.fao.geonet.kernel.ThesaurusManager;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.sesame.config.ConfigurationException;

public class BroaderTermTest extends AbstractBroaderTermTest {

    private Classifier broaderTermClassifier;
    
    @Before 
    public void setup() throws IOException, ConfigurationException {
        ThesaurusManager manager = mockThesaurusManagerWith("BroaderTerm.rdf");
        broaderTermClassifier = new BroaderTerm(manager, "scheme", "eng");
    }

    @Test
    public void testWithTermWithBroaderTermWithBroaderTerm() {
        List<CategoryPath> testTermHierarchy = broaderTermClassifier.classify("http://www.my.com/#sea_surface_temperature");
        assertCategoryListEquals(testTermHierarchy, "ocean>ocean temperature>sea surface temperature");
    }

    @Test
    public void testWithTermWithTwoBroaderTerms() {
        List<CategoryPath> testTermHierarchy = broaderTermClassifier.classify("http://www.my.com/#air_sea_flux");
        assertCategoryListEquals(testTermHierarchy, "physical - air>air sea flux", "physical - water>air sea flux");
    }

    @Test
    public void testWithUnknownTerm() {
        List<CategoryPath> testTermHierarchy = broaderTermClassifier.classify("http://www.my.com/#unkown-term");
        assertEquals(0, testTermHierarchy.size());
    }

}
