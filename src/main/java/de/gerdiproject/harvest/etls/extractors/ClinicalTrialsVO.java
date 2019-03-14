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

import org.jsoup.nodes.Document;

import lombok.Data;

/**
 * This class is a value object that contains all extracted (meta-) data from
 * ClinicalTrials that is required to generate a document.
 *
 * @author Komal Ahir
 */
@Data
public class ClinicalTrialsVO
{
    // TODO add fields here, or replace this class with whatever suits your needs
    private int id;
    private Document viewPage;


    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public Document getViewPage()
    {
        return viewPage;
    }
    public void setViewPage(Document viewPage)
    {
        this.viewPage = viewPage;
    }
}
