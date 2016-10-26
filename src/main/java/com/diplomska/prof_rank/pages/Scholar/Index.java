package com.diplomska.prof_rank.pages.Scholar;

import com.diplomska.prof_rank.annotations.InstructorPage;
import com.diplomska.prof_rank.entities.Reference;
import com.diplomska.prof_rank.entities.ReferenceType;
import com.diplomska.prof_rank.pages.Reference.CreateReference;
import com.diplomska.prof_rank.services.ReferenceHibernate;
import com.diplomska.prof_rank.services.ReferenceTypeHibernate;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aleksandar on 21-Oct-16.
 */
@InstructorPage
public class Index {

    @Persist
    @Property
    Integer numScholar;

    @Persist
    @Property
    List<String> allScholarResults;

    @Persist
    @Property
    List<String> scholarResults;

    @Property
    String scholarResult;

    @Property
    ReferenceType referenceType;

    @Inject
    ReferenceTypeHibernate referenceTypeHibernate;

    void setupRender() throws Exception {
        referenceType = referenceTypeHibernate.getByColumn("name", "Papers").get(0);

        if (numScholar == null) {
            numScholar = -1;
        }

        if (allScholarResults == null) {
            allScholarResults = new ArrayList<String>();
            scholarResults = new ArrayList<String>();

            readScholar();
        }

        if (searchScholarResult == null) {
            searchScholarResult = "";
        }

        if (scholarResultTitleQueryString != null) {
            scholarResults = filterScholarResultsByTitle(scholarResultTitleQueryString);

            if (scholarResults.size() == 0) {
                scholarResults = allScholarResults;
            }
        }
    }

    @Inject
    ReferenceHibernate referenceHibernate;

    void readScholar() throws Exception{
        String command = "python scholar.py --author \"vangel ajanovski\"";

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", command);
        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        numScholar = 0;
        allScholarResults = new ArrayList<String>();
        String paper = "";
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            line = line.trim();
            String lineToLower = line.toLowerCase();
            if (lineToLower.startsWith("title")) {
                paper += line.substring(6);
            } else if (lineToLower.startsWith("year")) {
                paper = line.substring(5) + ", " + paper;
            }
            if (line.trim().length() == 0) {
                allScholarResults.add(paper);
                numScholar++;
                paper = "";
            }
        }

        List<String> allDisplayNames = referenceHibernate.getAllDisplayNames();

        for (String result: allScholarResults) {
            String title = result;
            String[] splitResult = result.split(", ");
            if (splitResult.length > 1) {
                title = splitResult[1];
            }

            for (String name: allDisplayNames) {
                if (name.startsWith(title)) {
                    allDisplayNames.remove(result);
                    break;
                }
            }
        }

        Collections.sort(allScholarResults, Collections.reverseOrder());
        scholarResults = allScholarResults;
    }

    @InjectPage
    CreateReference createReferencePage;

    Object onActionFromCreateReference(String title) {
        ReferenceType referenceType = referenceTypeHibernate.getByColumn("name", "Papers").get(0);
        Link link = createReferencePage.setPhrase(referenceType.getId(), title);

        return link;
    }

    public boolean isResultsNotNull() {
        return scholarResults.size() > 0 ? true : false;
    }

    @Property
    String searchScholarResult;

    List<String> onProvideCompletionsFromSearchName(String partial){
        return filterScholarResultsByTitle(partial);
    }

    List<String> filterScholarResultsByTitle(String title) {
        if (title == null || title == ""){
            return new ArrayList<String>();
        }

        List<String> matches = new ArrayList<String>();
        title = title.toUpperCase();

        for (String name : scholarResults) {
            String[] splitName = name.split(", ");
            if (splitName.length > 1) {
                name = splitName[1];
            }

            if (name.toUpperCase().startsWith(title)) {
                matches.add(name);
            }
        }

        return matches;
    }

    public Object onSuccessFromForm() {
        Link link = this.set(searchScholarResult);

        return link;
    }

    @ActivationRequestParameter(value = "title")
    private String scholarResultTitleQueryString;

    @Inject
    PageRenderLinkSource pageRenderLinkSource;

    public Link set(String scholarResultTitle) {
        if (scholarResultTitle == null){
            this.scholarResultTitleQueryString = "";
        } else {
            this.scholarResultTitleQueryString = scholarResultTitle;
        }

        return pageRenderLinkSource.createPageRenderLink(this.getClass());
    }
}