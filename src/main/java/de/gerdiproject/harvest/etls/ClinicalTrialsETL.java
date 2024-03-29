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
package de.gerdiproject.harvest.etls;

import de.gerdiproject.harvest.etls.extractors.ClinicalTrialsExtractor;
import de.gerdiproject.harvest.etls.extractors.ClinicalTrialsVO;
import de.gerdiproject.harvest.etls.transformers.ClinicalTrialsTransformer;
import de.gerdiproject.json.datacite.DataCiteJson;


/**
 * An ETL for harvesting ClinicalTrials.<br>
 * See: https://clinicaltrials.gov/ct2/resources/download
 *
 * @author Komal Ahir
 */
public class ClinicalTrialsETL extends StaticIteratorETL<ClinicalTrialsVO, DataCiteJson>
{
    /**
     * Constructor
     */
    public ClinicalTrialsETL()
    {
        super(new ClinicalTrialsExtractor(), new ClinicalTrialsTransformer());
    }

}
