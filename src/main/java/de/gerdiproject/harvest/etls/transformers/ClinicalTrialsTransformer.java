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



import de.gerdiproject.harvest.clinicaltrials.constants.clinicaltrialsConstants;
import de.gerdiproject.harvest.clinicaltrials.constants.clinicaltrialsUrlConstants;
import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.etls.extractors.ClinicalTrialsVO;
import de.gerdiproject.harvest.utils.HtmlUtils;
import de.gerdiproject.json.datacite.DataCiteJson;
import java.text.SimpleDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.abstr.AbstractPerson;
import de.gerdiproject.json.datacite.enums.DateType;
import de.gerdiproject.json.datacite.extension.IDataCiteExtension;
import de.gerdiproject.json.datacite.extension.generic.WebLink;
import de.gerdiproject.json.datacite.extension.generic.enums.WebLinkType;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Date;
import de.gerdiproject.json.datacite.Contributor;


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


        document.setPublisher(clinicaltrialsConstants.PROVIDER);
        document.setLanguage(clinicaltrialsConstants.LANGUAGE);
        document.addTitles(getTitles(vo));
        document.addDescriptions(getDescriptions(vo));
        document.addDates(getDates(vo));
        document.addDates(getlastdate(vo));
        document.addSubjects(getKeyword(vo));
        document.addContributors(getsponsors(vo));
        document.addWebLinks(getlink(vo));
        document.addGeoLocations(getgeolocationPlace(vo));
        //document.addExtension(getoverallstatus(vo));

        return document;

    }

    private List<Title> getTitles(ClinicalTrialsVO vo)
    {
        final List<Title> titlelist = new LinkedList<>();

        // get the title
        final Element title = vo.getViewPage().selectFirst(clinicaltrialsConstants.BRIEF_TITLE);
        final Element officialtitle = vo.getViewPage().selectFirst(clinicaltrialsConstants.OFFICIAL_TITLE);

        // verify that there is data
        if (officialtitle != null)
            titlelist.add(new Title(officialtitle.text()));

        if (title != null)
            titlelist.add(new Title(title.text()));

        return titlelist;
    }


    private List<Description> getDescriptions(ClinicalTrialsVO vo)
    {
        final List<Description> descriptionlist = new LinkedList<>();
        // get the description
        final Element descriptions = vo.getViewPage().selectFirst(clinicaltrialsConstants.DETAILED_DESCRIPTION);
        // verify that there is data

        if (descriptions != null)
            descriptionlist.add(new Description(descriptions.wholeText(), null));

        return descriptionlist;
    }


    /*private List<AbstractDate> getDates(ClinicalTrialsVO vo) throws ParseException
    {
        final List<AbstractDate> dates = new LinkedList<>();

        // retrieve the first submitted date

        final String dateElements = HtmlUtils.getString(vo.getViewPage(), clinicaltrialsConstants.STUDY_FIRST_SUBMITTED);

        String sDate  = dateElements;
        SimpleDateFormat formatter = new SimpleDateFormat(clinicaltrialsConstants.DATE_FORMAT);

        try {
            java.util.Date date = formatter.parse(sDate);

            //System.out.println(date);
            DateFormat dateFormat = new SimpleDateFormat(clinicaltrialsConstants.DATE_FORMAT_REQUIRED);
            String strDate = dateFormat.format(date);

            //System.out.println("Converted String: " + strDate);

            // verify that there are dates
            if (dates != null)
                dates.add(new Date(strDate, DateType.Collected));
        } catch (ParseException e) { //NOPMD do nothing. just do not add the date if it does not exist
        }

        return dates;
    }
    private List<AbstractDate> getlastdate(ClinicalTrialsVO vo) throws ParseException
    {
        final List<AbstractDate> ldates = new LinkedList<>();

        // retrieve the last updated date

        final String ldateElements = HtmlUtils.getString(vo.getViewPage(), clinicaltrialsConstants.LAST_UPDATE_SUBMITTED);
        String lDate  = ldateElements;
        SimpleDateFormat formatter = new SimpleDateFormat(clinicaltrialsConstants.DATE_FORMAT);

        try {
            java.util.Date ldate = formatter.parse(lDate);

            //System.out.println(ldate);
            DateFormat dateFormat = new SimpleDateFormat(clinicaltrialsConstants.DATE_FORMAT_REQUIRED);
            String lstrDate = dateFormat.format(ldate);

            //System.out.println("Converted String: " + lstrDate);

            // verify that there are dates
            if (ldates != null)
                ldates.add(new Date(lstrDate, DateType.Collected));
        } catch (ParseException e) { //NOPMD do nothing. just do not add the date if it does not exist
        }


        return ldates;
    }*/

    private List<AbstractDate> getDates(ClinicalTrialsVO vo)
    {
        final List<AbstractDate> dates = new LinkedList<>();

        // retrieve the first submitted date

        final String dateElements = HtmlUtils.getString(vo.getViewPage(), clinicaltrialsConstants.STUDY_FIRST_SUBMITTED);

        // verify that there are dates
        if (dates != null)
            dates.add(new Date(dateElements, DateType.Collected));

        return dates;
    }

    private List<AbstractDate> getlastdate(ClinicalTrialsVO vo)
    {
        final List<AbstractDate> ldates = new LinkedList<>();

        // retrieve the last updated date

        final String ldateElements = HtmlUtils.getString(vo.getViewPage(), clinicaltrialsConstants.LAST_UPDATE_SUBMITTED);

        // verify that there are dates
        if (ldates != null)
            ldates.add(new Date(ldateElements, DateType.Collected));

        return ldates;
    }

    private List<Subject> getKeyword(ClinicalTrialsVO vo)
    {
        final List<Subject> keyword = new LinkedList<>();

        // retrieve the keywords
        final Elements keywordElements = vo.getViewPage().select(clinicaltrialsConstants.KEYWORD);
        final Elements meshtermElements = vo.getViewPage().select(clinicaltrialsConstants.MESH_TERM);

        for (Element keywordElement : keywordElements) {
            Subject subject = new Subject(keywordElement.text(), null);
            keyword.add(subject);
        }

        for (Element meshtermElement : meshtermElements) {
            Subject subject = new Subject(meshtermElement.text(), null);
            keyword.add(subject);
        }

        return keyword;
    }

    /*private List<Subject> getMeshterm(ClinicalTrialsVO vo)
    {
        final List<Subject> meshterm = new LinkedList<>();

        // retrieve the mesh terms
        final Elements meshtermElements = vo.getViewPage().select(clinicaltrialsConstants.MESH_TERM);

        for (Element meshtermElement : meshtermElements) {
            Subject subject = new Subject(meshtermElement.text());
            meshterm.add(subject);
        }

        return meshterm;
    }*/


    private List<Contributor> getsponsors(ClinicalTrialsVO vo)
    {
        final List<Contributor> sponsor = new LinkedList<>();

        // retrieve the sponsor name
        final Elements sponsorElements = vo.getViewPage().select(clinicaltrialsConstants.SPONSORS);
        final Elements overallcontacts  = vo.getViewPage().select(clinicaltrialsConstants.OVERALL_CONTACT);

        // verify that there is data
        for (Element sponsorElement : sponsorElements) {
            Contributor contributor = new Contributor(sponsorElement.text(), null);
            sponsor.add(contributor);
        }

        for (Element overallcontact : overallcontacts) {
            Contributor contact = new Contributor(overallcontact.text(), null);
            sponsor.add(contact);
        }


        return sponsor;
    }


    private List<WebLink> getlink(ClinicalTrialsVO vo)
    {
        final List<WebLink> link = new LinkedList<>();

        // retrieve the url and document links
        final Elements linkElements = vo.getViewPage().select(clinicaltrialsConstants.URL);
        final Elements docElements = vo.getViewPage().select(clinicaltrialsConstants.DOCUMENT_URL);

        // verify that there is data
        for (Element linkElement : linkElements) {
            WebLink weblink = new WebLink(linkElement.text());
            weblink.setName(clinicaltrialsUrlConstants.URL_NAME);
            link.add(weblink);
        }

        for (Element docElement : docElements) {
            WebLink weblink = new WebLink(docElement.text());
            weblink.setName(clinicaltrialsUrlConstants.DOCUMENT_URL_NAME);
            link.add(weblink);
        }

        WebLink logoLink = new WebLink(clinicaltrialsUrlConstants.LOGO_URL);
        logoLink.setName(clinicaltrialsUrlConstants.LOGO_URL_NAME);
        logoLink.setType(WebLinkType.ProviderLogoURL);
        link.add(logoLink);

        return link;
    }


    private List<GeoLocation> getgeolocationPlace(ClinicalTrialsVO vo)
    {
        final List<GeoLocation> geoLocations = new LinkedList<>();
        //final String locationName = HtmlUtils.getString(vo.getViewPage(),"country");
        // get the area name element
        final Element locationName = vo.getViewPage().selectFirst(clinicaltrialsConstants.COUNTRY);

        // verify that there is data

        if (locationName != null)
            geoLocations.add(new GeoLocation(locationName.text()));

        return geoLocations;
    }

    /*private IDataCiteExtension getoverallstatus(ClinicalTrialsVO vo)
    {
        final List<IDataCiteExtension> overallstatus = new LinkedList<>();
        final Element status = vo.getViewPage().selectFirst(clinicaltrialsConstants.OVERALL_STATUS);

        // verify that there is data

        if (status != null)
            overallstatus.add(new IDataCiteExtension(status.text()));

        return (IDataCiteExtension) overallstatus;
    }*/


    /**
     * Creates a unique identifier for a document from ClinicalTrials.
     *
     * @param source the source object that contains all metadata that is needed
     *
     * @return a unique identifier of this document
     */
    /*private String createIdentifier(ClinicalTrialsVO vo)
    {
        // TODO retrieve a unique identifier from the source
        return vo.toString();
    }*/
}


