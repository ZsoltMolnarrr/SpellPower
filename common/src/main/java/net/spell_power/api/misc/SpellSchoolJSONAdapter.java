package net.spell_power.api.misc;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.io.IOException;

public class SpellSchoolJSONAdapter extends TypeAdapter<SpellSchool> {
    @Override
    public void write(JsonWriter jsonWriter, SpellSchool school) throws IOException {
        jsonWriter.value(school.id.toString());
    }

    @Override
    public SpellSchool read(JsonReader jsonReader) throws IOException {
        return SpellSchools.getSchool(jsonReader.nextString());
    }
}
