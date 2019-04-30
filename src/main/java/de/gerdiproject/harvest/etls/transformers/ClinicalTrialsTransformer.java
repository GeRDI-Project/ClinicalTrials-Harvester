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

import de.gerdiproject.harvest.clinicaltrials.constants.ClinicalTrialsUrlConstants;
import de.gerdiproject.harvest.clinicaltrials.constants.ClinicalTrialsConstants;
import de.gerdiproject.harvest.etls.extractors.ClinicalTrialsVO;
import de.gerdiproject.harvest.utils.HtmlUtils;
import de.gerdiproject.json.datacite.DataCiteJson;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.enums.DateType;
import de.gerdiproject.json.datacite.extension.generic.WebLink;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Date;
import de.gerdiproject.json.datacite.Contributor;
import de.gerdiproject.json.datacite.FundingReference;


/**
 * This transformer parses metadata from a {@linkplain ClinicalTrialsVO}
 * and creates {@linkplain DataCiteJson} objects from it.
 *
 * @author Komal Ahir
 */
public class ClinicalTrialsTransformer extends AbstractIteratorTransformer<ClinicalTrialsVO, DataCiteJson>
{
    @Override
    protected DataCiteJson transformElement(ClinicalTrialsVO vo) throws TransformerException
    {
        // create the document
        final Document viewPage = vo.getViewPage();
        final String nctId = HtmlUtils.getString(viewPage, ClinicalTrialsConstants.NCT_ID);
        final DataCiteJson document = new DataCiteJson(nctId);
        // add all possible metadata to the document
        document.setPublisher(ClinicalTrialsConstants.PROVIDER);
        document.setLanguage(ClinicalTrialsConstants.LANGUAGE);

        document.addTitles(getTitles(vo));
        document.addDescriptions(getDescription(vo));
        document.addDates(getDates(vo));
        // TODO; REFACTOR CONTACTS like SUBJECTS see below
        // document.addContributors(getSponsors(vo));
        document.addWebLinks(getWebLinkList(vo));
        document.addGeoLocations(getGeoLocations(vo));

        document.addSubjects(HtmlUtils.getObjects(viewPage, ClinicalTrialsConstants.KEYWORD, this::parseSubject));
        document.addSubjects(HtmlUtils.getObjects(viewPage, ClinicalTrialsConstants.MESH_TERM, this::parseSubject));
        document.addSubjects(HtmlUtils.getObjects(viewPage, ClinicalTrialsConstants.OVERALL_STATUS, this::parseSubject));
        document.addFundingReferences(HtmlUtils.getObjectsFromParent(viewPage, ClinicalTrialsConstants.SPONSORS, this::parseFunder));

        return document;
    }

    /**
     * Create a new Subject from an elements text
     * @param ele
     * @return
     */
    private Subject parseSubject(Element ele)
    {
        return new Subject(ele.text());
    }

    private FundingReference parseFunder(Element ele)
    {
        final String funderName = HtmlUtils.getString(ele, ClinicalTrialsConstants.AGENCY);

        if(funderName != null)
            return new FundingReference(funderName);
        else
            return null;
    }

    private List<Title> getTitles(ClinicalTrialsVO vo)
    {
        final List<Title> titleList = new LinkedList<>();

        // get the title
        final Element brief_title = vo.getViewPage().selectFirst(ClinicalTrialsConstants.BRIEF_TITLE);
        final Element official_title = vo.getViewPage().selectFirst(ClinicalTrialsConstants.OFFICIAL_TITLE);

        // verify that there is data
        if (brief_title != null)
            titleList.add(new Title(brief_title.text()));

        if (official_title != null)
            titleList.add(new Title(official_title.text()));

        return titleList;
    }

    private List<Description> getDescription(ClinicalTrialsVO vo)
    {
        final List<Description> description = new LinkedList<>();
        // get the description
        final Element detailedDescription = vo.getViewPage().selectFirst(ClinicalTrialsConstants.DETAILED_DESCRIPTION);
        // verify that there is data
        if (detailedDescription != null)
            description.add(new Description(detailedDescription.wholeText(), null));

        return description;
    }

    private List<AbstractDate> getDates(ClinicalTrialsVO vo)
    {
        final List<AbstractDate> dates = new LinkedList<>();
        // retrieve the first and last submitted date
        final String submissionDate = HtmlUtils.getString(vo.getViewPage(), ClinicalTrialsConstants.STUDY_FIRST_SUBMITTED);
        final String lastSubmissionDate = HtmlUtils.getString(vo.getViewPage(), ClinicalTrialsConstants.LAST_UPDATE_SUBMITTED);
        // verify that there are dates
        if (submissionDate != null)
            dates.add(new Date(submissionDate, DateType.Collected));

        if (lastSubmissionDate != null)
            dates.add(new Date(lastSubmissionDate, DateType.Collected));

        return dates;
    }

    private List<Contributor> getSponsors(ClinicalTrialsVO vo)
    {
        final List<Contributor> overallContact = new LinkedList<>();
        // retrieve the overall contact
        final Elements overallContacts  = vo.getViewPage().select(ClinicalTrialsConstants.OVERALL_CONTACT);

        for (Element contact : overallContacts) {
            Contributor contrib = new Contributor(contact.text(), null);
            overallContact.add(contrib);
        }

        return overallContact;
    }

    private List<WebLink> getWebLinkList(ClinicalTrialsVO vo)
    {
        final List<WebLink> webLinkList = new LinkedList<>();
        // retrieve the url,document links and logo url
        final Elements linkElements = vo.getViewPage().select(ClinicalTrialsConstants.URL);
        final Elements docElements = vo.getViewPage().select(ClinicalTrialsConstants.DOCUMENT_URL);

        for (Element linkElement : linkElements) {
            WebLink weblink = new WebLink(linkElement.text());
            weblink.setName(ClinicalTrialsUrlConstants.URL_NAME);
            webLinkList.add(weblink);
        }

        for (Element docElement : docElements) {
            WebLink weblink = new WebLink(docElement.text());
            weblink.setName(ClinicalTrialsUrlConstants.DOCUMENT_URL_NAME);
            webLinkList.add(weblink);
        }

        webLinkList.add(ClinicalTrialsUrlConstants.LOGO_WEB_LINK);
        return webLinkList;
    }

    private List<GeoLocation> getGeoLocations(ClinicalTrialsVO vo)
    {
        final List<GeoLocation> geoLocations = new LinkedList<>();
        // get the location; TODO: fetch all locations
        final Element locationName = vo.getViewPage().selectFirst(ClinicalTrialsConstants.COUNTRY);
        // verify that there is data
        if (locationName != null)
            geoLocations.add(new GeoLocation(locationName.text()));

        return geoLocations;
    }

    /**
     * Creates a unique identifier for a document from ClinicalTrials.
     *
     * @param source the source object that contains all metadata that is needed
     *
     * @return a unique identifier of this document
     */
}
