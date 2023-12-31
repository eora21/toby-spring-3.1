package com.example.spring_mvc.property_editor;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.spring_mvc.level.Level;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.WebDataBinder;

class LevelPropertyEditorTest {
    LevelPropertyEditor levelEditor = new LevelPropertyEditor();

    @Test
    void StringToLevel() {
        levelEditor.setAsText("3");
        assertThat(levelEditor.getValue()).isEqualTo(Level.GOLD);
    }

    @Test
    void levelToString() {
        levelEditor.setValue(Level.BRONZE);
        assertThat(levelEditor.getAsText()).isEqualTo("1");
    }

    @Test
    void webDataBinder() {
        WebDataBinder dataBinder = new WebDataBinder(null);
        dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
        assertThat(dataBinder.convertIfNecessary("1", Level.class)).isEqualByComparingTo(Level.BRONZE);
    }
}