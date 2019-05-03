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
package de.gerdiproject.harvest.clinicaltrials.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A static collection of constant parameters for configuring the ClinicalTrials Harvester.
 * @author Komal Ahir
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClinicalTrialsConstants
{

    public static final String PROVIDER = "U.S. National Library of Medicine";
    public static final String LANGUAGE = "en";
    public static final String AGENCY = "agency";
    public static final String BRIEF_TITLE = "brief_title";
    public static final String OFFICIAL_TITLE = "official_title";
    public static final String DETAILED_DESCRIPTION = "detailed_description";
    public static final String STUDY_FIRST_SUBMITTED = "study_first_submitted";
    public static final String STUDY_FIRST_POSTED = "study_first_posted";
    public static final String LAST_UPDATE_SUBMITTED = "last_update_submitted";
    public static final String LAST_UPDATE_POSTED = "last_update_posted";
    public static final String KEYWORD = "keyword";
    public static final String MESH_TERM = "mesh_term";
    public static final String SPONSORS = "sponsors";
    public static final String STUDY_RECORD_DETAIL_URL = "url";
    public static final String VIEW_DOCUMENT_URL = "document_url";
    public static final String COUNTRY = "country";
    public static final String OVERALL_STATUS = "overall_status";
    public static final String OVERALL_CONTACT = "overall_contact";

    public static final int CLINICAL_TRIALS_DOC_COUNT = 10000;
    public static final String NCT_ID = "nct_id";
}


