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

import de.gerdiproject.json.datacite.extension.generic.WebLink;
import de.gerdiproject.json.datacite.extension.generic.enums.WebLinkType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A static collection of constant parameters for assembling ClinicalTrials URLs.
 * 
 * @author Komal Ahir
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClinicalTrialsUrlConstants
{
    // URLs
    public static final String VIEW_URL = "https://clinicaltrials.gov/ct2/show/NCT%08d?displayxml=true";

    // NAMES
    public static final String VIEW_DOCUMENT =  "View Document";
    public static final String STUDY_RECORD_DETAIL = "Study Record Detail";
    public static final WebLink LOGO_WEB_LINK = createLogoWebLink();

    
    private static WebLink createLogoWebLink()
    {
        final WebLink logoLink = new WebLink("https://clinicaltrials.gov/ct2/html/images/ct.gov-nlm-nih-logo.png");
        logoLink.setName("logo");
        logoLink.setType(WebLinkType.ProviderLogoURL);
        return logoLink;
    }
}
