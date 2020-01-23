package nl.praegus.fitnesse.responders.testHistory;

import fitnesse.reporting.history.PageHistory;
import fitnesse.wiki.PathParser;
import util.FileUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class TestHistory {
    private List<TestHistoryLine> testHistoryLineList = new ArrayList<>();

    final Map<String, File> pageDirectoryMap = new HashMap<>();

    public TestHistory(File historyDirectory) {
        readHistoryDirectory(historyDirectory);
    }

    public TestHistory(File historyDirectory, String pageName) {
        readPageHistoryDirectory(historyDirectory, pageName);
    }


    public Set<String> getPageNames() {
        return new TreeSet<>(pageDirectoryMap.keySet());
    }

    public PageHistory getPageHistory(String pageName) {
        File pageHistoryDirectory = pageDirectoryMap.get(pageName);
        if (pageHistoryDirectory == null)
            return null;
        else {
            PageHistory pageHistory = new PageHistory(pageHistoryDirectory);
            if (pageHistory.size() == 0)
                return null;
            else
                return pageHistory;
        }
    }

    public List<TestHistoryLine> getSortedLines() {

        return testHistoryLineList
                .stream()
                .sorted(comparing(TestHistoryLine::getLastRun, nullsLast(reverseOrder())))
                .collect(toList());
    }

    private void readHistoryDirectory(File historyDirectory) {
        File[] pageDirectories = FileUtil.getDirectoryListing(historyDirectory);
        for (File file : pageDirectories)
            if (isValidFile(file))
                pageDirectoryMap.put(file.getName(), file);
    }

    private boolean isValidFile(File file) {
        return file.isDirectory() && file.list().length > 0 && PathParser.isWikiPath(file.getName());
    }

    private void readPageHistoryDirectory(File historyDirectory, String pageName) {
        File[] pageDirectories = FileUtil.getDirectoryListing(historyDirectory);
        for (File file : pageDirectories)
            if ((isValidFile(file)) && file.getName().startsWith(pageName))
                pageDirectoryMap.put(file.getName(), file);
    }

}
