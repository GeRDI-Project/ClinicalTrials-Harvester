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



import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.etls.extractors.ClinicalTrialsVO;
import de.gerdiproject.harvest.utils.HtmlUtils;
import de.gerdiproject.json.datacite.DataCiteJson;


import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.enums.DateType;
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
        document.setPublisher("U.S. National Library of Medicine");
        document.setLanguage("English");
        document.addTitles(getTitles(vo));
        document.addDescriptions(getDescriptions(vo));
        document.addDates(getDates(vo));
        document.addDates(getlastdate(vo));
        document.addSubjects(getKeyword(vo));
        document.addSubjects(getMeshterm(vo));
        document.addContributors(getsponsors(vo));
        document.addWebLinks(getlink(vo));
        document.addWebLinks(getlogoLink(vo));
        
        document.addWebLinks(getdocumenturl(vo));
        document.addGeoLocations(getgeolocationPlace(vo));
        
        return document;
        		
    }  		
        
   
   private List<Title> getTitles(ClinicalTrialsVO vo)  
    {
        final List<Title> titlelist = new LinkedList<>();

        // get the title
        final Element title = vo.getViewPage().selectFirst("brief_title");
       // verify that there is data
        if (title != null)
            titlelist.add(new Title(title.text()));


        return titlelist;
    }


    private List<Description> getDescriptions(ClinicalTrialsVO vo)
    {
        final List<Description> Descriptionlist = new LinkedList<>();
        // get the description
        final Element descriptions = vo.getViewPage().selectFirst("detailed_description");
        // verify that there is data
        if (descriptions != null)
            Descriptionlist.add(new Description(descriptions.wholeText(), null));

        return Descriptionlist;
    }


    
    private List<AbstractDate> getDates(ClinicalTrialsVO vo)
    {
        final List<AbstractDate> dates = new LinkedList<>();

        // retrieve the first submitted date
        final Elements dateElements = vo.getViewPage().select("study_first_submitted");

        // verify that there are dates
         if (dates != null)
        	 dates.add(new Date(dateElements.text(), DateType.Collected));
         
        return dates;
    } 
    private List<AbstractDate> getlastdate(ClinicalTrialsVO vo)
    {
        final List<AbstractDate> ldates = new LinkedList<>();

        // retrieve the last updated date
        final Elements ldateElements = vo.getViewPage().select("last_update_submitted");

        // verify that there are dates
         if (ldates != null)
        	 ldates.add(new Date(ldateElements.text(), DateType.Collected));
         
        return ldates;
    }
    
    
    
	private List<Subject> getKeyword(ClinicalTrialsVO vo)
    {
        final List<Subject> keyword = new LinkedList<>();

        // retrieve the keywords
        final Elements keywordElements = vo.getViewPage().select("keyword");
        
        for (Element keywordElement : keywordElements) {
            Subject subject = new Subject(keywordElement.text());
            keyword.add(subject);
        }

        return keyword;
    }
	
	private List<Subject> getMeshterm(ClinicalTrialsVO vo)
    {
        final List<Subject> meshterm = new LinkedList<>();

        // retrieve the mesh terms
        final Elements meshtermElements = vo.getViewPage().select("mesh_term");
        
        for (Element meshtermElement : meshtermElements) {
            Subject subject = new Subject(meshtermElement.text());
            meshterm.add(subject);
        }

        return meshterm;
    }
    
    
    private List<Contributor> getsponsors(ClinicalTrialsVO vo)
    {
        final List<Contributor> sponsor = new LinkedList<>();

        // retrieve the sponsor name
        final Elements sponsorElements = vo.getViewPage().select("sponsors");

        // verify that there is data
         if (sponsor != null)
        	 sponsor.add(new Contributor(sponsorElements.text(), null));
         
        return sponsor;
    }
    
    
    private List<WebLink> getlink(ClinicalTrialsVO vo)
    {
        final List<WebLink> link = new LinkedList<>();

        // retrieve the url link
        final Elements linkElements = vo.getViewPage().select("url");

        // verify that there is data
         if (link != null)
        	 link.add(new WebLink(linkElements.text()));
         
        return link;
    }
    
    private List<WebLink> getlogoLink(ClinicalTrialsVO vo)
    {
    	//retrieve logo link
        final List<WebLink> logolinks = new LinkedList<>();
        WebLink logoLink = new WebLink("https://clinicaltrials.gov/ct2/html/images/ct.gov-nlm-nih-logo.png");
        logoLink.setName("logo");
        logoLink.setType(WebLinkType.ProviderLogoURL);
        logolinks.add(logoLink);

        return (logolinks);
    }
    
    private List<WebLink> getdocumenturl(ClinicalTrialsVO vo)
    {
        final List<WebLink> documentlink = new LinkedList<>();

        // retrieve the document link
        final Elements docElements = vo.getViewPage().select("document_url");

        // verify that there is data
         if (documentlink != null)
        	 documentlink.add(new WebLink(docElements.text()));
         
        return documentlink;
    }
    
    
    private List<GeoLocation> getgeolocationPlace(ClinicalTrialsVO vo)
    {
        final List<GeoLocation> geoLocations = new LinkedList<>();
        //final String locationName = HtmlUtils.getString(vo.getViewPage(),"country");
        // get the area name element
        final Element locationName = vo.getViewPage().selectFirst("country");
        // verify that there is data
        if (locationName!= null)
            geoLocations.add(new GeoLocation(locationName.text()));
     
        //System.out.println(vo.getViewPage());
        //System.out.println(locationName.text());
        return geoLocations;
    }

   
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


