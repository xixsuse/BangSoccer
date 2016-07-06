package upgrade.ntv.bangsoccer.Utils;

import android.util.JsonToken;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import upgrade.ntv.bangsoccer.Attraction.Area;


public class JsonReader {

    public ArrayList<Area> readJsonStream(InputStream in) throws IOException {
        android.util.JsonReader reader = new android.util.JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readAreasArray(reader);
        } finally {
            reader.close();
        }
    }

    private ArrayList<Area> readAreasArray(android.util.JsonReader reader) throws IOException {
        ArrayList<Area> areasList = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            areasList.add(readArea(reader));
        }
        reader.endArray();
        return areasList;
    }

    private Area readArea(android.util.JsonReader reader) throws IOException {

        int Id = 0;
        String Area_Name = null;
        List geo = null;
        int Area_Type = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String field = reader.nextName();
            if (field.equals("Id")) {
                Id = reader.nextInt();
            } else if (field.equals("Area_Name")) {
                Area_Name = reader.nextString();
            } else if (field.equals("geo") && reader.peek() != JsonToken.NULL) {
                geo = readGeoLocation(reader);
            } else if (field.equals("Area_Type")) {
                Area_Type = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        LatLng latLng = new LatLng(
                Double.parseDouble(geo.get(0).toString()),
                Double.parseDouble(geo.get(1).toString()));

        return new Area(Id, Area_Name, latLng, Area_Type);
    }

    private List readGeoLocation(android.util.JsonReader reader) throws IOException {
        List geo = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            geo.add(reader.nextDouble());
        }
        reader.endArray();
        return geo;
    }
}
