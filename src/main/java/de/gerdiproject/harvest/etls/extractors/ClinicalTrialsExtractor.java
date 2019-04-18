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


import java.awt.Font;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.ws.http.HTTPException;

import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.harvest.utils.data.enums.RestRequestType;

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

        // TODO if possible, extract some metadata in order to determine the size and a version string
        // final ClinicalTrialsETL specificEtl = (ClinicalTrialsETL) etl;
        // this.version = ;
        // this.size = ;
    }


    @Override
    public String getUniqueVersionString()
    {
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
     * TODO add a description here
     *
     * @author Komal Ahir
     */
    private class ClinicalTrialsIterator implements Iterator<ClinicalTrialsVO>
    {
        int id = 0;


        @Override
        public boolean hasNext()
        {
            // TODO
            return id < size();
        }


        @Override
        public ClinicalTrialsVO next()
        {
            // TODO

            String id_s = Integer.toString(id);
            String NCT_id = "NCT" + "0000000000".substring(id_s.length()) + id_s;

            final String url = String.format(clinicaltrialsUrlConstants.VIEW_URL, NCT_id);

            try {
                // check if a dataset page exists for the url

                // catch possible 404-codes
                httpRequester.getRestResponse(RestRequestType.HEAD, url, null);
                final Document viewPage = httpRequester.getHtmlFromUrl(url);
                final ClinicalTrialsVO vo =
                    viewPage == null
                    ? null
                    : new ClinicalTrialsVO(id, viewPage);
                id++;
                return vo;

            } catch (HTTPException | IOException e) { // NOPMD skip this page
                id++;
                return null;
            }

        }
    }
}
