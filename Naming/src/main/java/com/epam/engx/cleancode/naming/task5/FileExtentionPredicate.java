package com.epam.engx.cleancode.naming.task5;


import com.epam.engx.cleancode.naming.task5.thirdpartyjar.Predicate;

public class FileExtentionPredicate implements Predicate<String> {

    private String[] extentions;

    FileExtentionPredicate(String[] extentions) {
        this.extentions = extentions;
    }

    @Override
    public boolean test(String fileName) {
        for (String extension : extentions) {
            if (fileName.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
