/**
 * Copyright © 2019 Komal Ahir (http://www.gerdi-project.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gerdiproject.harvest.etls.transformers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.etls.extractors.ClinicalTrialsVO;

import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Date;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.RelatedIdentifier;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.enums.DateType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.enums.RelatedIdentifierType;
import de.gerdiproject.json.datacite.enums.RelationType;
import de.gerdiproject.json.datacite.extension.generic.ResearchData;
import de.gerdiproject.json.datacite.extension.generic.WebLink;
import de.gerdiproject.json.datacite.extension.generic.enums.WebLinkType;


/**
 * This transformer parses metadata from a {@linkplain ClinicalTrialsVO}
 * and creates {@linkplain DataCiteJson} objects from it.
 *
 * @author Komal Ahir
 */
public class ClinicalTrialsTransformer extends AbstractIteratorTransformer<ClinicalTrialsVO, DataCiteJson>
{
    @Override
    public void init(AbstractETL<?, ?> etl)
    {
        super.init(etl);
    }


    @Override
    protected DataCiteJson transformElement(ClinicalTrialsVO vo) throws TransformerException
    {
        // create the document
        final DataCiteJson document = new DataCiteJson(String.valueOf(vo.getId()));

        // TODO add all possible metadata to the document
        
        document.addDates(getDates(vo));
        document.addGeoLocations(getGeoLocations(vo));


        return document;
    }


    /**
     * Creates a unique identifier for a document from ClinicalTrials.
     *
     * @param source the source object that contains all metadata that is needed
     *
     * @return a unique identifier of this document
     */

    private List<GeoLocation> getGeoLocations(ClinicalTrialsVO vo)
    {
        final List<GeoLocation> geoLocations = new LinkedList<>();

        // get the area name element
        final Element areaElem = (vo)
                                 .getViewPage()
                                 .selectFirst("div.right-margin:containsOwn(Area:) + div.left_margin");

        if (areaElem != null)
            geoLocations.add(new GeoLocation(areaElem.text()));

        return geoLocations;
    }
    private List<AbstractDate> getDates(ClinicalTrialsVO vo)
    {
        final List<AbstractDate> dates = new LinkedList<>();

        // retrieve the measurement date selector
        final Elements study_first_submitted = vo.getViewPage().select("clinical_study");

        // verify that there are dates
        
        return dates;
    }
}




