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
package de.gerdiproject.harvest.etls.extractors;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.ws.http.HTTPException;

import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.harvest.utils.data.enums.RestRequestType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.gerdiproject.harvest.clinicaltrials.constants.clinicaltrialsConstants;
import de.gerdiproject.harvest.clinicaltrials.constants.clinicaltrialsUrlConstants;

import de.gerdiproject.harvest.etls.AbstractETL;


/**
 * This {@linkplain AbstractIteratorExtractor} implementation extracts all
 * (meta-)data from ClinicalTrials and bundles it into a {@linkplain ClinicalTrialsVO}.
 *
 * @author Komal Ahir
 */
public class ClinicalTrialsExtractor extends AbstractIteratorExtractor<ClinicalTrialsVO>
{
    private final HttpRequester httpRequester;
    private String version = null;
    private int size = -1;


    /**
     * Simple constructor.
     */
    public ClinicalTrialsExtractor()
    {
        this.httpRequester = new HttpRequester();
    }


    @Override
    public void init(AbstractETL<?, ?> etl)
    {
        super.init(etl);

        this.httpRequester.setCharset(etl.getCharset());

        // if possible, extract some metadata in order to determine the size and a version string
        // final ClinicalTrialsETL specificEtl = (ClinicalTrialsETL) etl;
        // this.version = ;
        // this.size = ;
    }


    @Override
    public String getUniqueVersionString()
    {
        // it's not feasible to calculate the hash, because there is no overall version hence null
        return version;
    }



    @Override
    public int size()
    {
        return clinicaltrialsConstants.CLINICAL_TRIALS_DOC_COUNT;

    }


    @Override
    protected Iterator<ClinicalTrialsVO> extractAll() throws ExtractorException
    {
        return new ClinicalTrialsIterator();
    }


    public int getSize()
    {
        return size;
    }


    public void setSize(int size)
    {
        this.size = size;
    }

    /**
     * This class represents an {@linkplain Iterator} that iterates through
     * {@linkplain ClinicalTrialsVO}s used for harvesting clinicalTrials datasets by
     * trying out all IDs in a range of 0000 to 9999.
     *
     * @author Komal Ahir
     */
    private class ClinicalTrialsIterator implements Iterator<ClinicalTrialsVO>
    {
        int id = 0;


        @Override
        public boolean hasNext()
        {
            return id < size();
        }

        @Override
        public ClinicalTrialsVO next()
        {

            final String url = String.format(clinicaltrialsUrlConstants.VIEW_URL, id);
            id++;

            try {

                // suppress expected warning messages by retrieving the string response first
                final String response = httpRequester.getRestResponse(RestRequestType.GET, url, null);

                // parse HTML from String
                final Document viewPage = Jsoup.parse(response);
                return new ClinicalTrialsVO(id, viewPage);
            } catch (Exception e) { // skip this page
                return null;
            }

        }

    }
}
