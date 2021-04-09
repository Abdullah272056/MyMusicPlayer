package com.example.mymusicplayer.theme;

public class ThemeNote {
    int id ;
    int themeStatus;

    public ThemeNote() {
    }

    public ThemeNote(int themeStatus) {
        this.themeStatus = themeStatus;
    }

    public ThemeNote(int id, int themeStatus) {
        this.id = id;
        this.themeStatus = themeStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThemeStatus() {
        return themeStatus;
    }

    public void setThemeStatus(int themeStatus) {
        this.themeStatus = themeStatus;
    }
}
