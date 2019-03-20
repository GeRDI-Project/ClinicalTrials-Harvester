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
import de.gerdiproject.json.datacite.DataCiteJson;


import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;



import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.Description;







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
        document.addTitles(getTitles(vo));
        document.addDescriptions(getDescriptions(vo));
       

        // TODO add all possible metadata to the document
       
        
        return document;
    }
    private List<Title> getTitles(ClinicalTrialsVO vo)
    {
        final List<Title> titlelist = new LinkedList<>();

        // get the title
        final Element title = vo
                                 .getViewPage()
                                 .selectFirst("brief_title");

        if (title != null)
            titlelist.add(new Title(title.text()));
       

        return titlelist;
    }
    private List<Description> getDescriptions(ClinicalTrialsVO vo)
    {
        final List<Description> Descriptionlist = new LinkedList<>();

        final Element descriptions = vo
                .getViewPage()
                .selectFirst("detailed_description");
        
        if (descriptions != null)
            Descriptionlist.add(new Description(descriptions.wholeText(), null));

        return Descriptionlist;
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


